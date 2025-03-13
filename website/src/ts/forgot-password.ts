document.addEventListener("DOMContentLoaded", () => {
    const emailInput = document.getElementById("email-input") as HTMLInputElement;
    const nextButton = document.getElementById("next-button") as HTMLButtonElement;
    const securityQuestion = document.getElementById("security-question") as HTMLParagraphElement;
    const securityAnswerInput = document.getElementById("security-asnwer") as HTMLInputElement;
    const securityAnswerButton = document.getElementById("submit-button") as HTMLButtonElement;
    const newPasswordInput = document.getElementById("new-password") as HTMLInputElement;
    const confirmPasswordInput = document.getElementById("confirm-password") as HTMLInputElement;
    const resetPasswordButton = document.getElementById("reset-password-button") as HTMLButtonElement;
    const toggleNewPassword = document.getElementById("toggle-new-password") as HTMLSpanElement;
    const toggleConfirmPassword = document.getElementById("toggle-confirm-password") as HTMLSpanElement;

    const step1 = document.getElementById("step-1") as HTMLDivElement;
    const step2 = document.getElementById("step-2") as HTMLDivElement;
    const step3 = document.getElementById("step-3") as HTMLDivElement;

    toggleNewPassword?.addEventListener('click', () => {
        if(newPasswordInput.type === "password"){
            newPasswordInput.type = "text";
        }else{
            newPasswordInput.type = "password";
        }
    });

    toggleConfirmPassword?.addEventListener('click', () => {
        if(confirmPasswordInput.type === "password"){
            confirmPasswordInput.type = "text";
        }else{
            confirmPasswordInput.type = "password";
        }
    });
    
    let userEmail: string;
    let securityQuestionText: string;

    nextButton?.addEventListener('click', async () => {
        userEmail = emailInput.value;

        if(!userEmail){
            alert("Please enter your email");
            return;
        }

        try {
            const response = await fetch(''); //TODO add url for actually getting the secuity q, new endpoint in server?
            
            if (response.ok){
                const q = await response.json();
                securityQuestionText = q.question; //TODO change to actual param
                securityQuestion.textContent = securityQuestionText;

                step1.style.display = "none";
                step2.style.display = "block";
            }else {
                const error = response.text();
                alert(error);
            }
        }catch (error) {
            console.error(error);
            alert('An error occure, please try again');
        }
    });

    securityAnswerButton?.addEventListener('click', async () => {
        const answer = securityAnswerInput.value;

        if(!answer){
            alert('Dont play, enter your answer');
            return;
        }

        try {
            const response = await fetch(''); //TODO add url for verifying the answer, prob also new endpoint?
            if (response.ok){
                step2.style.display = "none";
                step3.style.display = "block";
            }else {
                const error = response.text();
                alert(error);
            }
        }catch (error) {
            console.error(error);
            alert('An error occured, please try again');
        }
    });

    resetPasswordButton?.addEventListener('click', async () => {
        const newPassword = newPasswordInput.value;
        const confirmPassword = confirmPasswordInput.value;

        if(newPassword !== confirmPassword){
            alert('Passwords do not match, dubbelcheck spelling');
            return;
        }

        try {
            const response = await fetch(''); //TODO add url for updating user with new password, third endpoint?
            if (response.ok){
                alert('Password has been reset');
                window.location.href = "../html/login-page.html";
            }else {
                const error = response.text();
                alert(error);
            }
        }catch (error) {
            console.error(error);
            alert('An error occured, please try again');
        }
    });

});