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
}

function closePopup() {
    document.getElementById('overlay').style.display = 'none';
    document.getElementById(`order-car-menu`).style.display = `none`;
}

// Function to handle "Take me to log in" button click
function takeMeToLogin() {
    location.href = `https://danov-autoshow-656625355b99.herokuapp.com/login`;
    closePopup();
}

// Function to handle "No thanks" button click
function noThanks() {
    var carManufacturer = document.getElementById('car-manufacturer');
    var carModel = document.getElementById('car-model');
    var carYear = document.getElementById('car-year');
    carManufacturer.children[0].selected = true;
    carModel.children[0].selected = true;
    carYear.children[0].selected = true;
    document.getElementById(`response-result`).style.display = `none`;
    closePopup();
}

function orderCar(e) {
    var carManufacturer = document.getElementById('car-manufacturer');
    var carModel = document.getElementById('car-model');
    var carYear = document.getElementById('car-year');

    fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/carOrders/add`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            id: userId,
            carManufacturer: carManufacturer.value,
            carModel: carModel.value,
            carYear: carYear.value
        })
    })
        .then(response => response.text())
        .then(result => {
            console.log(result);
            if (result === `Order sent successfully.`) {
                const resultHtmlEle = document.getElementById(`response-result`);
                resultHtmlEle.textContent = result;
                resultHtmlEle.style.display = `block`;
                resultHtmlEle.style.backgroundColor = `green`;
                resultHtmlEle.style.color = `white`;
                resultHtmlEle.style.border = `5px solid green`;
                resultHtmlEle.style.borderRadius = `5px`;
                setTimeout(function () {
                    document.getElementById('order-car-menu').style.display = `none`;
                    document.getElementById(`response-result`).style.display = `none`;
                    carManufacturer.children[0].selected = true;
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
                    document.getElementById('order-car-menu').style.display = `none`;
                    document.getElementById(`response-result`).style.display = `none`;
                    carManufacturer.children[0].selected = true;
                    carModel.children[0].selected = true;
                    carYear.children[0].selected = true;
                }, 2000);
            }
        })
        .catch(err => console.log(err));
}