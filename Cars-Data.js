const isLocalhost = window.location.hostname === 'localhost';
const proxyUrl = isLocalhost ? 'http://localhost:3000/api' : '/api';
const urlParams = new URLSearchParams(window.location.search);
const carParam = urlParams.get('car');

let vin = '';
if (carParam.includes(`Urus`)) {
    vin = `ZPBUA1ZL4NLA19618`;
} else if (carParam.includes(`Porsche-Carrera-2015.glb`)) {
    vin = `WP0AA2A90FS106665`;
} else {
    vin = `ZPBUA1ZL4NLA19618`;
}
const queryParams = new URLSearchParams({
    key: 'rqx8xyzf5_uto6mwpym_php2r1u28',
    deepdata: '1',
    vin: vin,
});

const url = `${proxyUrl}/specs?${queryParams}`;

fetch(url)
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        for (const dataEntries of Object.entries(data)) {
            const [key, value] = dataEntries;
            if (key === `attributes`) {
                for (const attributesEntries of Object.entries(value)) {
                    const [key, value] = attributesEntries;
                    if (value !== ``) {
                        const dataContainerWrapper = document.createElement(`div`);
                        const dataContainer = document.createElement(`span`);
                        for (const keySplitted of key.split(`_`)) {
                            dataContainer.textContent += keySplitted.charAt(0).toUpperCase() + keySplitted.substring(1) + " ";
                        }
                        dataContainer.textContent = dataContainer.textContent.trim();
                        const dataContainerValue = document.createElement(`span`);
                        dataContainerValue.textContent += value;
                        if (dataContainer.textContent.split(`:`)[0] === `Interior Trim` ||
                            dataContainer.textContent.split(`:`)[0] === `Exterior Color`) {
                        } else {
                            dataContainerWrapper.appendChild(dataContainer);
                            dataContainerWrapper.appendChild(dataContainerValue);
                            const containers = document.querySelectorAll(`#outside-wrapper .cars-data`);
                            let index = 0;
                            for (const container of containers) {
                                index++;
                                if (container.offsetHeight >= 538 && index === containers.length) {
                                    const newContainer = document.createElement(`div`);
                                    newContainer.classList.add(`cars-data`);
                                    newContainer.appendChild(dataContainerWrapper);
                                    document.getElementById(`outside-wrapper`).appendChild(newContainer);
                                    break;
                                } else {
                                    container.appendChild(dataContainerWrapper);
                                }
                            }
                        }
                    }
                }
            } else if (key === `equipment`) {
                for (const attributesEntries of Object.entries(value)) {
                    const [key, value] = attributesEntries;
                    if (value === `Std.`) {
                        const dataContainerWrapper = document.createElement(`div`);
                        const dataContainer = document.createElement(`span`);
                        for (const keySplitted of key.split(`_`)) {
                            dataContainer.textContent += keySplitted.charAt(0).toUpperCase() + keySplitted.substring(1) + " ";
                        }
                        dataContainer.textContent = dataContainer.textContent.trim();
                        const dataContainerValue = document.createElement(`span`);
                        dataContainerValue.textContent += `✅`;
                        if (dataContainer.textContent.split(`:`)[0] === `Interior Trim` ||
                            dataContainer.textContent.split(`:`)[0] === `Exterior Color`) {
                        } else {
                            dataContainerWrapper.appendChild(dataContainer);
                            dataContainerWrapper.appendChild(dataContainerValue);
                            const containers = document.querySelectorAll(`#outside-wrapper-equipment .cars-equipment`);
                            let index = 0;
                            for (const container of containers) {
                                index++;
                                if (container.offsetHeight >= 538 && index === containers.length) {
                                    const newContainer = document.createElement(`div`);
                                    newContainer.classList.add(`cars-equipment`);
                                    newContainer.appendChild(dataContainerWrapper);
                                    document.getElementById(`outside-wrapper-equipment`).appendChild(newContainer);
                                    break;
                                } else {
                                    container.appendChild(dataContainerWrapper);
                                }
                            }
                        }
                    }
                }
            }
        }
    })
    .catch(error => {
        console.log('Error:', error);
    });
