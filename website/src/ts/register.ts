const emailInput = document.getElementById("emailInput") as HTMLInputElement;
const passwordInput = document.getElementById("passwordInput") as HTMLInputElement;
const confirmPasswordInput = document.getElementById("confirmPasswordInput") as HTMLInputElement;
const registerButton = document.getElementById("registerButton") as HTMLButtonElement;
const registerForm = document.getElementById("registerForm") as HTMLFormElement;


document.addEventListener("DOMContentLoaded", () => {
    const signInLink = document.querySelector(".already-registered") as HTMLAnchorElement;
    signInLink?.addEventListener("click", (event) => {
        event.preventDefault();
        window.location.href = "login-page.html";
    });
});

registerButton.addEventListener("click", performRegistration)

function performRegistration(){
    const email: String = emailInput.value;
    const password: String = passwordInput.value;
    const confirmPassword: String = confirmPasswordInput.value;

    if(!verifyPassword(password, confirmPassword)){
        confirmPasswordInput.reportValidity();
    }
}

function verifyPassword(password: String, confirmPassword: String): boolean{
    if(password !== confirmPassword){
        return false;
    }
    return true;
}


function registerUser(email: String, password: String){

}