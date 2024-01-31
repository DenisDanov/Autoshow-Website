function getCookieShowroom(name) {
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
        const [cookieName, cookieValue] = cookie.split('=').map(entry => entry.trim());
        if (cookieName === name) {
            // Replace _ with ;
            return decodeURIComponent(cookieValue.replace(/_/g, ';'));
        }
    }
    return null;
}

const autoshowCars = ['Lamborghini-Urus-2020.glb', 'BMW-X5.glb', 'Lamborghini-Aventador-2020.glb',
    'Lamborghini-Gallardo-2007.glb', 'Mclaren-P1-2015.glb', 'Porsche-Boxster-2016.glb',
    'Porsche-Carrera-2015.glb', `Toyota-Gr-Supra-2020.glb`, `Tesla-Model-3-2020.glb`];
const cookie = getCookieShowroom('showroomToken');

const indexCarParam = window.location.href.indexOf(`?`);
const pageUrl = window.location.href.substring(0, indexCarParam);

if (!checkIfCookieMatchesCar() && !autoshowCars.includes(carParam.split("3D Models/")[1]) &&
    pageUrl.includes(`showroom.html`)) {
    document.getElementById(`loader`).style.display = `none`;
    document.getElementById(`model-container`).style.display = `none`;
    document.querySelector(`.view-buttons`).style.display = `none`;
    document.querySelector(`#info-page`).style.display = `none`;
    document.getElementById(`unsubscribe-confirmation-2`).style.display = `block`;
} else if (!checkIfCookieMatchesCar() && !autoshowCars.includes(carParam.split("3D Models/")[1]) &&
    !pageUrl.includes(`showroom.html`)) {
    document.getElementById(`loader`).style.display = `none`;
    document.getElementById(`cars-data-wrapper`).style.display = `none`;
    document.getElementById(`unsubscribe-confirmation-2`).style.display = `block`;
}

function checkIfCookieMatchesCar() {
    if (cookie) {
        for (const car of cookie.split(",")) {
            if (car === carParam) {
                return true;
            }
        }
    }
    return false;
}