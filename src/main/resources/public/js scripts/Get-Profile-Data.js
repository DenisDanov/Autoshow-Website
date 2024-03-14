var authToken = getCookie("authToken");
const favVehiclesIds = [];
var carOrderBtnEvent = false;
let funcReference;

if (authToken) {
    // User is logged in
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

    var decodedToken = JSON.parse(atob(authToken.split('.')[1]));
    var userId = decodedToken.userId;

    var favoritesLoaded = new Promise((resolve, reject) => {
        const favVehiclesArr = document.querySelectorAll("#favorite-vehicles .car-card");
        if (favVehiclesArr !== null) {
            for (const vehicle of favVehiclesArr) {
                favVehiclesIds.push(vehicle.querySelector
                ("a").href.split(`car=`)[1].replaceAll(`%20`, ` `));
                vehicle.querySelector(".favorites .add-fav input").addEventListener(`change`, removeFavVehicle);
            }
            document.getElementById(`remove-btn`).addEventListener(`click`, removeTheCar);
        }
        resolve();
    });

    favoritesLoaded.then(result => {
        for (const container of document.querySelectorAll(`#car-orders li`)) {
            container.querySelector(`#change-order`).addEventListener(`click`, (e) => {
                modifyOrderFunc(e, container.querySelector(`.car-manufacturer`).textContent,
                    container.querySelector(`.car-model`).textContent,
                    container.querySelector(`.car-year`).textContent);
            });
            if (container.querySelector(`.order-status`).textContent === `Completed`) {
                container.querySelector(`.car-order-model .car-card .favorites input`).addEventListener(`change`, addCarOrderToFavs);
            }
            container.querySelector(`#modify-order`).addEventListener(`click`, (e) => {
                modifyOrderFunc(e, container.querySelector(`.car-manufacturer`).textContent,
                    container.querySelector(`.car-model`).textContent,
                    container.querySelector(`.car-year`).textContent);
            });
            container.querySelector(`#cancel-order`).addEventListener(`click`, removeCarOrder);
        }
    });
}

let timeOutMsgs;

function remakeOrder(reorderCar, e, carManufacturer, carModel, carYear) {
    if (!carOrderBtnEvent) {
        const newManufacturer = e.currentTarget.parentNode.parentNode.querySelector(`#car-manufacturer`)
            .options[e.currentTarget.parentNode.parentNode.querySelector(`#car-manufacturer`).selectedIndex].textContent;
        const newModel = e.currentTarget.parentNode.parentNode.querySelector(`#car-model`).value;
        const newYear = e.currentTarget.parentNode.parentNode.querySelector(`#car-year`).value;
        let modifyReference = document.querySelector('[modify-reference="true"]');
        const resultHtmlEle = document.getElementById(`response-result`);
        carManufacturer = modifyReference.parentNode.parentNode.children[0].children[0].children[1].textContent;
        carModel = modifyReference.parentNode.parentNode.children[0].children[1].children[1].textContent;
        carYear = modifyReference.parentNode.parentNode.children[0].children[2].children[1].textContent;
        if (modifyReference.id === `change-order`) {
            let checkForSameOrder = false;
            if (carManufacturer === newManufacturer) {
                if (carModel === newModel) {
                    if (carYear === newYear) {
                        checkForSameOrder = true;
                    }
                }
            }
            if (!checkForSameOrder && newManufacturer !== "loading" && newModel !== "loading" && newYear !== "loading") {
                fetch(`${window.location.origin}/api/carOrders/modify`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        id: userId,
                        currentManufacturer: carManufacturer,
                        currentModel: carModel,
                        currentYear: carYear,
                        newManufacturer: newManufacturer,
                        newModel: newModel,
                        newYear: newYear,
                        authToken: authToken
                    })
                })
                    .then(response => response.json())
                    .then(result => {
                        if (result.result === `New order is made and the period is extended.`) {
                            resultHtmlEle.textContent = result.result;
                            resultHtmlEle.style.display = `block`;
                            resultHtmlEle.style.backgroundColor = `green`;
                            resultHtmlEle.style.color = `white`;
                            resultHtmlEle.style.border = `5px solid green`;
                            resultHtmlEle.style.borderRadius = `5px`;
                            document.getElementById(`reorder-car`).disabled = true;
                            let carManufacturer = result.carManufacturer;
                            carManufacturer = carManufacturer.charAt(0).toUpperCase() + carManufacturer.substring(1);
                            modifyReference.parentNode.parentNode.parentNode.innerHTML = `
                        <div class="car-orders-container">
                    <div class="car-order-details">
                        <div>
                            <span>Car manufacturer</span>
                            <p>${carManufacturer}</p>
                        </div>
                        <div>
                            <span>Car model</span>
                            <p>${result.carModel}</p>
                        </div>
                        <div>
                            <span>Manufacture year</span>
                            <p>${result.carYear}</p>
                        </div>
                    </div>
                    <div class="car-order-status">
                        <div>
                            <span>Order status</span>
                            <p class="order-status" status="${result.orderStatus}">${result.orderStatus}</p>
                        </div>
                        <div>
                            <span>Order date</span>
                            <p>${result.dateOfOrder}</p>
                        </div>
                    </div>
                    <div class="car-order-model" style="display: none">
                        <h1>Ordered car</h1>
                        <div class="car-card">
                            <div class="img-container">
                                <img src="../images/${carManufacturer}-${result.carModel}-${result.carYear}.png" alt="Car 2">
                            </div>
                            <div class="car-info">
                                <h3>${result.carYear} ${result.carManufacturer.toUpperCase()} ${result.carModel.toUpperCase()}</h3>
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
                            <a href="showroom.html?car=3D Models/${carManufacturer}-${result.carModel}-${result.carYear}.glb"
                                class="view-button">View in
                                Showroom</a>
                        </div>
                    </div>
                    <div id="cancel-order-container">
                    <button id="change-order">Change Order</button>
                    <button id="cancel-order">Cancel Order</button>
                    <button id="modify-order" modify-reference="true">Remake order</button>
                </div>
                </div>
                        `
                            modifyReference = document.querySelector('[modify-reference="true"]');
                            if (result.orderStatus === `Completed`) {
                                modifyReference.parentNode.parentNode.parentNode.
                                querySelector(`.car-order-model  .car-card .favorites .add-fav input`).addEventListener(`change`,addCarOrderToFavs);
                                if (favVehiclesIds.includes(modifyReference.parentNode.parentNode.parentNode.
                                querySelector(`.car-order-model  .car-card a`).href.split(`car=`)[1].replaceAll(`%20`,` `))) {
                                    modifyReference.parentNode.parentNode.parentNode.
                                    querySelector(`.car-order-model  .car-card .favorites .add-fav input`).checked = `true`;
                                    modifyReference.parentNode.parentNode.parentNode.
                                    querySelector(`.car-order-model  .car-card .favorites h3`).textContent = `Remove from Favorites`;
                                }
                                modifyReference.parentNode.parentNode.parentNode.querySelector(`.car-order-model`).style.display = `flex`;
                                modifyReference.parentNode.parentNode.parentNode.querySelector(`#change-order`).style.display = `none`;
                            } else {
                                modifyReference.parentNode.parentNode.parentNode.querySelector(`#change-order`).addEventListener(`click`, (e) => {
                                    modifyOrderFunc(e, carManufacturer, result.carModel, result.carYear);
                                });
                            }
                            modifyReference.parentNode.parentNode.parentNode.querySelector(`#cancel-order`).addEventListener(`click`, removeCarOrder);
                            modifyReference.parentNode.parentNode.parentNode.querySelector('[modify-reference="true"]').remove();
                            modifyReference.removeAttribute(`modify-reference`);
                            modifyReference.style.display = `none`;
                            removeReorderCarClickListener(reorderCar);
                            timeOutMsgs = setTimeout(function () {
                                document.getElementById(`response-result`).style.display = `none`;
                                document.getElementById('order-car-menu').style.display = `none`;
                                document.getElementById(`reorder-car`).disabled = false;
                            }, 1700);
                        } else {
                            resultHtmlEle.textContent = result.result;
                            resultHtmlEle.style.display = `block`;
                            resultHtmlEle.style.backgroundColor = `red`;
                            resultHtmlEle.style.color = `white`;
                            resultHtmlEle.style.border = `5px solid red`;
                            resultHtmlEle.style.borderRadius = `5px`;
                            document.getElementById(`reorder-car`).disabled = true;
                            timeOutMsgs = setTimeout(function () {
                                document.getElementById(`response-result`).style.display = `none`;
                                document.getElementById(`reorder-car`).disabled = false;
                            }, 2200);
                        }
                    })
                    .catch(err => console.log(err));
            } else {
                if (newManufacturer !== "loading" && newModel !== "loading" && newYear !== "loading") {
                    resultHtmlEle.textContent = `Please order different model than the current one.`;
                } else {
                    resultHtmlEle.textContent = `Please wait for the car queries to load.`;
                }
                resultHtmlEle.style.display = `block`;
                resultHtmlEle.style.backgroundColor = `red`;
                resultHtmlEle.style.color = `white`;
                resultHtmlEle.style.border = `5px solid red`;
                resultHtmlEle.style.borderRadius = `5px`;
                timeOutMsgs = setTimeout(function () {
                    document.getElementById(`response-result`).style.display = `none`;
                }, 3000);
            }
        } else if (modifyReference.id !== `change-order`) {
            if (newManufacturer == "loading" && newModel == "loading" && newYear == "loading") {
                resultHtmlEle.textContent = "Please wait for the car queries to laod.";
                resultHtmlEle.style.display = `block`;
                resultHtmlEle.style.backgroundColor = `red`;
                resultHtmlEle.style.color = `white`;
                resultHtmlEle.style.border = `5px solid red`;
                resultHtmlEle.style.borderRadius = `5px`;
                timeOutMsgs = setTimeout(function () {
                    document.getElementById(`response-result`).style.display = `none`;
                }, 2200);
            } else {
                fetch(`${window.location.origin}/api/carOrders/modify`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        id: userId,
                        currentManufacturer: carManufacturer,
                        currentModel: carModel,
                        currentYear: carYear,
                        newManufacturer: newManufacturer,
                        newModel: newModel,
                        newYear: newYear,
                        authToken: authToken
                    })
                })
                    .then(response => response.json())
                    .then(result => {
                        if (result.result === `Same order is made and the period is extended.` ||
                            result.result === `New order is made and the period is extended.`) {
                            resultHtmlEle.textContent = result.result;
                            resultHtmlEle.style.display = `block`;
                            resultHtmlEle.style.backgroundColor = `green`;
                            resultHtmlEle.style.color = `white`;
                            resultHtmlEle.style.border = `5px solid green`;
                            resultHtmlEle.style.borderRadius = `5px`;
                            document.getElementById(`reorder-car`).disabled = true;
                            let carManufacturer = result.carManufacturer;
                            carManufacturer = carManufacturer.charAt(0).toUpperCase() + carManufacturer.substring(1);
                            modifyReference.parentNode.parentNode.parentNode.innerHTML = `
                    <div class="car-orders-container">
                <div class="car-order-details">
                    <div>
                        <span>Car manufacturer</span>
                        <p>${carManufacturer}</p>
                    </div>
                    <div>
                        <span>Car model</span>
                        <p>${result.carModel}</p>
                    </div>
                    <div>
                        <span>Manufacture year</span>
                        <p>${result.carYear}</p>
                    </div>
                </div>
                <div class="car-order-status">
                    <div>
                        <span>Order status</span>
                        <p class="order-status" status="${result.orderStatus}">${result.orderStatus}</p>
                    </div>
                    <div>
                        <span>Order date</span>
                        <p>${result.dateOfOrder}</p>
                    </div>
                </div>
                <div class="car-order-model" style="display: none;">
                    <h1>Ordered car</h1>
                    <div class="car-card">
                        <div class="img-container">
                            <img src="../images/${carManufacturer}-${result.carModel}-${result.carYear}.png" alt="Car 2">
                        </div>
                        <div class="car-info">
                            <h3>${result.carYear} ${result.carManufacturer.toUpperCase()} ${result.carModel.toUpperCase()}</h3>
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
                        <a href="showroom.html?car=3D Models/${carManufacturer}-${result.carModel}-${result.carYear}.glb"
                            class="view-button">View in
                            Showroom</a>
                    </div>
                </div>
                <div id="cancel-order-container">
                <button id="change-order">Change Order</button>
                <button id="cancel-order">Cancel Order</button>
                <button id="modify-order" modify-reference="true">Remake order</button>
            </div>
            </div>
                    `
                            modifyReference = document.querySelector('[modify-reference="true"]');
                            if (result.orderStatus === `Completed`) {
                                modifyReference.parentNode.parentNode.parentNode.
                                querySelector(`.car-order-model  .car-card .favorites .add-fav input`).addEventListener(`change`,addCarOrderToFavs);
                                if (favVehiclesIds.includes(modifyReference.parentNode.parentNode.parentNode.
                                querySelector(`.car-order-model  .car-card a`).href.split(`car=`)[1].replaceAll(`%20`,` `))) {
                                    modifyReference.parentNode.parentNode.parentNode.
                                    querySelector(`.car-order-model  .car-card .favorites .add-fav input`).checked = `true`;
                                    modifyReference.parentNode.parentNode.parentNode.
                                    querySelector(`.car-order-model  .car-card .favorites h3`).textContent = `Remove from Favorites`;
                                }
                                modifyReference.parentNode.parentNode.parentNode.querySelector(`.car-order-model`).style.display = `flex`;
                                modifyReference.parentNode.parentNode.parentNode.querySelector(`#change-order`).style.display = `none`;
                            } else {
                                modifyReference.parentNode.parentNode.parentNode.querySelector(`#change-order`).addEventListener(`click`, (e) => {
                                    modifyOrderFunc(e, carManufacturer, result.carModel, result.carYear);
                                });
                            }
                            modifyReference.parentNode.parentNode.parentNode.querySelector(`#cancel-order`).addEventListener(`click`, removeCarOrder);
                            modifyReference.parentNode.parentNode.parentNode.querySelector('[modify-reference="true"]').remove();
                            modifyReference.removeAttribute(`modify-reference`);
                            modifyReference.style.display = `none`;
                            removeReorderCarClickListener(reorderCar);
                            timeOutMsgs = setTimeout(function () {
                                document.getElementById(`response-result`).style.display = `none`;
                                document.getElementById('order-car-menu').style.display = `none`;
                                document.getElementById(`reorder-car`).disabled = false;
                            }, 1700);
                        } else {
                            resultHtmlEle.textContent = result.result;
                            resultHtmlEle.style.display = `block`;
                            resultHtmlEle.style.backgroundColor = `red`;
                            resultHtmlEle.style.color = `white`;
                            resultHtmlEle.style.border = `5px solid red`;
                            resultHtmlEle.style.borderRadius = `5px`;
                            timeOutMsgs = setTimeout(function () {
                                document.getElementById(`response-result`).style.display = `none`;
                            }, 2200);
                        }
                    })
                    .catch(err => console.log(err));
            }
        }
    } else {
        carOrderBtnEvent = false;
    }
}

function addCarOrderToFavs(e) {
    if (e.currentTarget.checked) {
        let carId = e.currentTarget.parentNode.parentNode.parentNode.children[e.currentTarget.parentNode.parentNode.parentNode.children.length - 1].href;
        carId = carId.substring(carId.lastIndexOf(`car=`), carId.length).replaceAll(`%20`, ` `).split(`car=`)[1];
        const carImg = e.currentTarget.parentNode.parentNode.parentNode.children[0]
            .children[0].getAttribute("src");
        const carName = e.currentTarget.parentNode.parentNode.parentNode.children[1].children[0].textContent;
        e.currentTarget.parentNode.parentNode.children[0].textContent = `Remove from Favorites`;
        document.querySelectorAll(`#car-orders .car-card a`).forEach(entrie => {
            if (entrie.href.replaceAll(`%20`, ` `).includes(carId)) {
                entrie.parentNode.querySelector(`.favorites .add-fav input`).checked = true;
                entrie.parentNode.querySelector(`.favorites h3`).textContent = `Remove from Favorites`;
                entrie.parentNode.querySelector(`.favorites .add-fav i`).style.color = "orange";
            }
        });
        document.querySelectorAll(`.recently-viewed-cars .car-card a`).forEach(entrie => {
            if (entrie.href.replaceAll(`%20`, ` `).includes(carId)) {
                entrie.parentNode.querySelector(`.favorites .add-fav input`).checked = true;
                entrie.parentNode.querySelector(`.favorites h3`).textContent = `Remove from Favorites`;
                entrie.parentNode.querySelector(`.favorites .add-fav i`).style.color = "orange";
            }
        });
        const favVehiclesContainer = document.createElement(`li`);
        favVehiclesContainer.innerHTML = `
    <div class="car-card">
    <div class="img-container">
        <img src="${carImg}" alt="Car 2">
    </div>
    <div class="car-info">
        <h3>${carName}</h3>
    </div>
    <div class="favorites">
        <h3>Remove from Favorites</h3>
        <label class="add-fav">
            <input type="checkbox" />
            <i class="icon-heart fas fa-heart">
                <i class="icon-plus-sign fa-solid fa-plus"></i>
            </i>
        </label>
    </div>
    <a href="showroom.html?car=${carId}" class="view-button">View in
        Showroom</a>
</div>
    `
        favVehiclesIds.push(carId);
        document.getElementById(`favorite-vehicles`).appendChild(favVehiclesContainer);
        favVehiclesContainer.children[0].children[2].children[1].children[0].checked = true;
        favVehiclesContainer.children[0].children[2].children[1].children[0].addEventListener(`change`, removeFavVehicle);
        document.getElementById(`remove-btn`).addEventListener(`click`, removeTheCar);
        fetch(`${window.location.origin}/api/favorites/add`, {
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
            .then(response => console.log(response))
            .catch(err => console.log(err));
    } else {
        let carId = e.currentTarget.parentNode.parentNode.parentNode.children[e.currentTarget.parentNode.parentNode.parentNode.children.length - 1].href;
        const favVehicles = document.getElementById(`favorite-vehicles`).querySelectorAll(`li .car-card a`);
        for (const vehicle of favVehicles) {
            if (vehicle.href.includes(carId)) {
                vehicle.parentNode.parentNode.remove();
                break;
            }
        }
        e.currentTarget.parentNode.parentNode.children[0].textContent = `Add to Favorites`;
        fetch(`${window.location.origin}/api/favorites/remove`, {
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
                document.querySelectorAll(`.recently-viewed-cars .car-card a`).forEach(entrie => {
                    if (entrie.href === carId) {
                        entrie.parentNode.querySelector(`.favorites .add-fav input`).checked = false;
                        entrie.parentNode.querySelector(`.favorites h3`).textContent = `Add to Favorites`;
                        entrie.parentNode.querySelector(`.favorites .add-fav i`).style.color = "#666";
                    }
                });
                document.querySelectorAll(`#car-orders .car-card a`).forEach(entrie => {
                    if (entrie.href === carId) {
                        entrie.parentNode.querySelector(`.favorites .add-fav input`).checked = false;
                        entrie.parentNode.querySelector(`.favorites h3`).textContent = `Add to Favorites`;
                        entrie.parentNode.querySelector(`.favorites .add-fav i`).style.color = "#666";
                    }
                });
                carId = carId.substring(carId.lastIndexOf(`car=`), carId.length).replaceAll(`%20`, ` `).split(`car=`)[1];
                favVehiclesIds.splice(favVehiclesIds.indexOf(carId), 1);
            })
            .catch(err => console.log(err));
    }
}

function modifyOrderFunc(modifyReference, carManufacturer, carModel, carYear) {
    document.querySelectorAll(`[modify-reference="true"]`).forEach(entrie => {
        entrie.removeAttribute(`modify-reference`);
    });
    if (modifyReference.currentTarget.id === `change-order`) {
        document.getElementById(`order-car-menu-header`).textContent = `Please change your order here.`;
    } else {
        document.getElementById(`order-car-menu-header`).textContent = `Reorder the same car, or choose a different one.`;
    }
    modifyReference.currentTarget.setAttribute(`modify-reference`, "true");
    var carManufacturerEle = document.getElementById('car-manufacturer');
    var carModelEle = document.getElementById('car-model');
    var carYearEle = document.getElementById('car-year');

    const optionToSelect = Array.from(carManufacturerEle.children).find(
        ele => ele.textContent.toLowerCase() === carManufacturer.toLowerCase());
    if (optionToSelect) {
        optionToSelect.selected = true;
        populateModels().then(result => {
            const optionToSelectModel = Array.from(carModelEle.children).find(
                ele => ele.textContent.toLowerCase() === carModel.toLowerCase()
            );
            if (optionToSelectModel) {
                optionToSelectModel.selected = true;
                populateDataYears().then(result => {
                    const optionToSelectYear = Array.from(carYearEle.children).find(
                        ele => ele.textContent === carYear
                    );
                    if (optionToSelectYear) {
                        optionToSelectYear.selected = true;
                    }
                }).catch(err => console.log(err));
            }
        }).catch(err => {
            console.error(err);
        });
    }
    document.getElementById('order-car-menu').style.display = `flex`;

    document.getElementById('reorder-car').addEventListener('click', function reorderCar(e) {
        funcReference = reorderCar;
        if (!carOrderBtnEvent) {
            reorderCarClickListener(reorderCar, e, carManufacturer, carModel, carYear);
        } else {
            carOrderBtnEvent = false;
        }
    });

    document.getElementById(`cancel-order-icon`).addEventListener(`click`, function cancelButtons(e) {
        carOrderBtnEvent = true;
        document.getElementById('reorder-car').click();
        carOrderBtnEvent = false;
        cancelRemakeOrder(funcReference, cancelButtons);
    });
    document.getElementById(`cancel-orderbtn`).addEventListener(`click`, function cancelButtons(e) {
        carOrderBtnEvent = true;
        document.getElementById('reorder-car').click();
        carOrderBtnEvent = false;
        cancelRemakeOrder(funcReference, cancelButtons);
    });
}

function cancelRemakeOrder(eventListener, e) {
    clearTimeout(timeOutMsgs);
    document.getElementById(`reorder-car`).disabled = false;
    document.getElementById(`response-result`).style.display = `none`;
    document.getElementById('order-car-menu').style.display = `none`;
    removeReorderCarClickListener(eventListener, e);
}

function reorderCarClickListener(reorderCar, e, carManufacturer, carModel, carYear) {
    remakeOrder(reorderCar, e, carManufacturer, carModel, carYear);
}

function removeReorderCarClickListener(reorderCarClickListener, e) {
    document.getElementById('reorder-car').removeEventListener('click', reorderCarClickListener);
    document.getElementById('cancel-order').removeEventListener('click', e);
    document.getElementById('cancel-order-icon').removeEventListener('click', e);
}

// Function to close the pop-up
function closePopup() {
    document.getElementById('overlay').style.display = 'none';
}

function removeCarOrder(e) {
    let carManufacturer = e.currentTarget.parentNode.parentNode.children[0].children[0].children[1].textContent;
    const carModel = e.currentTarget.parentNode.parentNode.children[0].children[1].children[1].textContent;
    const carYear = e.currentTarget.parentNode.parentNode.children[0].children[2].children[1].textContent;

    e.currentTarget.parentNode.parentNode.parentNode.parentNode.remove();
    fetch(`${window.location.origin}/api/carOrders/remove`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            id: userId,
            carManufacturer: carManufacturer,
            carModel: carModel,
            carYear: carYear,
            authToken: authToken
        })
    })
        .then(response => response.text)
        .then(result => {
            console.log(result);
        })
}

// Function to handle "Remove car" button click
function removeTheCar(e) {
    const carId = document.querySelector(`.car-id-remove`).href;

    document.querySelector(`.remove-car`).remove();
    document.querySelectorAll(`.car-orders-container .car-order-model .car-card a`).forEach(entrie => {
        if (entrie.href === carId) {
            entrie.parentNode.children[2].children[0].textContent = `Add to Favorites`;
            entrie.parentNode.children[2].children[1].children[0].checked = false;
            entrie.parentNode.children[2].children[1].children[1].style.color = `#666`;
        }
    });
    document.querySelectorAll(`.recently-viewed-cars .car-card a`).forEach(entrie => {
        if (entrie.href === carId) {
            entrie.parentNode.children[2].children[0].textContent = `Add to Favorites`;
            entrie.parentNode.children[2].children[1].children[0].checked = false;
            entrie.parentNode.children[2].children[1].children[1].style.color = `#666`;
        }
    });
    fetch(`${window.location.origin}/api/favorites/remove`, {
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
            favVehiclesIds.splice(favVehiclesIds.indexOf(carId), 1);
            console.log(response.text());
        })
        .catch(err => console.log(err));
    closePopup();
}

// Function to handle "No thanks" button click
function noThanks() {
    document.querySelector(`.car-id-remove`).classList.remove(`car-id-remove`);
    document.querySelector(`.remove-car`).classList.remove(`remove-car`);
    closePopup();
}

function removeFavVehicle(e) {
    e.currentTarget.checked = true;
    e.currentTarget.parentNode.parentNode.parentNode.children[3].classList.add(`car-id-remove`);
    e.currentTarget.parentNode.parentNode.parentNode.parentNode.classList.add(`remove-car`);
    document.getElementById('overlay').style.display = 'flex';
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

//Function to log out the user
function logOutUser() {
    document.querySelectorAll(`#log-out-icon`).forEach(entrie => {
        entrie.addEventListener(`click`, () => {
            // Set expiry to a past date, and include path and domain
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            document.cookie = "recently_viewed=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; ";

            // Reload the page
            location.reload();
        });
    });
    document.querySelectorAll(`#log-out-text`).forEach(entrie => {
        entrie.addEventListener(`click`, () => {
            // Set expiry to a past date, and include path and domain
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            document.cookie = "recently_viewed=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; ";

            // Reload the page
            location.reload();
        });
    });
}