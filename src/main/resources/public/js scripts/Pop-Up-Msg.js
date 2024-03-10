// Function to set a cookie
var authToken = getCookie("authToken");
const urlParams = new URLSearchParams(window.location.search);
const popup = urlParams.get('popup');
var currentUrl = window.location.href;
function setCookie(name, value, days) {
    const date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    const expires = "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

// Function to get a cookie value
function getCookiePopUpMsg(name) {
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
    if (!getCookiePopUpMsg('popupShown') || (new Date() - new Date(getCookiePopUpMsg('popupShown'))) > (3 * 24 * 60 * 60 * 1000)) {
        setCookie('popupShown', new Date().toUTCString(), 14); // Set the cookie to remember the pop-up display time
        popupContainer.style.display = 'block';
    } else {
        if (authToken && popup !== null) {
            urlParams.delete('popup');
            const newUrl = currentUrl.split('?')[0] + '?' + urlParams.toString();
            // Replace the current URL with the new one
            window.history.replaceState({}, document.title, newUrl);
        } else if (popup !== null) {
            urlParams.delete('popup');
            const newUrl = currentUrl.split('?')[0] + '?' + urlParams.toString();
            // Replace the current URL with the new one
            window.history.replaceState({}, document.title, newUrl);
        }
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
        document.getElementById('overlay').style.display = 'flex';
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
