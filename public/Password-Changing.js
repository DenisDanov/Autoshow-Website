document.getElementById(`password`).addEventListener(`input`, displayIcons);
document.getElementById(`new-password`).addEventListener(`input`, displayIcons);
document.getElementById(`repeat-new-password`).addEventListener(`input`, displayIcons);

function displayIcons(e) {
    if (e.currentTarget.id === `password`) {
        document.getElementById(`password-eye`).style.display = `block`;

        document.getElementById(`new-password-eye`).style.display = `none`;
        document.getElementById(`new-password-eye`).classList.remove(`fa-eye-slash`);
        document.getElementById(`new-password-eye`).classList.add(`fa-eye`);

        document.getElementById(`new-password-repeat-eye`).style.display = `none`;
        document.getElementById(`new-password-repeat-eye`).classList.remove(`fa-eye-slash`);
        document.getElementById(`new-password-repeat-eye`).classList.add(`fa-eye`);

        document.getElementById(`repeat-new-password`).type = `password`;
        document.getElementById(`new-password`).type = `password`;
    } else if (e.currentTarget.id === `new-password`) {
        document.getElementById(`new-password-eye`).style.display = `block`;

        document.getElementById(`password-eye`).style.display = `none`;
        document.getElementById(`password-eye`).classList.remove(`fa-eye-slash`);
        document.getElementById(`password-eye`).classList.add(`fa-eye`);

        document.getElementById(`new-password-repeat-eye`).style.display = `none`;
        document.getElementById(`new-password-repeat-eye`).classList.remove(`fa-eye-slash`);
        document.getElementById(`new-password-repeat-eye`).classList.add(`fa-eye`);

        document.getElementById(`password`).type = `password`;
        document.getElementById(`repeat-new-password`).type = `password`;
    } else if (e.currentTarget.id === `repeat-new-password`) {
        document.getElementById(`new-password-repeat-eye`).style.display = `block`;

        document.getElementById(`password-eye`).style.display = `none`;
        document.getElementById(`password-eye`).classList.remove(`fa-eye-slash`);
        document.getElementById(`password-eye`).classList.add(`fa-eye`);

        document.getElementById(`new-password-eye`).style.display = `none`;
        document.getElementById(`new-password-eye`).classList.remove(`fa-eye-slash`);
        document.getElementById(`new-password-eye`).classList.add(`fa-eye`);

        document.getElementById(`new-password`).type = `password`;
        document.getElementById(`password`).type = `password`;
    }
}