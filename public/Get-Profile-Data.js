var authToken = getCookie("authToken");
const favVehiclesIds = [];
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
            const username = result.username;
            const email = result.email;
            const favVehiclesArr = result.favVehicles

            document.getElementById("username").value = username;
            document.getElementById("email").value = email;
            for (const vehicle of favVehiclesArr) {
                const favVehiclesContainer = document.createElement(`li`);
                favVehiclesIds.push(vehicle.vehicleId);
                favVehiclesContainer.innerHTML = `
            <div class="car-card">
            <div class="img-container">
                <img src="${vehicle.vehicleImg}" alt="Car 2">
            </div>
            <div class="car-info">
                <h3>${vehicle.vehicleName}</h3>
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
            <a href="${vehicle.vehicleId}" class="view-button">View in
                Showroom</a>
        </div>
            `
                document.getElementById(`favorite-vehicles`).appendChild(favVehiclesContainer);
                favVehiclesContainer.children[0].children[2].children[1].children[0].checked = true;
                favVehiclesContainer.children[0].children[2].children[1].children[0].addEventListener(`change`, removeFavVehicle);
                document.getElementById(`remove-btn`).addEventListener(`click`, removeTheCar);
            }
        })

    fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/carOrders/get?id=${userId}`)
        .then(response => response.json())
        .then(result => {
            for (const carOrder of result) {
                const container = document.createElement(`li`);
                let carManufacturer = carOrder.carManufacturer;
                carManufacturer = carManufacturer.charAt(0).toUpperCase() + carManufacturer.substring(1);
                container.innerHTML = `
                <div class="car-orders-container">
                <div class="car-order-details">
                    <div>
                        <span>Car manufacturer</span>
                        <p>${carManufacturer}</p>
                    </div>
                    <div>
                        <span>Car model</span>
                        <p>${carOrder.carModel}</p>
                    </div>
                    <div>
                        <span>Manufacture year</span>
                        <p>${carOrder.carYear}</p>
                    </div>
                </div>
                <div class="car-order-status">
                    <div>
                        <span>Order status</span>
                        <p class="order-status" status="${carOrder.orderStatus}">${carOrder.orderStatus}</p>
                    </div>
                    <div>
                        <span>Order date</span>
                        <p>${carOrder.dateOfOrder}</p>
                    </div>
                </div>
                <div class="car-order-model" style="display: none;">
                    <h1>Ordered car</h1>
                    <div class="car-card">
                        <div class="img-container">
                            <img src="images/${carManufacturer}-${carOrder.carModel}.png" alt="Car 2">
                        </div>
                        <div class="car-info">
                            <h3>${carOrder.carYear} ${carManufacturer.toUpperCase()} ${carOrder.carModel.toUpperCase()}</h3>
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
                        <a href="showroom.html?car=3D Models/${carManufacturer}-${carOrder.carModel}-${carOrder.carYear}.glb"
                            class="view-button">View in
                            Showroom</a>
                    </div>
                </div>
                <div id="cancel-order-container">
                <button id="change-order">Change Order</button>
                <button id="cancel-order">Cancel Order</button>
            </div>
            </div>
                `
                container.querySelector(`#change-order`).addEventListener(`click`, (e) => {
                    modifyOrderFunc(e, carManufacturer, carOrder);
                });
                const imagePath = `images/${carManufacturer}-${carOrder.carModel}.png`;
                const img = new Image();
                img.src = imagePath;
                orderStatusCheck(img, container, carManufacturer, carOrder);
                container.querySelector(`#cancel-order`).addEventListener(`click`, removeCarOrder);
                document.getElementById(`car-orders`).appendChild(container);
            }
        })
        .catch(err => console.log(err));
}

function cancelRemakeOrder(e) {
    document.getElementById('order-car-menu').style.display = `none`;
    document.querySelector('[modify-reference="true"]').removeAttribute(`modify-reference`);
}

function remakeOrder(e, carManufacturer, carModel, carYear) {
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
        if (!checkForSameOrder) {
            fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/carOrders/modify`, {
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
                    newYear: newYear
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
                        setTimeout(function () {
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
                                    <img src="images/${carManufacturer}-${result.carModel}.png" alt="Car 2">
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
                            modifyReference.parentNode.parentNode.parentNode.querySelector(`#change-order`).
                                addEventListener(`click`, (e) => {
                                    modifyOrderFunc(e, carManufacturer, result);
                                });
                            const imagePath = `images/${carManufacturer}-${result.carModel}.png`;
                            const img = new Image();
                            img.src = imagePath;
                            orderStatusCheck(img, modifyReference.parentNode.parentNode.parentNode,
                                carManufacturer, result);
                            modifyReference.parentNode.parentNode.parentNode.querySelector(`#cancel-order`).addEventListener(`click`, removeCarOrder);
                            modifyReference.parentNode.parentNode.parentNode.querySelector('[modify-reference="true"]').remove();
                            modifyReference.removeAttribute(`modify-reference`);
                            document.getElementById(`response-result`).style.display = `none`;
                            document.getElementById('order-car-menu').style.display = `none`;
                            document.getElementById(`reorder-car`).disabled = false;
                        }, 2000);
                    } else {
                        resultHtmlEle.textContent = result.result;
                        resultHtmlEle.style.display = `block`;
                        resultHtmlEle.style.backgroundColor = `red`;
                        resultHtmlEle.style.color = `white`;
                        resultHtmlEle.style.border = `5px solid red`;
                        resultHtmlEle.style.borderRadius = `5px`;
                        document.getElementById(`reorder-car`).disabled = true;
                        setTimeout(function () {
                            document.getElementById(`response-result`).style.display = `none`;
                            document.getElementById(`reorder-car`).disabled = false;
                        }, 2000);
                    }
                })
                .catch(err => console.log(err));
        } else {
            resultHtmlEle.textContent = `Please order different model than the current one.`;
            resultHtmlEle.style.display = `block`;
            resultHtmlEle.style.backgroundColor = `red`;
            resultHtmlEle.style.color = `white`;
            resultHtmlEle.style.border = `5px solid red`;
            resultHtmlEle.style.borderRadius = `5px`;
            setTimeout(function () {
                document.getElementById(`response-result`).style.display = `none`;
            }, 3000);
        }
    } else if (modifyReference.id !== `change-order`) {
        fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/carOrders/modify`, {
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
                newYear: newYear
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
                    setTimeout(function () {
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
                                <img src="images/${carManufacturer}-${result.carModel}.png" alt="Car 2">
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
                        modifyReference.parentNode.parentNode.parentNode.querySelector(`#change-order`).
                            addEventListener(`click`, (e) => {
                                modifyOrderFunc(e, carManufacturer, result);
                            });
                        const imagePath = `images/${carManufacturer}-${result.carModel}.png`;
                        const img = new Image();
                        img.src = imagePath;
                        orderStatusCheck(img, modifyReference.parentNode.parentNode.parentNode,
                            carManufacturer, result);
                        modifyReference.parentNode.parentNode.parentNode.querySelector(`#cancel-order`).addEventListener(`click`, removeCarOrder);
                        modifyReference.parentNode.parentNode.parentNode.querySelector('[modify-reference="true"]').remove();
                        document.getElementById(`response-result`).style.display = `none`;
                        document.getElementById('order-car-menu').style.display = `none`;
                        document.getElementById(`reorder-car`).disabled = false;
                        modifyReference.removeAttribute(`modify-reference`);
                    }, 2000);
                } else {
                    resultHtmlEle.textContent = result.result;
                    resultHtmlEle.style.display = `block`;
                    resultHtmlEle.style.backgroundColor = `red`;
                    resultHtmlEle.style.color = `white`;
                    resultHtmlEle.style.border = `5px solid red`;
                    resultHtmlEle.style.borderRadius = `5px`;
                    document.getElementById(`reorder-car`).disabled = true;
                    setTimeout(function () {
                        document.getElementById(`response-result`).style.display = `none`;
                        document.getElementById(`reorder-car`).disabled = false;
                    }, 2000);
                }
            })
            .catch(err => console.log(err));
    }
}

function addCarOrderToFavs(e) {
    if (e.currentTarget.checked) {
        const carId = e.currentTarget.parentNode.parentNode.parentNode.
            children[e.currentTarget.parentNode.parentNode.parentNode.children.length - 1].href;
        const carImg = e.currentTarget.parentNode.parentNode.parentNode.children[0]
            .children[0].getAttribute("src");
        const carName = e.currentTarget.parentNode.parentNode.parentNode.children[1].
            children[0].textContent;
        e.currentTarget.parentNode.parentNode.children[0].textContent = `Remove from Favorites`;

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
    <a href="${carId}" class="view-button">View in
        Showroom</a>
</div>
    `
        document.getElementById(`favorite-vehicles`).appendChild(favVehiclesContainer);
        favVehiclesContainer.children[0].children[2].children[1].children[0].checked = true;
        favVehiclesContainer.children[0].children[2].children[1].children[0].addEventListener(`change`, removeFavVehicle);
        document.getElementById(`remove-btn`).addEventListener(`click`, removeTheCar);
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
    } else {
        const carId = e.currentTarget.parentNode.parentNode.parentNode.
            children[e.currentTarget.parentNode.parentNode.parentNode.children.length - 1].href;
        const favVehicles = document.getElementById(`favorite-vehicles`).querySelectorAll(`li .car-card a`);
        for (const vehicle of favVehicles) {
            if (vehicle.href === carId) {
                vehicle.parentNode.parentNode.remove();
            }
        }
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
            .then(response => console.log(response))
            .catch(err => console.log(err));
    }
}

function removeCarOrder(e) {
    let carManufacturer = e.currentTarget.parentNode.parentNode.children[0].children[0].children[1].textContent;
    const carModel = e.currentTarget.parentNode.parentNode.children[0].children[1].children[1].textContent;
    const carYear = e.currentTarget.parentNode.parentNode.children[0].children[2].children[1].textContent;
    e.currentTarget.parentNode.parentNode.parentNode.remove();
    fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/carOrders/remove`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            id: userId,
            carManufacturer: carManufacturer,
            carModel: carModel,
            carYear: carYear
        })
    })
        .then(response => response.text)
        .then(result => {
            console.log(result);
        })
}

function orderStatusCheck(img, container, carManufacturer, carOrder) {
    img.onload = function () {
        container.children[0].querySelector(`#change-order`).remove();
        container.children[0].querySelector(`.car-order-model`).style.display = `flex`;
        container.children[0].children[1].children[0].children[1].textContent = `Completed`;
        container.children[0].children[1].children[0].children[1].setAttribute("status", "Completed");
        favVehiclesIds.forEach(vehicleId => {
            if (vehicleId === container.children[0].querySelector(`.car-order-model`)
                .children[1].querySelector(`a`).href) {
                container.children[0].querySelector(`.car-order-model`).children[1].children[2]
                    .children[1].children[0].checked = true;
                container.children[0].querySelector(`.car-order-model`).children[1].children[2]
                    .children[0].textContent = `Remove from Favorites`;
            }
        });
        container.children[0].querySelector(`.car-order-model`).children[1].children[2]
            .children[1].children[0].addEventListener(`change`, addCarOrderToFavs);
    };
    img.onerror = function () {
        container.children[0].querySelector(`.car-order-model`).remove();
        var dateOfOrder = new Date(carOrder.dateOfOrder);

        // Get the current date
        var currentDate = new Date();

        // Calculate the time difference in milliseconds
        var timeDifference = currentDate - dateOfOrder;

        // Convert milliseconds to days
        var daysDifference = Math.floor(timeDifference / (1000 * 60 * 60 * 24));

        // Check if 7 days have passed
        if (daysDifference >= 7) {
            // 7 days have passed, so the order has expired
            container.children[0].querySelector(`#change-order`).remove();
            container.children[0].children[1].children[0].children[1].textContent = `Expired`;
            container.children[0].children[1].children[0].children[1].setAttribute("status", "Expired");
            const modifyOrder = document.createElement(`button`);
            modifyOrder.id = `modify-order`;
            modifyOrder.textContent = `Remake order`;
            modifyOrder.setAttribute(`modify-reference`, "false");
            modifyOrder.addEventListener(`click`, (e) => {
                modifyOrderFunc(e, carManufacturer, carOrder);
            });
            container.children[0].querySelector(`#cancel-order-container`).appendChild(modifyOrder);
        }
    };
}

function modifyOrderFunc(modifyReference, carManufacturer, carOrder) {
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
                ele => ele.textContent.toLowerCase() === carOrder.carModel.toLowerCase()
            );
            if (optionToSelectModel) {
                optionToSelectModel.selected = true;
                populateDataYears().then(result => {
                    const optionToSelectYear = Array.from(carYearEle.children).find(
                        ele => ele.textContent === carOrder.carYear
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
    function handleReorderClick(e) {
        remakeOrder(e, carOrder.carManufacturer, carOrder.carModel, carOrder.carYear);
    }
    document.getElementById(`reorder-car`).removeEventListener(`click`, handleReorderClick);
    document.getElementById(`reorder-car`).addEventListener(`click`, handleReorderClick);
}

// Function to close the pop-up
function closePopup() {
    document.getElementById('overlay').style.display = 'none';
}

// Function to handle "Remove car" button click
function removeTheCar(e) {
    const carId = document.querySelector(`.car-id-remove`).href;
    document.querySelector(`.remove-car`).remove();
    document.querySelectorAll(`.car-orders-container .car-order-model .car-card a`).forEach(entrie => {
        if (entrie.href === carId) {
            entrie.parentNode.children[2].children[0].textContent = `Add to Favorites`;
            entrie.parentNode.children[2].children[1].children[0].checked = false;
        }
    });
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
        .then(response => console.log(response))
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
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure";

            // Reload the page
            location.reload();
        });
    });
    document.querySelectorAll(`#log-out-text`).forEach(entrie => {
        entrie.addEventListener(`click`, () => {
            // Set expiry to a past date, and include path and domain
            document.cookie = "authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure";

            // Reload the page
            location.reload();
        });
    });
}