showLoadingOverlay();
const urlParams = new URLSearchParams(window.location.search);
const carParam = urlParams.get('car');

let vin = '';
if (carParam.includes(`Urus`)) {
    vin = `ZPBUA1ZL4NLA19618`;
} else if (carParam.includes(`Porsche-Carrera-2015.glb`)) {
    vin = `WP0AA2A90FS106665`;
} else if (carParam.includes(`Lamborghini-Galardo-2007.glb`)) {
    vin = `ZHWGU12T87LA05336`;
} else if (carParam.includes(`Lamborghini-Aventador-2020.glb`)) {
    vin = `ZHWUM6ZD7KLA08087`;
} else if (carParam.includes(`Toyota-Gr Supra-2020.glb`)) {
    vin = `WZ1DB4C04LW026201`;
} else if (carParam.includes(`Porshe-911-2016.FBX`)) {
    vin = `WP0AD2A97FS166983`;
} else if (carParam.includes(`BMW-X5.glb`)) {
    vin = `5YMJU0C06N9J16964`;
} else if (carParam.includes(`Mclaren-P1-2020.glb`)) {
    vin = `SBM12ABA4FW000283`;
} else if (carParam.includes(`Tesla-Model 3-2020.glb`)) {
    vin = `5YJ3E1EC2PF577924`;
} else {
    vin = `ZPBUA1ZL4NLA19618`;
}

const url = `https://danovs-autoshow-afcbab0f302b.herokuapp.com/api/proxy/car-specs?vin=${vin}`;
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
                    if (value === `Std.`) {
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