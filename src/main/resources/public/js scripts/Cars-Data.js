showLoadingOverlay();
const urlParams = new URLSearchParams(window.location.search);
const carParam = urlParams.get('car').split(`3D%20Models/`)[1];
const carMake = carParam.split(`[-]`)

const url = `${window.location.origin}/api/proxy/carquery-car-data?make=${car}`;
// Make a GET request
fetch(url, {
    method: 'GET',
    headers: {
        "Content-Type": "application/json"
    },
})
    .then(response => {
        // Check if the request was successful (status code 200)
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        // Parse the JSON response
        return response.json();
    })
    .then(data => {
        for (const dataEntries of Object.entries(data)) {
            const [key, value] = dataEntries;
            if (key === `attributes`) {
                for (const attributesEntries of Object.entries(value)) {
                    const [key, value] = attributesEntries;
                    if (value !== ``) {
                        const dataContainerWrapper = document.createElement(`tr`);
                        const dataContainer = document.createElement(`td`);
                        for (const keySplitted of key.split(`_`)) {
                            dataContainer.textContent += keySplitted.charAt(0).toUpperCase() + keySplitted.substring(1) + " ";
                        }
                        dataContainer.textContent = dataContainer.textContent.trim();
                        const dataContainerValue = document.createElement(`td`);
                        dataContainerValue.textContent += value;
                        if (dataContainer.textContent.split(`:`)[0] === `Interior Trim` ||
                            dataContainer.textContent.split(`:`)[0] === `Exterior Color`) {
                        } else {
                            dataContainerWrapper.appendChild(dataContainer);
                            if (vin === `SBM12ABA4FW000283` && carParam.includes(`McLaren-P1-2019.glb`) &&
                                value === `year`) {
                                dataContainerValue.textContent = `2019`;
                            }
                            dataContainerWrapper.appendChild(dataContainerValue);
                            const fragment = document.createDocumentFragment();
                            fragment.appendChild(dataContainerWrapper);
                            document.querySelector(`#outside-wrapper .cars-data`).appendChild(fragment);
                        }
                    } else if (vin === `SBM12ABA4FW000283` && key === `manufacturer_suggested_retail_price`) {
                        const dataContainerWrapper = document.createElement(`tr`);
                        const dataContainer = document.createElement(`td`);
                        for (const keySplitted of key.split(`_`)) {
                            dataContainer.textContent += keySplitted.charAt(0).toUpperCase() + keySplitted.substring(1) + " ";
                        }
                        dataContainer.textContent = dataContainer.textContent.trim();
                        const dataContainerValue = document.createElement(`td`);
                        dataContainerValue.textContent += `$2 450 000 USD`;
                        if (dataContainer.textContent.split(`:`)[0] === `Interior Trim` ||
                            dataContainer.textContent.split(`:`)[0] === `Exterior Color`) {
                        } else {
                            dataContainerWrapper.appendChild(dataContainer);
                            dataContainerWrapper.appendChild(dataContainerValue);
                            const fragment = document.createDocumentFragment();
                            fragment.appendChild(dataContainerWrapper);
                            document.querySelector(`#outside-wrapper .cars-data`).appendChild(fragment);
                        }
                    }
                }
            } else if (key === `equipment`) {
                for (const attributesEntries of Object.entries(value)) {
                    const [key, value] = attributesEntries;
                    if (value === `Std.` || value === `Opt.`) {
                        const dataContainerWrapper = document.createElement(`tr`);
                        const dataContainer = document.createElement(`td`);
                        for (const keySplitted of key.split(`_`)) {
                            dataContainer.textContent += keySplitted.charAt(0).toUpperCase() + keySplitted.substring(1) + " ";
                        }
                        dataContainer.textContent = dataContainer.textContent.trim();
                        const dataContainerValue = document.createElement(`td`);
                        dataContainerValue.textContent += `✅`;
                        if (dataContainer.textContent.split(`:`)[0] === `Interior Trim` ||
                            dataContainer.textContent.split(`:`)[0] === `Exterior Color`) {
                        } else {
                            dataContainerWrapper.appendChild(dataContainer);
                            dataContainerWrapper.appendChild(dataContainerValue);

                            const fragment = document.createDocumentFragment();
                            fragment.appendChild(dataContainerWrapper);
                            document.querySelector(`#outside-wrapper-equipment .cars-equipment`).appendChild(fragment);
                        }
                    }
                }

                if (document.querySelector(`#outside-wrapper-equipment .cars-equipment`).children.length === 0) {
                    const h1 = document.createElement(`h3`);
                    h1.textContent = `No available data for this model`;
                    document.querySelector(`#outside-wrapper-equipment .cars-equipment`).appendChild(h1);
                }
            }
        }
        hideLoadingOverlay();
    })
    .catch(error => {
        // Log any errors to the console
        hideLoadingOverlay();
        console.error('Error:', error);
    });

function showLoadingOverlay() {
    console.log('Showing loading overlay');
    document.getElementById(`vehicle-spec`).style.display = `none`;
    document.getElementById(`vehicle-equip`).style.display = `none`;
    const container = document.getElementById(`loader`);
    const loadingOverlayContainer = document.createElement('div');
    loadingOverlayContainer.id = 'loading-overlay-container';
    loadingOverlayContainer.style.display = `flex`;
    loadingOverlayContainer.style.top = `35%`;
    const loadingOverlay = document.createElement('div');
    loadingOverlay.id = 'loading-overlay';
    loadingOverlay.innerHTML = '<div class="lds-ring"><div></div><div></div><div></div><div></div></div>';

    loadingOverlayContainer.appendChild(loadingOverlay);
    container.appendChild(loadingOverlayContainer);
}

function hideLoadingOverlay() {
    console.log('Hiding loading overlay');
    const loadingOverlay = document.getElementById('loading-overlay');
    if (loadingOverlay) {
        loadingOverlay.remove();
        document.getElementById(`vehicle-spec`).style.display = `block`;
        document.getElementById(`vehicle-equip`).style.display = `block`;
    }
}