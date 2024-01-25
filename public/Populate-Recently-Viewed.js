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
const favVehiclesIdsHome = [];

if (authToken) {
    var decodedToken = JSON.parse(atob(authToken.split('.')[1]));
    var userId = decodedToken.userId;
}

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
                        showroomAccess(carCard.querySelector(`a`).href);
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
            if (authToken) {
                fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/profile/get`, {
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
                        const favVehiclesArr = result.favVehicles
                        for (const vehicle of favVehiclesArr) {
                            favVehiclesIdsHome.push(vehicle.vehicleId);
                        }

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
                            if (favVehiclesIdsHome.includes(carCard.querySelector(`a`).href)) {
                                showroomAccess(carCard.querySelector(`a`).href);
                                carCard.querySelector(`.add-fav input`).checked = true;
                                carCard.querySelector(`.favorites h3`).textContent = `Remove from Favorites`;
                            }
                            carCard.querySelector(`.add-fav input`).addEventListener(`change`, checkFavVehicles);
                            container.appendChild(carCard);
                        });
                        resolve("success");
                    }).catch(error => {
                        reject(error);
                    });
            } else {
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
                    carCard.querySelector(`.add-fav input`).addEventListener(`change`, checkFavVehicles);
                    container.appendChild(carCard);
                });
                resolve("success");
            }
        } else {
            document.querySelector(`.recently-viewed-cars`).appendChild(document.createElement(`p`));
            document.querySelector(`.recently-viewed-cars`).children[0].textContent = `No recently viewed cars`;
            document.querySelector(`.recently-viewed-cars`).children[0].style.margin = "1rem 0";
        }
    }

});

function checkFavVehicles(e) {
    if (e.currentTarget.checked && authToken) {
        const carId = e.currentTarget.parentNode.parentNode.parentNode.
            children[e.currentTarget.parentNode.parentNode.parentNode.children.length - 1].href;
        const carImg = e.currentTarget.parentNode.parentNode.parentNode.children[0]
            .children[0].getAttribute("src");
        const carName = e.currentTarget.parentNode.parentNode.parentNode.children[1].
            children[0].textContent;
        e.currentTarget.parentNode.parentNode.children[0].textContent = `Remove from Favorites`;
        showroomAccess(carId);
        favVehiclesIdsHome.push(carId);
        fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/favorites/add`, {
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
    } else if (authToken) {
        const carId = e.currentTarget.parentNode.parentNode.parentNode.
            children[e.currentTarget.parentNode.parentNode.parentNode.children.length - 1].href;
        e.currentTarget.parentNode.parentNode.children[0].textContent = `Add to Favorites`;
        fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/favorites/remove`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userId: userId,
                vehicleId: carId
            })
        })
            .then(response => {
                console.log(response);
                removeCarFromCookie(carId);
                favVehiclesIdsHome.splice(favVehiclesIdsHome.indexOf(carId), 1);
            })
            .catch(err => console.log(err));
    } else {
        document.getElementById(`overlay`).style.display = "flex";
        e.currentTarget.checked = false;
    }
}

