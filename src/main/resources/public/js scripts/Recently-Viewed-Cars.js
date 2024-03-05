// Function to parse the URL and get the value of the 'car' parameter
function getCarParam() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('car');
}

// Function to get the array of saved car params from the cookie
function getSavedCarParams() {
    const savedParamsCookie = document.cookie.split('; ').find(cookie => cookie.startsWith('recently_viewed='));
    if (savedParamsCookie) {
        const paramsString = savedParamsCookie.split('=')[1].replace(/\+/g, ' ');
        return paramsString.split(',');
    }
    return [];
}

// Function to save a new car param to the cookie
function saveCarParam(newCarParam) {
    let savedParams = getSavedCarParams();

    if (authToken) {
        const decodedToken = JSON.parse(atob(authToken.split('.')[1]));
        const userId = decodedToken.userId;
        if (savedParams.includes(newCarParam)) {
            savedParams.splice(savedParams.indexOf(newCarParam), 1);
            savedParams.push(newCarParam);
        } else {
            savedParams.push(newCarParam);
        }
        fetch(`${window.location.origin}/api/recently-viewed/add?userId=${userId}&carId=${newCarParam}&authToken=${authToken}`, {
            method: "POST"
        })
            .then(response => response.text())
            .then(result => {
                if (result.split(".")[0].includes(`Successfully added the car`)) {
                    // Save the updated array to the cookie
                    const date = new Date(result.split(".")[1]);
                    const expires = date.toUTCString();
                    document.cookie = `recently_viewed=${savedParams
                        .filter(s => s !== null && s !== "")
                        .join(',')}; expires=${expires}; path=/;`;
                }
            })
            .catch(err => console.log(err))
    } else {
        if (savedParams.includes(newCarParam)) {
            savedParams.splice(savedParams.indexOf(newCarParam), 1);
            savedParams.push(newCarParam);
        } else {
            savedParams.push(newCarParam);
        }
        document.cookie = `recently_viewed=${savedParams.join(',')}; expires=${new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toUTCString()}; path=/;`;
    }
}

// Usage
const carParamValue = getCarParam();

if (carParamValue) {
    // Save the new car param to the cookie
    saveCarParam(carParamValue);
}