// Function to set a cookie
var authToken = getCookie("authToken");
function setCookie(name, value, days) {
    const date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    const expires = "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

// Function to get a cookie value
function getCookie(name) {
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookies = decodedCookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i];
        while (cookie.charAt(0) === ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(name + "=") === 0) {
            return cookie.substring(name.length + 1, cookie.length);
        }
    }
    return "";
}

// Function to show the pop-up
function showPopup() {
    const popupContainer = document.getElementById('popup-container');

    // Check if the cookie exists and if it has been more than 3 days
    if (!getCookie('popupShown') || (new Date() - new Date(getCookie('popupShown'))) > (3 * 24 * 60 * 60 * 1000)) {
        popupContainer.style.display = 'block';
        setCookie('popupShown', new Date().toUTCString(), 3); // Set the cookie to remember the pop-up display time
    }
}

// Function to hide the pop-up
function hidePopup() {
    document.getElementById('popup-container').style.display = 'none';
}

// Function to handle ordering a car
function popUpOrderCar() {
    hidePopup();
    if (authToken) {
        document.getElementById('order-car-menu').style.display = 'flex';
    } else {
        document.getElementById('popup').style.display = 'block';
    }
}

// Function to handle canceling the order
function cancelPopUpOrder() {
    hidePopup();
}

// Show the pop-up when the page loads
window.onload = function () {
    showPopup();
    setTimeout(function () {
        hidePopup();
    }, 30000);
};
