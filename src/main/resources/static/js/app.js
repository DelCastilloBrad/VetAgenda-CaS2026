// Behavior uses data attributes and form IDs, never visual CSS classes.
document.querySelectorAll("nav a").forEach((a) => {
  if (a.pathname === location.pathname) a.setAttribute("aria-current", "page");
});
document.querySelectorAll("form[data-confirm]").forEach((form) =>
  form.addEventListener("submit", (event) => {
    if (!window.confirm(form.dataset.confirm)) event.preventDefault();
  }),
);
document
  .querySelectorAll("[data-back]")
  .forEach((button) => button.addEventListener("click", () => history.back()));
