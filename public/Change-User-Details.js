let unchangedUsername;
let unchangedEmail;
setTimeout(function () {
    unchangedUsername = document.getElementById(`username`).value;
    unchangedEmail = document.getElementById(`email`).value;
}, 1000);

var authToken = getCookie("authToken");
var decodedToken = JSON.parse(atob(authToken.split('.')[1]));
var userId = decodedToken.userId;

document.getElementById(`username`).addEventListener(`input`, (e) => {
    document.getElementById(`change-username`).style.display = "block";
    document.getElementById(`cancel`).style.display = "block";
    document.getElementById(`username-container`).style.width = "125%";

    document.getElementById(`change-username`).addEventListener(`click`, changeUserName);
    document.getElementById(`cancel`).addEventListener(`click`, cancel);

    if (document.getElementById(`username`).value === ``) {
        document.getElementById(`change-username`).style.display = `none`;
        document.getElementById(`cancel`).style.display = `none`;
        document.getElementById(`username-container`).style.width = "100%";
    }
});

function cancel(e) {
    e.currentTarget.style.display = `none`;
    e.currentTarget.parentNode.children[1].style.display = `none`;
    e.currentTarget.parentNode.style.width = "100%";
    if (e.currentTarget.parentNode.children[0].id === `username`) {
        e.currentTarget.parentNode.children[0].value = unchangedUsername;
    } else if (e.currentTarget.parentNode.children[0].id === `email`) {
        e.currentTarget.parentNode.children[0].value = unchangedEmail;
    } else {

    }
}

function changeUserName(e) {
    document.getElementById(`overlay-changeUsername`).style.display = `flex`;
    document.getElementById(`popup-changeUsername`).style.display = `flex`;
    document.getElementById(`popup-changeUsername`).style.flexDirection = `column`;
    document.getElementById(`remove-btn-changeUsername`).addEventListener(`click`, requestChangeUsername);
}

function requestChangeUsername(e) {
    fetch(`https://danovs-autoshow-afcbab0f302b.herokuapp.com/api/profile/changeUsername`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            userId: userId,
            username: document.getElementById(`username`).value,
            password: document.querySelector(`#password-overlay`).value
        })
    })
        .then(response => response.json())
        .then(result => {
            console.log(result.result);
            if (result.result == `Successfully changed the username.`) {
                document.getElementById(`warning-fields-username`).style.backgroundColor = `green`;
                document.getElementById(`warning-fields-username`).style.border = "3px solid green";
                document.getElementById(`warning-fields-username`).textContent = result.result;
                unchangedUsername = document.getElementById(`username`).value;
                setTimeout(function () {
                    document.getElementById(`cancel`).click();
                    document.getElementById(`overlay-changeUsername`).style.display = `none`;
                    document.querySelector(`#password-overlay`).value = ``;
                }, 1500);
            } else {
                document.getElementById(`warning-fields-username`).style.backgroundColor = `red`;
                document.getElementById(`warning-fields-username`).style.border = "3px solid red";
                document.getElementById(`warning-fields-username`).textContent = result.result;
            }
        })
}

function closePopupChangeUsername(e) {
    document.getElementById(`overlay-changeUsername`).style.display = `none`;
    document.querySelector(`#password-overlay`).value = ``;
    document.getElementById(`cancel`).click();
}

document.getElementById(`email`).addEventListener(`input`, (e) => {
    document.getElementById(`change-email`).style.display = "block";
    document.getElementById(`cancel-email`).style.display = "block";
    document.getElementById(`email-container`).style.width = "125%";

    document.getElementById(`change-email`).addEventListener(`click`, changeEmail);
    document.getElementById(`cancel-email`).addEventListener(`click`, cancel);

    if (document.getElementById(`email`).value === ``) {
        document.getElementById(`change-email`).style.display = `none`;
        document.getElementById(`cancel-email`).style.display = `none`;
        document.getElementById(`email-container`).style.width = "100%";
    }
});

function changeEmail(e) {

}

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
