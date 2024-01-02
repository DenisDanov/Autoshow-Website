 //Function to log out the user
 function logOutUser() {
    document.querySelectorAll(`#log-out-icon`).forEach(entrie => {
        entrie.addEventListener(`click`, () => {
            // Set expiry to a past date, and include path and domain
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=danovs-autoshow-afcbab0f302b.herokuapp.com; secure";

            // Reload the page
            location.reload();
        });
    });
    document.querySelectorAll(`#log-out-text`).forEach(entrie => {
        entrie.addEventListener(`click`, () => {
            // Set expiry to a past date, and include path and domain
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=danovs-autoshow-afcbab0f302b.herokuapp.com; secure";

            // Reload the page
            location.reload();
        });
    });
}

// Function to check the login status
function checkLoginStatus() {
    // Retrieve the auth token from cookies
    var authToken = getCookie("authToken");

    if (authToken) {
        // User is logged in
        var decodedToken = JSON.parse(atob(authToken.split('.')[1]));
        var userId = decodedToken.userId;

        fetch(`https://danovs-autoshow-afcbab0f302b.herokuapp.com/api/favorites/get`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userId: userId
            })
        })
            .then(response => response.json())
            .then(result => {
                for (let vehicle of result) {
                    document.querySelectorAll(`.car-card a`).forEach(entrie => {
                        if (entrie.href === vehicle.vehicleId) {
                            entrie.parentNode.children[2].children[0].textContent = `Remove from Favorites`;
                            entrie.parentNode.children[2].children[1].children[0].checked = true;
                            entrie.parentNode.children[2].children[1].children[0].classList.add(`checked`);
                        }
                    });
                }
            })
            .catch(err => console.log(err));

        document.querySelectorAll(`input[type="checkbox"]`).forEach(entrie => {
            entrie.addEventListener("change", (e) => {
                if (e.currentTarget.checked && !e.currentTarget.classList.contains(`checked`)) {
                    e.currentTarget.classList.add(`checked`);
                    const carId = e.currentTarget.parentNode.parentNode.parentNode.
                        children[e.currentTarget.parentNode.parentNode.parentNode.children.length - 1].href;
                    const carImg = e.currentTarget.parentNode.parentNode.parentNode.children[0]
                        .children[0].getAttribute("src");
                    const carName = e.currentTarget.parentNode.parentNode.parentNode.children[1].
                        children[0].textContent;
                    e.currentTarget.parentNode.parentNode.children[0].textContent = `Remove from Favorites`;
                    fetch(`https://danovs-autoshow-afcbab0f302b.herokuapp.com/api/favorites/add`, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            userId: userId,
                            vehicleId: carId,
                            vehicleImg: carImg,
                            vehicleName: carName
                        })
                    })
                        .then(response => console.log(response))
                        .catch(err => console.log(err));
                } else if (e.currentTarget.classList.contains(`checked`)) {
                    e.currentTarget.classList.remove(`checked`);
                    e.currentTarget.parentNode.parentNode.children[0].textContent = `Add to Favorites`;
                    const carId = e.currentTarget.parentNode.parentNode.parentNode.
                        children[e.currentTarget.parentNode.parentNode.parentNode.children.length - 1].href;
                    fetch(`https://danovs-autoshow-afcbab0f302b.herokuapp.com/api/favorites/remove`, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            userId: userId,
                            vehicleId: carId
                        })
                    })
                        .then(response => console.log(response))
                        .catch(err => console.log(err));
                }
            });
        });
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
        document.querySelectorAll(`input[type="checkbox"]`).forEach(entrie => {
            entrie.addEventListener(`click`, () => {
                entrie.checked = false;
                document.getElementById('overlay').style.display = 'flex';
            });
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