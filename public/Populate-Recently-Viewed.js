function getCarParamsFromCookie() {
    const savedParamsCookie = document.cookie.split('; ').find(cookie => cookie.startsWith('saved_car_params='));
    if (savedParamsCookie) {
        const paramsString = savedParamsCookie.split('=')[1];
        return paramsString ? paramsString.split(',') : [];
    }
    return [];
}

const carParams = getCarParamsFromCookie();
var currentURL = window.location.href;

var recentlyViewedLoaded = new Promise((resolve, reject) => {
    if (currentURL === "https://danov-autoshow-656625355b99.herokuapp.com/profile.html") {
        favoritesLoaded.then(response => {
            if (carParams.length !== 0) {
                const container = document.querySelector(`.recently-viewed-cars`);
                carParams.forEach(entrie => {
                    const carCard = document.createElement(`div`);
                    const modelName = entrie.split(`3D Models/`)[1];
                    carCard.classList.add(`car-card`);
                    carCard.innerHTML = `
                        <div class="img-container">
                                        <img src="images/${modelName.split(`.`)[0]}.png" alt="Car 2">
                                    </div>
                                    <div class="car-info">
                                        <h3>${modelName.split('.')[0].replace(/-/g, ' ')}</h3>
                                    </div>
                                    <div class="favorites">
                                    <h3>Add to Favorites</h3>
                                    <label class="add-fav">
                                        <input type="checkbox" />
                                        <i class="icon-heart fas fa-heart">
                                            <i class="icon-plus-sign fa-solid fa-plus"></i>
                                        </i>
                                    </label>
                                </div>
                                    <a href="showroom.html?car=${entrie}" class="view-button">View in
                                        Showroom</a>        
                        `;
                    if (favVehiclesIds.includes(carCard.querySelector(`a`).href)) {
                        carCard.querySelector(`.add-fav input`).checked = true;
                        carCard.querySelector(`.favorites h3`).textContent = `Remove from Favorites`;
                    }
                    carCard.querySelector(`.add-fav input`).addEventListener(`change`, addCarOrderToFavs);
                    container.appendChild(carCard);
                });
                resolve("success");
            } else {
                document.querySelector(`.recently-viewed-cars`).appendChild(document.createElement(`p`));
                document.querySelector(`.recently-viewed-cars`).children[0].textContent = `No recently viewed cars`;
                document.querySelector(`.recently-viewed-cars`).children[0].style.margin = "1rem 0";
            }
        });
    } else {
        if (carParams.length !== 0) {
            const container = document.querySelector(`.recently-viewed-cars`);
            carParams.forEach(entrie => {
                const carCard = document.createElement(`div`);
                const modelName = entrie.split(`3D Models/`)[1];
                carCard.classList.add(`car-card`);
                carCard.innerHTML = `
                        <div class="img-container">
                                        <img src="images/${modelName.split(`.`)[0]}.png" alt="Car 2">
                                    </div>
                                    <div class="car-info">
                                        <h3>${modelName.split('.')[0].replace(/-/g, ' ')}</h3>
                                    </div>
                                    <div class="favorites">
                                    <h3>Add to Favorites</h3>
                                    <label class="add-fav">
                                        <input type="checkbox" />
                                        <i class="icon-heart fas fa-heart">
                                            <i class="icon-plus-sign fa-solid fa-plus"></i>
                                        </i>
                                    </label>
                                </div>
                                    <a href="showroom.html?car=${entrie}" class="view-button">View in
                                        Showroom</a>        
                        `;
                container.appendChild(carCard);
            });
            resolve("success");
        } else {
            document.querySelector(`.recently-viewed-cars`).appendChild(document.createElement(`p`));
            document.querySelector(`.recently-viewed-cars`).children[0].textContent = `No recently viewed cars`;
            document.querySelector(`.recently-viewed-cars`).children[0].style.margin = "1rem 0";
        }
    }

});



