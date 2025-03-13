console.log("hello")

const button = document.querySelector<HTMLButtonElement>("#plantInfoBtn");

if (button) {
  button.addEventListener("click", () => {
    window.location.href = "src/html/login-page.html";
  });
}