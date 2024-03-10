//Function to log out the user
var authToken = getCookie("authToken");
function logOutUser() {
    document.querySelectorAll(`#log-out-icon`).forEach(entrie => {
        entrie.addEventListener(`click`, () => {
            // Set expiry to a past date, and include path and domain
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            document.cookie = "recently_viewed=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

            // Reload the page
            location.reload();
        });
    });
    document.querySelectorAll(`#log-out-text`).forEach(entrie => {
        entrie.addEventListener(`click`, () => {
            // Set expiry to a past date, and include path and domain
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            document.cookie = "recently_viewed=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

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

// Call the function when the page is loaded
$(document).ready(function () {
    checkLoginStatus();
});