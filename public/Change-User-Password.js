let timeOut = setTimeout(function () {
    document.getElementById(`warning-fields`).style.display = 'none';
}, 4000);
document.getElementById(`change-password`).addEventListener(`click`, (e) => {
    const currentPassword = document.getElementById(`password`).value;
    const newPassword = document.getElementById(`new-password`).value;
    const newPasswordRepeat = document.getElementById(`repeat-new-password`).value;

    if (currentPassword === `` || newPassword === `` || newPasswordRepeat == ``) {
        document.getElementById(`warning-fields`).textContent = `Please fill out all password fields.`;
        document.getElementById(`warning-fields`).style.display = `block`;
        clearTimeout(timeOut);
        timeOut = setTimeout(function () {
            document.getElementById(`warning-fields`).style.display = 'none';
        }, 4000);
    } else if (newPassword !== newPasswordRepeat) {
        document.getElementById(`warning-fields`).textContent = `Passwords do not match.`;
        document.getElementById(`warning-fields`).style.display = `block`;
        clearTimeout(timeOut);
        timeOut = setTimeout(function () {
            document.getElementById(`warning-fields`).style.display = 'none';
        }, 4000);
    } else {

    }
});
