let timeOutManufacturer;
let timeOutModel;
let timeOutYear;

var authToken = getCookie("authToken");
var decodedToken;
var userId;

if (authToken !== null) {
    decodedToken = JSON.parse(atob(authToken.split('.')[1]));
    userId = decodedToken.userId;
    document.addEventListener('DOMContentLoaded', function () {
        var orderCarContainer = document.getElementById('order-car-container');
        var orderCarMenu = document.getElementById('order-car-menu');

        orderCarContainer.addEventListener('click', function () {
            orderCarMenu.style.display = 'flex';
        });
    });
    if (popup !== null) {
        document.getElementById('order-car-menu').style.display = 'flex';
        document.getElementById('popup-order-car').style.display = `flex`;
    }
} else {
    if (popup !== null) {
        document.getElementById('overlay').style.display = `flex`;
    }
}

function closePopup() {
    document.getElementById('overlay').style.display = 'none';
    document.getElementById(`order-car-menu`).style.display = `none`;
}

// Function to handle "Take me to log in" button click
function takeMeToLogin() {
    location.href = `/login`;
    closePopup();
}

// Function to handle "No thanks" button click
function noThanks() {
    document.getElementById(`response-result`).style.display = `none`;
    closePopup();
}

function orderCar(e) {
    document.querySelector(`.loading-status`).style.display = `block`;
    var carManufacturer = document.getElementById('car-manufacturer');
    var carModel = document.getElementById('car-model');
    var carYear = document.getElementById('car-year');

    if (carManufacturer.options[carManufacturer.selectedIndex].textContent !== '' &&
        carManufacturer.options[carManufacturer.selectedIndex].textContent !== "loading" &&
        carModel.value !== '' && carModel.value !== "loading" &&
        carYear.value !== '' && carYear.value !== 'loading') {
        fetch(`${window.location.origin}/api/carOrders/add`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: userId,
                carManufacturer: carManufacturer.options[carManufacturer.selectedIndex].textContent,
                carModel: carModel.value,
                carYear: carYear.value,
                authToken: authToken
            })
        })
            .then(response => response.text())
            .then(result => {
                console.log(result);
                document.querySelector(`.loading-status`).style.display = `none`;
                if (result === `Order sent successfully.`) {
                    const resultHtmlEle = document.getElementById(`response-result`);
                    resultHtmlEle.textContent = `Order made successfully.`;
                    resultHtmlEle.style.display = `block`;
                    resultHtmlEle.style.backgroundColor = `green`;
                    resultHtmlEle.style.color = `white`;
                    resultHtmlEle.style.border = `5px solid green`;
                    resultHtmlEle.style.borderRadius = `5px`;
                    setTimeout(function () {
                        document.getElementById(`response-result`).style.display = `none`;
                    }, 2000);
                } else {
                    const resultHtmlEle = document.getElementById(`response-result`);
                    resultHtmlEle.textContent = result;
                    resultHtmlEle.style.display = `block`;
                    resultHtmlEle.style.backgroundColor = `red`;
                    resultHtmlEle.style.color = `white`;
                    resultHtmlEle.style.border = `5px solid red`;
                    resultHtmlEle.style.borderRadius = `5px`;

                    setTimeout(function () {
                        document.getElementById(`response-result`).style.display = `none`;
                    }, 2000);
                }
            })
            .catch(err => console.log(err));
    } else {
        const resultHtmlEle = document.getElementById(`response-result`);
        resultHtmlEle.textContent = "Please wait for the car queries to load.";
        resultHtmlEle.style.display = `block`;
        resultHtmlEle.style.backgroundColor = `red`;
        resultHtmlEle.style.color = `white`;
        resultHtmlEle.style.border = `5px solid red`;
        resultHtmlEle.style.borderRadius = `5px`;

        setTimeout(function () {
            document.getElementById(`response-result`).style.display = `none`;
        }, 2000);
    }
}

document.querySelector(`.fa-window-close`).addEventListener(`click`, () => {
    document.getElementById('order-car-menu').style.display = `none`;
    document.getElementById(`response-result`).style.display = `none`;
});
