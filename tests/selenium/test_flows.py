"""Selenium funcional. Ejecutar contra una base de demostración desechable.
Las cuentas de cada prueba se crean con UUID; no usar contra datos reales.
"""
import os
import uuid
from datetime import datetime, timedelta
from zoneinfo import ZoneInfo
from pathlib import Path
import pytest
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait, Select
from selenium.webdriver.support import expected_conditions as EC

BASE = os.getenv('BASE_URL', 'http://127.0.0.1:8080')
PASSWORD = 'DemoVet2026!'

@pytest.fixture
def browser(request):
    options = webdriver.ChromeOptions()
    options.add_argument('--headless=new')
    options.add_argument('--no-sandbox')
    options.add_argument('--disable-dev-shm-usage')
    options.add_argument('--window-size=1440,1050')
    if os.getenv('CHROME_BINARY'):
        options.binary_location = os.environ['CHROME_BINARY']
    driver = webdriver.Chrome(service=Service(os.environ['CHROMEDRIVER']) if os.getenv('CHROMEDRIVER') else Service(), options=options)
    yield driver
    driver.quit()

def visible(b, selector):
    return WebDriverWait(b, 10).until(EC.visibility_of_element_located((By.CSS_SELECTOR, selector)))

def click_visible(b, selector):
    """Center an unobstructed control and use WebDriver's normal mouse click."""
    element = WebDriverWait(b, 10).until(
        EC.element_to_be_clickable((By.CSS_SELECTOR, selector))
    )
    b.execute_script(
        "arguments[0].scrollIntoView({block:'center', inline:'nearest', behavior:'instant'});",
        element,
    )
    WebDriverWait(b, 10).until(lambda driver: driver.execute_script("""
        const element = arguments[0];
        const rect = element.getBoundingClientRect();
        const x = rect.left + rect.width / 2;
        const y = rect.top + rect.height / 2;
        if (rect.width <= 0 || rect.height <= 0 ||
            x < 0 || x >= window.innerWidth || y < 0 || y >= window.innerHeight) {
            return false;
        }
        const top = document.elementFromPoint(x, y);
        return top !== null && (top === element || element.contains(top));
    """, element))
    element.click()

def fill(b, selector, value):
    el = visible(b, selector)
    el.clear()
    el.send_keys(value)

def login(b, role='cliente'):
    b.get(BASE + '/login')
    fill(b, '#username', role + '@vetagenda.test')
    fill(b, '#password', PASSWORD)
    b.find_element(By.ID, 'login-submit').click()
    visible(b, '.sidebar')

def new_client(b):
    token = uuid.uuid4().hex[:10]
    b.get(BASE + '/registro')
    fill(b, '#name', 'Paciente ' + token)
    fill(b, '#dni', str(20000000 + uuid.uuid4().int % 70000000))
    fill(b, '#phone', '987654321')
    fill(b, '#email', token + '@prueba.test')
    fill(b, '#password', PASSWORD)
    b.find_element(By.ID, 'consent').click()
    b.find_element(By.ID, 'register-submit').click()
    visible(b, '#success-message')
    fill(b, '#username', token + '@prueba.test')
    fill(b, '#password', PASSWORD)
    b.find_element(By.ID, 'login-submit').click()
    visible(b, '.sidebar')
    b.get(BASE + '/mascotas')
    fill(b, '#name', 'Nube ' + token)
    fill(b, '#breed', 'Mestiza')
    b.find_element(By.ID, 'save-pet').click()
    assert 'Mascota guardada' in visible(b, '#success-message').text
    return token

def book(b):
    b.get(BASE + '/reservar')
    for selector in ['petId', 'serviceId', 'vetId']:
        Select(visible(b, '#' + selector)).select_by_index(1)
    date = datetime.now(ZoneInfo('America/Lima')).date() + timedelta(days=2)
    while date.weekday() == 6:
        date += timedelta(days=1)
    # Native date input's textual key format varies by browser locale.
    b.execute_script("arguments[0].value=arguments[1]", b.find_element(By.ID, 'date'), date.isoformat())
    b.find_element(By.ID, 'search-slots').click()
    radio = visible(b, '.slot span')
    radio.click()
    click_visible(b, '#confirm-booking')
    assert 'Cita reservada' in visible(b, '#success-message').text
    return visible(b, '[data-appointment-id]').get_attribute('data-appointment-id')

def test_01_access_correct(browser):
    login(browser)
    assert visible(browser, 'h1').text == 'Todo listo para cuidarlos'

def test_02_invalid_credentials(browser):
    browser.get(BASE + '/login')
    fill(browser, '#username', 'inexistente@prueba.test')
    fill(browser, '#password', 'Incorrecta123!')
    browser.find_element(By.ID, 'login-submit').click()
    assert 'incorrectos' in visible(browser, '#login-error').text

def test_03_role_restriction(browser):
    login(browser)
    browser.get(BASE + '/admin')
    assert 'otro permiso' in visible(browser, 'h1').text

def test_04_registration_pet_and_reservation(browser):
    token = new_client(browser)
    book(browser)
    assert 'RESERVADA' in visible(browser, '.va-table').text
    assert token in visible(browser, '.va-table').text

def test_05_server_rejects_invalid_dni(browser):
    browser.get(BASE + '/registro')
    for selector, value in {'#name':'Inválido','#dni':'123','#phone':'987654321','#email':'invalido@prueba.test','#password':PASSWORD}.items():
        fill(browser, selector, value)
    browser.find_element(By.ID, 'consent').click()
    browser.execute_script("arguments[0].noValidate=true", browser.find_element(By.CSS_SELECTOR, 'form'))
    browser.find_element(By.ID, 'register-submit').click()
    assert 'DNI' in visible(browser, '#error-message').text

def test_06_history_query(browser):
    new_client(browser)
    browser.get(BASE + '/historial')
    Select(visible(browser, '#petId')).select_by_index(1)
    browser.find_element(By.ID, 'search-history').click()
    assert 'Su historia está por comenzar' in visible(browser, 'main').text

def test_07_checkin_attention_and_history(browser):
    # The fresh demo seed includes appointment 1 for today. This case changes it once.
    login(browser, 'recepcion')
    browser.get(BASE + '/citas')
    row = visible(browser, '[data-appointment-id="1"]')
    row.find_element(By.CSS_SELECTOR, 'form[action$="/llegada"] button').click()
    assert 'Llegada confirmada' in visible(browser, '#success-message').text
    browser.find_element(By.ID, 'logout').click()
    login(browser, 'vet')
    browser.get(BASE + '/citas/1/atender')
    for selector, value in {'#reason':'Control de prueba Selenium','#diagnosis':'Salud estable','#treatment':'Control en 30 días','#weight':'12.5'}.items():
        fill(browser, selector, value)
    browser.find_element(By.ID, 'save-attention').click()
    assert 'Atención registrada' in visible(browser, '#success-message').text
    row = visible(browser, '[data-appointment-id="1"]')
    row.find_element(By.LINK_TEXT, 'Historial').click()
    assert 'Control de prueba Selenium' in visible(browser, '.clinical-card').text

def test_08_logout(browser):
    login(browser)
    browser.find_element(By.ID, 'logout').click()
    assert '/login?logout' in browser.current_url
    browser.get(BASE + '/citas')
    visible(browser, '#login-submit')
    assert '/login' in browser.current_url

def test_09_theme_persists_and_resets(browser):
    login(browser, 'admin')
    browser.get(BASE + '/admin/apariencia')
    browser.execute_script("arguments[0].value='#315e9d'", browser.find_element(By.ID, 'primary'))
    Select(browser.find_element(By.ID, 'radius')).select_by_value('8')
    browser.find_element(By.ID, 'save-theme').click()
    visible(browser, '#success-message')
    assert browser.execute_script("return getComputedStyle(document.documentElement).getPropertyValue('--va-primary').trim()") == '#315e9d'
    browser.refresh()
    assert browser.find_element(By.ID, 'primary').get_attribute('value') == '#315e9d'
    browser.find_element(By.ID, 'reset-theme').click()
    WebDriverWait(browser, 5).until(EC.alert_is_present()).accept()
    visible(browser, '#success-message')
    assert browser.find_element(By.ID, 'primary').get_attribute('value') == '#157b68'

def test_10_mobile_layout(browser):
    login(browser)
    browser.set_window_size(360, 850)
    for path in ['/', '/mascotas', '/reservar', '/citas']:
        browser.get(BASE + path)
        visible(browser, 'h1')
        assert browser.execute_script('return document.documentElement.scrollWidth <= window.innerWidth'), path

def test_11_reschedule_and_cancel(browser):
    new_client(browser)
    ident = book(browser)
    row = visible(browser, '[data-appointment-id="' + ident + '"]')
    row.find_element(By.LINK_TEXT, 'Reprogramar').click()
    for selector in ['serviceId', 'vetId']:
        Select(visible(browser, '#' + selector)).select_by_index(1)
    browser.find_element(By.ID, 'search-slots').click()
    visible(browser, '.slot span').click()
    click_visible(browser, '#confirm-booking')
    assert 'reprogramada' in visible(browser, '#success-message').text
    row = visible(browser, '[data-appointment-id="' + ident + '"]')
    row.find_element(By.CSS_SELECTOR, 'form[action$="/cancelar"] button').click()
    WebDriverWait(browser, 5).until(EC.alert_is_present()).accept()
    assert 'cancelada' in visible(browser, '#success-message').text
    assert 'CANCELADA' in visible(browser, '[data-appointment-id="' + ident + '"]').text
