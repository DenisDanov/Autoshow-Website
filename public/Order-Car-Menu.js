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
    location.href = `https://danovs-autoshow-afcbab0f302b.herokuapp.com/login`;
    closePopup();
}


// Function to handle "No thanks" button click
function noThanks() {
    document.getElementById('car-manufacturer').value = ``;
    document.getElementById('car-model').value = ``;
    document.getElementById('car-year').value = ``;
    document.getElementById(`response-result`).style.display = `none`;
    closePopup();
}

function orderCar(e) {
    var carManufacturer = document.getElementById('car-manufacturer');
    var carModel = document.getElementById('car-model');
    var carYear = document.getElementById('car-year');

    let arr = [carManufacturer, carModel, carYear];
    let check = false;

    for (const ele of arr) {
        if (ele.value === ``) {
            if (ele.id === `car-manufacturer`) {
                displayErrorMessage(`error-car-manufacturer`, `This field is required`);
            } else if (ele.id === `car-year`) {
                displayErrorMessage(`error-car-year`, `This field is required`);
            } else if (ele.id === `car-model`) {
                displayErrorMessage(`error-car-model`, `This field is required`);
            }
            check = true;
        }
    }

    if (check) {
        return;
    } else {
        fetch(`https://danovs-autoshow-afcbab0f302b.herokuapp.com/api/carOrders/add`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: userId,
                carManufacturer: carManufacturer.textContent,
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
                        carManufacturer.value = ``;
                        carModel.value = ``;
                        carYear.value = ``;
                    }, 2000);
                }
            })
            .catch(err => console.log(err));
    }


}

function displayErrorMessage(id, message) {
    var errorElement = document.getElementById(id);
    errorElement.textContent = message;
    errorElement.style.display = 'block';
    if (id === `error-car-manufacturer`) {
        clearTimeout(timeOutManufacturer);
        timeOutManufacturer = setTimeout(function () {
            if (document.getElementById(`error-car-manufacturer`).style.display === `block`) {
                document.getElementById(`error-car-manufacturer`).style.display = `none`;
            }
        }, 2000);
    } else if (id === `error-car-model`) {
        clearTimeout(timeOutModel);
        timeOutModel = setTimeout(function () {
            if (document.getElementById(`error-car-model`).style.display === `block`) {
                document.getElementById(`error-car-model`).style.display = `none`;
            }
        }, 2000);
    } else if (id === `error-car-year`) {
        clearTimeout(timeOutYear);
        timeOutYear = setTimeout(function () {
            if (document.getElementById(`error-car-year`).style.display === `block`) {
                document.getElementById(`error-car-year`).style.display = `none`;
            }
        }, 2000);
    }
}

