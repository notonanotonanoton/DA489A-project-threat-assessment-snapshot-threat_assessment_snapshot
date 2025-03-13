import { setCookie } from "./cookieUtil";

const loginForm = document.querySelector(".loginbox") as HTMLDivElement;
const emailInput = document.querySelector('input[type="email"]') as HTMLInputElement;
const passwordInput = document.getElementById("password-input") as HTMLInputElement;
const loginButton = document.querySelector('button[type="submit"]') as HTMLButtonElement;
const registerButton = document.getElementById("register-button") as HTMLButtonElement;
const togglePassword = document.getElementById("toggle-password") as HTMLSpanElement;
const forgotPasswordLink = document.querySelector(".forgot-password") as HTMLAnchorElement;

document.addEventListener("DOMContentLoaded", () => {
    registerButton?.addEventListener("click", () => {
        window.location.href = "register-page.html";
    });

    forgotPasswordLink?.addEventListener('click', (event) => {
        event.preventDefault();
        window.location.href = "forgot-password-page.html"
    });

    //TODO maybe add graphics for the eye being open and closed?
    togglePassword?.addEventListener("click", () => {
        if(passwordInput.type === "password"){
            passwordInput.type = "text";
        }else{
            passwordInput.type = "password";
        }
    });

    loginForm?.addEventListener("submit", async(event) => {
        login(event);
    });
});

async function login(event : SubmitEvent){
    event.preventDefault();
            const email = emailInput.value;
            const password = passwordInput.value;
            const user = {email, password};

            try {
                console.log("Eventlisterner triggered, inside try-block");
                const response = await fetch("http://localhost:7888/login", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(user),
                });

                if(response.ok){
                    const data = await response.json();
                    console.log("Successful login:" + data); //TODO remove

                    setCookie("accessToken", data.accessToken, 1);
                    window.location.href= "../html/home-page.html";
                }else {
                    const error = await response.text();
                    alert(`Login failed: ${error}`);
                }
            }catch (error){
                console.error("Login failiure: ", error);
                alert("An error occured during login. Please try again");
            }
}