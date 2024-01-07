let apiUrl = 'https://danov-autoshow-656625355b99.herokuapp.com/api/proxy-carMenu/carquery-api?make=&model=';

const loadingElement = document.createElement(`option`);
loadingElement.value = `loading`;
loadingElement.selected = true;
loadingElement.textContent = `Loading`;

const loadingElement2 = document.createElement(`option`);
loadingElement2.value = `loading`;
loadingElement2.selected = true;
loadingElement2.textContent = `Loading`;

const loadingElement3 = document.createElement(`option`);
loadingElement3.value = `loading`;
loadingElement3.selected = true;
loadingElement3.textContent = `Loading`;

// Function to parse JSONP response
function parseJSONP(response) {
    return new Promise((resolve, reject) => {
        // Read the response body as text
        response.text().then(text => {
            // Extract JSON string from the response
            const jsonString = text.substring(text.indexOf('{'), text.lastIndexOf('}') + 1);

            // Parse the JSON string
            try {
                const jsonData = JSON.parse(jsonString);
                resolve(jsonData);
            } catch (error) {
                reject(error);
            }
        });
    });
}

// Make the request
document.getElementById(`car-manufacturer`).addEventListener(`change`, () => {
    populateModels();
});

document.getElementById(`car-model`).addEventListener(`change`, () => {
    populateDataYears();
})

function populateDataYears() {
    document.getElementById(`car-year`).innerHTML = ``;
    document.getElementById(`car-year`).appendChild(loadingElement3);
    apiUrl = `https://danov-autoshow-656625355b99.herokuapp.com/api/proxy-carMenu/carquery-api?make=${document.getElementById(`car-manufacturer`).value}&model=${document.getElementById(`car-model`).value}`;
    fetch(apiUrl)
        .then(parseJSONP)
        .then(data => {
            let lastYear = ``;
            document.getElementById(`car-year`).innerHTML = ``;
            for (const jsonData of Object.entries(data)) {
                const [key, value] = jsonData;
                for (const carModel of value) {
                    if (carModel.model_year !== lastYear) {
                        const option = document.createElement(`option`);
                        option.value = carModel.model_year;
                        option.textContent = carModel.model_year;
                        document.getElementById(`car-year`).appendChild(option);
                    }
                    lastYear = carModel.model_year;
                }
                break;
            }
        })
        .catch(err => console.log(err));
}

function populateModels(params) {
    document.getElementById(`car-year`).appendChild(loadingElement3);
    document.getElementById(`car-model`).innerHTML = ``;
    document.getElementById(`car-model`).appendChild(loadingElement2);
    apiUrl = `https://danov-autoshow-656625355b99.herokuapp.com/api/proxy-carMenu/carquery-api?make=${document.getElementById(`car-manufacturer`).value}&model=`;
    fetch(apiUrl)
        .then(parseJSONP)
        .then(data => {
            document.getElementById(`car-model`).innerHTML = ``;
            for (const jsonData of Object.entries(data)) {
                const [key, value] = jsonData;
                for (const carModel of value) {
                    const option = document.createElement(`option`);
                    option.value = carModel.model_name;
                    option.textContent = carModel.model_name;
                    document.getElementById(`car-model`).appendChild(option);
                }
                break;
            }
            apiUrl = `https://danov-autoshow-656625355b99.herokuapp.com/api/proxy-carMenu/carquery-api?make=${document.getElementById(`car-manufacturer`).value}&model=${document.getElementById(`car-model`).value}`;
            fetch(apiUrl)
                .then(parseJSONP)
                .then(data => {
                    document.getElementById(`car-year`).innerHTML = ``;
                    let lastYear = ``;
                    for (const jsonData of Object.entries(data)) {
                        const [key, value] = jsonData;
                        for (const carModel of value) {
                            if (carModel.model_year !== lastYear) {
                                const option = document.createElement(`option`);
                                option.value = carModel.model_year;
                                option.textContent = carModel.model_year;
                                document.getElementById(`car-year`).appendChild(option);
                            }
                            lastYear = carModel.model_year;
                        }
                        break;
                    }
                })
                .catch(err => console.log(err));
        }).catch(err => console.log(err));
}

function populateData() {
    document.getElementById(`car-year`).innerHTML = ``;
    document.getElementById(`car-year`).appendChild(loadingElement3);
    document.getElementById(`car-model`).innerHTML = ``;
    document.getElementById(`car-model`).appendChild(loadingElement2);
    document.getElementById(`car-manufacturer`).innerHTML = ``;
    document.getElementById(`car-manufacturer`).appendChild(loadingElement);
    fetch(apiUrl)
        .then(response => response.json()) // Parse the JSONP response
        .then(data => {
            document.getElementById(`car-manufacturer`).innerHTML = ``;
            for (const jsonData of Object.entries(data)) {
                const [key, value] = jsonData;
                const option = document.createElement(`option`);
                option.value = value.make_id;
                option.textContent = value.make_display;
                document.getElementById(`car-manufacturer`).appendChild(option);
            }
            apiUrl = `https://danov-autoshow-656625355b99.herokuapp.com/api/proxy-carMenu/carquery-api?make=${document.getElementById(`car-manufacturer`).value}&model=`;
            fetch(apiUrl)
                .then(parseJSONP)
                .then(data => {
                    document.getElementById(`car-model`).innerHTML = ``;
                    for (const jsonData of Object.entries(data)) {
                        const [key, value] = jsonData;
                        for (const carModel of value) {
                            const option = document.createElement(`option`);
                            option.value = carModel.model_name;
                            option.textContent = carModel.model_name;
                            document.getElementById(`car-model`).appendChild(option);
                        }
                        break;
                    }
                    apiUrl = `https://danov-autoshow-656625355b99.herokuapp.com/api/proxy-carMenu/carquery-api?make=${document.getElementById(`car-manufacturer`).value}&model=${document.getElementById(`car-model`).value}`;
                    fetch(apiUrl)
                        .then(parseJSONP)
                        .then(data => {
                            document.getElementById(`car-year`).innerHTML = ``;
                            let lastYear = ``;
                            for (const jsonData of Object.entries(data)) {
                                const [key, value] = jsonData;
                                for (const carModel of value) {
                                    if (carModel.model_year !== lastYear) {
                                        const option = document.createElement(`option`);
                                        option.value = carModel.model_year;
                                        option.textContent = carModel.model_year;
                                        document.getElementById(`car-year`).appendChild(option);
                                    }
                                    lastYear = carModel.model_year;
                                }
                                break;
                            }
                        })
                        .catch(err => console.log(err));
                }).catch(err => console.log(err));

        })
        .catch(error => {
            console.log('Error:', error);
        });

}

populateData();