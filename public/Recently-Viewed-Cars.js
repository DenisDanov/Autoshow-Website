// Function to parse the URL and get the value of the 'car' parameter
function getCarParam() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('car');
}

// Function to get the array of saved car params from the cookie
function getSavedCarParams() {
    const savedParamsCookie = document.cookie.split('; ').find(cookie => cookie.startsWith('saved_car_params='));
    if (savedParamsCookie) {
        return savedParamsCookie.split('=')[1].split(',');
    }
    return [];
}

// Function to save a new car param to the cookie
function saveCarParam(newCarParam) {
    let savedParams = getSavedCarParams();

    if (!savedParams.includes(newCarParam)) {
        savedParams.push(newCarParam);
    }

    // Save the updated array to the cookie
    document.cookie = `saved_car_params=${savedParams.join(',')}; expires=${new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toUTCString()}; path=/;`;
}

// Usage
const carParamValue = getCarParam();

if (carParamValue) {
    // Save the new car param to the cookie
    saveCarParam(carParamValue);

    // You can now use the getSavedCarParams() function to get the array of saved car params
    console.log(getSavedCarParams());
}
