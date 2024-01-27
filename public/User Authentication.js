//Function to log out the user
var authToken = getCookie("authToken");
function logOutUser() {
    document.querySelectorAll(`#log-out-icon`).forEach(entrie => {
        entrie.addEventListener(`click`, () => {
            // Set expiry to a past date, and include path and domain
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure";
            document.cookie = "showroomToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure";            
            document.cookie = "saved_car_params=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure";

            // Reload the page
            location.reload();
        });
    });
    document.querySelectorAll(`#log-out-text`).forEach(entrie => {
        entrie.addEventListener(`click`, () => {
            // Set expiry to a past date, and include path and domain
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure";
            document.cookie = "showroomToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure";
            document.cookie = "saved_car_params=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure";

            // Reload the page
            location.reload();
        });
    });
}

// Function to check the login status
function checkLoginStatus() {
    if (authToken) {
        // User is logged in
        console.log('User is logged in.');
        document.querySelectorAll(`#log-in-icon`).forEach(entrie => {
            entrie.style.display = `none`;
        });
        document.querySelectorAll(`#log-in-text`).forEach(entrie => {
            entrie.style.display = `none`;
        });
        document.querySelectorAll(`#log-out-icon`).forEach(entrie => {
            entrie.style.display = `inline`;
        });
        document.querySelectorAll(`#log-out-text`).forEach(entrie => {
            entrie.style.display = `inline`;
        });
        document.querySelectorAll(`#profile`).forEach(entrie => {
            entrie.style.display = `inline`;
        });
        logOutUser();
    } else {
        // User is not logged in
        console.log('User is not logged in.');
        document.querySelectorAll(`#log-in-icon`).forEach(entrie => {
            entrie.style.display = `inline`;
        });
        document.querySelectorAll(`#log-in-text`).forEach(entrie => {
            entrie.style.display = `inline`;
        });
        document.querySelectorAll(`#log-out-icon`).forEach(entrie => {
            entrie.style.display = `none`;
        });
        document.querySelectorAll(`#log-out-text`).forEach(entrie => {
            entrie.style.display = `none`;
        });
    }
}

// Function to get the value of a cookie by name
function getCookie(name) {
    var cookieValue = null;
    if (document.cookie && document.cookie !== '') {
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
            var cookie = jQuery.trim(cookies[i]);
            // Check if the cookie name matches
            if (cookie.substring(0, name.length + 1) === (name + '=')) {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
}

// Call the function when the page is loaded
$(document).ready(function () {
    checkLoginStatus();
});