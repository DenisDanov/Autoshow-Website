showLoadingOverlay();
const urlParams = new URLSearchParams(window.location.search);
const carParam = urlParams.get('car').split(`3D Models/`)[1].split(`.glb`)[0];

document.querySelector(`#cars-data-wrapper div:nth-child(2)`).style.display = `none`;

const url = `${window.location.origin}/api/proxy-carMenu/carquery-car-data?car=${carParam}`;
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
            console.log(data);
            for (const dataValues of Object.entries(data)) {
                const [key, value] = dataValues;
                if (value !== null && (value !== -1.0)) {
                    const dataContainerWrapper = document.createElement(`tr`);
                    const dataContainer = document.createElement(`td`);
                    const dataContainerValue = document.createElement(`td`);

                    for (const keySplitted of key.split(`_`)) {
                        dataContainer.textContent += keySplitted.charAt(0).toUpperCase() + keySplitted.substring(1) + " ";
                    }

                    dataContainer.textContent = dataContainer.textContent.trim();

                    if (dataContainer.textContent.includes(`Make Display`)) {
                        dataContainer.textContent = `Manufacturer`;
                    } else if (dataContainer.textContent.includes(`Model Lkm Mixed`)) {
                        dataContainer.textContent = `Fuel Consumption (Mixed)`;
                    } else if (dataContainer.textContent.includes(`Model Lkm Hwy`)) {
                        dataContainer.textContent = `Fuel Consumption (Highway)`;
                    } else if (dataContainer.textContent.includes(`Model Lkm City`)) {
                        dataContainer.textContent = `Fuel Consumption (City)`;
                    } else if (dataContainer.textContent.includes(`Model Engine Power Rpm`)) {
                        dataContainer.textContent = `Engine Power`;
                    } else if (dataContainer.textContent.includes(`Body`)) {
                        dataContainer.textContent = `Body Style`;
                    } else if (dataContainer.textContent.includes(`Drive`)) {
                        dataContainer.textContent = `Drive Type`;
                    } else if (dataContainer.textContent.includes(`Co2`)) {
                        dataContainer.textContent = `CO2 Emissions`;
                    } else if (dataContainer.textContent.includes(`Engine Torque Rpm`)) {
                        dataContainer.textContent = `Engine Torque at RPM`;
                        dataContainerValue.setAttribute(`torque`, `true`);
                    } else if (dataContainer.textContent.includes(`Engine Torque Nm`)) {
                        document.querySelector(`td[torque='true']`).textContent =
                            `${value} Nm at ${data[`model_engine_torque_rpm`]} RPM`
                        continue;
                    }

                    if (dataContainer.textContent !== `Model Name` &&
                        dataContainer.textContent.includes(`Model`)) {
                        dataContainer.textContent = dataContainer.textContent.split(`Model `)[1];
                    }

                    if (dataContainer.textContent.includes(`Engine Power`)) {
                        dataContainerValue.textContent = `${data[`model_engine_power_ps`]} PS at ${value} RPM`;
                    } else if (dataContainer.textContent.includes(`Fuel Consumption (Mixed)`) ||
                        dataContainer.textContent.includes(`Fuel Consumption (City)`) ||
                        dataContainer.textContent.includes(`Fuel Consumption (Highway)`)) {
                        dataContainerValue.textContent += value + " L/100km";
                    } else {
                        dataContainerValue.textContent += value;
                    }

                    dataContainerWrapper.appendChild(dataContainer);
                    dataContainerWrapper.appendChild(dataContainerValue);
                    const fragment = document.createDocumentFragment();
                    fragment.appendChild(dataContainerWrapper);
                    if (!dataContainer.textContent.includes(`Engine Power Ps`)) {
                        document.querySelector(`#outside-wrapper .cars-data`).appendChild(fragment);
                    }
                }
            }
            hideLoadingOverlay();
        }
    )
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