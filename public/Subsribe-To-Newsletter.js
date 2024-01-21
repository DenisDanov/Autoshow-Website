<<<<<<< HEAD
let timeOutNewsLetter = setTimeout(function () {
=======
let timeOut = setTimeout(function () {
>>>>>>> 01852e0dc7d90c77ffa7d99930dce7b1ae748c81
    resultField.style.display = 'none';
}, 3500);
document.querySelector(`.subscribe-form button`).addEventListener(`click`, (e) => {
    const email = document.querySelector(`.subscribe-form input[type="email"]`).value;
    const resultField = document.getElementById(`sub-result`);
    if (email !== "") {
        fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/v1/user/newsletter-emails/add?email=${email}`, {
            method: "POST"
        })
            .then(response => response.text())
            .then(result => {
                if (result === `Thank you for subscribing to our Newsletter!`) {
                    resultField.textContent = result;
                    resultField.style.display = 'block';
                    resultField.style.border = "3px solid green";
                    resultField.style.borderRadius = "5px";
                    resultField.style.background = "green";
                    resultField.style.width = "max-content";
                    resultField.style.margin = "0.5rem auto";
<<<<<<< HEAD
                    clearTimeout(timeOutNewsLetter);
                    timeOutNewsLetter = setTimeout(function () {
=======
                    clearTimeout(timeOut);
                    timeOut = setTimeout(function () {
>>>>>>> 01852e0dc7d90c77ffa7d99930dce7b1ae748c81
                        resultField.style.display = 'none';
                    }, 3500);
                } else {
                    resultField.textContent = result;
                    resultField.style.display = 'block';
                    resultField.style.border = "3px solid red";
                    resultField.style.borderRadius = "5px";
                    resultField.style.background = "red";
                    resultField.style.width = "max-content";
                    resultField.style.margin = "0.5rem auto";
<<<<<<< HEAD
                    clearTimeout(timeOutNewsLetter);
                    timeOutNewsLetter = setTimeout(function () {
=======
                    clearTimeout(timeOut);
                    timeOut = setTimeout(function () {
>>>>>>> 01852e0dc7d90c77ffa7d99930dce7b1ae748c81
                        resultField.style.display = 'none';
                    }, 3500);
                }
            })
    } else {
        resultField.textContent = `Please fill out the field`;
        resultField.style.display = 'block';
        resultField.style.border = "3px solid red";
        resultField.style.borderRadius = "5px";
        resultField.style.background = "red";
        resultField.style.width = "max-content";
        resultField.style.margin = "0.5rem auto";
<<<<<<< HEAD
        clearTimeout(timeOutNewsLetter);
        timeOutNewsLetter = setTimeout(function () {
=======
        clearTimeout(timeOut);
        timeOut = setTimeout(function () {
>>>>>>> 01852e0dc7d90c77ffa7d99930dce7b1ae748c81
            resultField.style.display = 'none';
        }, 3500);
    }
})