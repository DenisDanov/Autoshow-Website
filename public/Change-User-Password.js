let timeOut = setTimeout(function () {
    document.getElementById(`warning-fields`).style.display = 'none';
}, 4000);

var authToken = getCookie("authToken");
var decodedToken = JSON.parse(atob(authToken.split('.')[1]));
var userId = decodedToken.userId;

document.getElementById(`change-password`).addEventListener(`click`, (e) => {
    const currentPassword = document.getElementById(`password`).value;
    const newPassword = document.getElementById(`new-password`).value;
    const newPasswordRepeat = document.getElementById(`repeat-new-password`).value;

    if (currentPassword === `` || newPassword === `` || newPasswordRepeat == ``) {
        document.getElementById(`warning-fields`).textContent = `Please fill out all password fields.`;
        document.getElementById(`warning-fields`).style.display = `block`;
        document.getElementById(`warning-fields`).style.backgroundColor = `red`;
        document.getElementById(`warning-fields`).style.border = "3px solid red";

        document.querySelector("#new-password-eye").style.display = `none`;
        document.querySelector("#password-eye").style.display = `none`;
        document.querySelector("#new-password-repeat-eye").style.display = `none`;
        clearTimeout(timeOut);
        timeOut = setTimeout(function () {
            document.getElementById(`warning-fields`).style.display = 'none';
        }, 3500);
    } else if (newPassword !== newPasswordRepeat) {
        document.getElementById(`warning-fields`).style.backgroundColor = `red`;
        document.getElementById(`warning-fields`).style.border = "3px solid red";
        document.getElementById(`warning-fields`).textContent = `Passwords do not match.`;
        document.getElementById(`warning-fields`).style.display = `block`;

        document.querySelector("#new-password-eye").style.display = `none`;
        document.querySelector("#password-eye").style.display = `none`;
        document.querySelector("#new-password-repeat-eye").style.display = `none`;
        clearTimeout(timeOut);
        timeOut = setTimeout(function () {
            document.getElementById(`warning-fields`).style.display = 'none';
        }, 3500);
    } else {
        fetch(`https://danovs-autoshow-afcbab0f302b.herokuapp.com/api/profile/changePassword`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                id: userId,
                currentPassword: document.getElementById(`password`).value,
                newPassword: document.querySelector(`#new-password`).value
            })
        })
            .then(response => response.json())
            .then(result => {
                console.log(result.result);
                if (result.result == `Successfully changed the password.`) {
                    document.getElementById(`password`).value = ``;
                    document.getElementById(`new-password`).value = ``;
                    document.getElementById(`repeat-new-password`).value = ``;
                    document.getElementById(`warning-fields`).style.display = `block`;
                    document.getElementById(`warning-fields`).style.backgroundColor = `green`;
                    document.getElementById(`warning-fields`).style.border = "3px solid green";
                    document.getElementById(`warning-fields`).textContent = result.result;

                    document.querySelector("#new-password-eye").style.display = `none`;
                    document.querySelector("#password-eye").style.display = `none`;
                    document.querySelector("#new-password-repeat-eye").style.display = `none`;
                    clearTimeout(timeOut);
                    timeOut = setTimeout(function () {
                        document.getElementById(`warning-fields`).style.display = 'none';
                    }, 3500);
                } else {
                    document.getElementById(`warning-fields`).style.display = `block`;
                    document.getElementById(`warning-fields`).style.backgroundColor = `red`;
                    document.getElementById(`warning-fields`).style.border = "3px solid red";
                    document.getElementById(`warning-fields`).textContent = result.result;

                    document.querySelector("#new-password-eye").style.display = `none`;
                    document.querySelector("#password-eye").style.display = `none`;
                    document.querySelector("#new-password-repeat-eye").style.display = `none`;
                    clearTimeout(timeOut);
                    timeOut = setTimeout(function () {
                        document.getElementById(`warning-fields`).style.display = 'none';
                    }, 3500);
                }
            })
    }
});

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