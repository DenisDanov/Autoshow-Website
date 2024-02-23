if (authToken) {
    var decodedToken = JSON.parse(atob(authToken.split('.')[1]));
    var userId = decodedToken.userId;
}

function getCarParams() {
    return new Promise((resolve, reject) => {
        if (authToken) {
            fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/recently-viewed/get?userId=${userId}&authToken=${authToken}`)
                .then(response => response.json())
                .then(result => {
                    if (result == null) {
                        resolve([]);
                    } else {
                        const arr = [];
                        for (const car of result) {
                            arr.push(car);
                        }
                        resolve(arr);
                    }
                })
                .catch(err => reject(err));
        } else {
            const savedParamsCookie = document.cookie.split('; ').find(cookie => cookie.startsWith('recently_viewed='));
            if (savedParamsCookie) {
                let paramsString = decodeURIComponent(savedParamsCookie.split('=')[1]).replace(/%2F/g, '/');
                paramsString = paramsString.replace(/\+/g, " ");
                resolve(paramsString ? paramsString.split(',') : []);
            }
            resolve([]);
        }
    })
}

var currentURL = window.location.href;
const favVehiclesIdsHome = [];

var recentlyViewedLoaded = new Promise((resolve, reject) => {
    getCarParams().then(carParams => {
        if (currentURL === "https://danov-autoshow-656625355b99.herokuapp.com/profile.html") {
            favoritesLoaded.then(response => {
                if (carParams.length !== 0) {
                    const container = document.querySelector(`.recently-viewed-cars`);
                    for (let index = carParams.length - 1; index >= 0; index--) {
                        let entrie = carParams[index];
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
                    }
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
                    fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/profile/get?id=${userId}&authToken=${authToken}`)
                        .then(response => response.json())
                        .then(result => {
                            const favVehiclesArr = result.favVehicles
                            if (favVehiclesArr !== null) {
                                for (const vehicle of favVehiclesArr) {
                                    favVehiclesIdsHome.push(vehicle.vehicleId);
                                }
                            }

                            const container = document.querySelector(`.recently-viewed-cars`);
                            for (let index = carParams.length - 1; index >= 0; index--) {
                                let entrie = carParams[index];
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
                                    carCard.querySelector(`.add-fav input`).checked = true;
                                    carCard.querySelector(`.favorites h3`).textContent = `Remove from Favorites`;
                                }
                                carCard.querySelector(`.add-fav input`).addEventListener(`change`, checkFavVehicles);
                                container.appendChild(carCard);
                            }
                            resolve("success");
                        }).catch(error => {
                            reject(error);
                        });
                } else {
                    const container = document.querySelector(`.recently-viewed-cars`);
                    for (let index = carParams.length - 1; index >= 0; index--) {
                        let entrie = carParams[index];
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
                    }
                    resolve("success");
                }
            } else {
                document.querySelector(`.recently-viewed-cars`).appendChild(document.createElement(`p`));
                document.querySelector(`.recently-viewed-cars`).children[0].textContent = `No recently viewed cars`;
                document.querySelector(`.recently-viewed-cars`).children[0].style.margin = "1rem 0";
            }
        }
    });
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
                vehicleName: carName,
                authToken: authToken
            })
        })
            .then(response => {
                console.log(response)
            })
            .catch(err => console.log(err));
    } else if (authToken) {
        const carId = e.currentTarget.parentNode.parentNode.parentNode.
            children[e.currentTarget.parentNode.parentNode.parentNode.children.length - 1].href;
        e.currentTarget.parentNode.parentNode.children[0].textContent = `Add to Favorites`;
        fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/favorites/remove`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userId: userId,
                vehicleId: carId,
                authToken: authToken
            })
        })
            .then(response => {
                console.log(response);
                removeCarFromCookie(carId);
                for (const vehicle of document.getElementById("favorite-vehicles").children) {
                    if (vehicle.querySelector("a").href === carId) {
                        vehicle.remove();
                        break;
                    }
                }
                favVehiclesIdsHome.splice(favVehiclesIdsHome.indexOf(carId), 1);
            })
            .catch(err => console.log(err));
    } else {
        document.getElementById(`overlay`).style.display = "flex";
        e.currentTarget.checked = false;
    }
}

