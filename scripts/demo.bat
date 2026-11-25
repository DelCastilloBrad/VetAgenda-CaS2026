@echo off
cd /d "%~dp0\.."
if not exist dist\vetagenda.jar (
  call mvn -B -DskipTests package
  if errorlevel 1 exit /b 1
  if not exist dist mkdir dist
  copy /Y target\vetagenda-0.1.0.jar dist\vetagenda.jar
)
java -jar dist\vetagenda.jar --spring.profiles.active=demo
