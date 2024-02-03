// Function to parse the URL and get the value of the 'car' parameter
function getCarParam() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('car');
}

// Function to get the array of saved car params from the cookie
function getSavedCarParams() {
    const savedParamsCookie = document.cookie.split('; ').find(cookie => cookie.startsWith('saved_car_params='));
    if (savedParamsCookie) {
        const paramsString = savedParamsCookie.split('=')[1].replace(/\+/g, ' ');
        return paramsString.split(',');
    }
    return [];
}

// Function to save a new car param to the cookie
function saveCarParam(newCarParam) {
    let savedParams = getSavedCarParams();

    if (!savedParams.includes(newCarParam) && authToken) {
        const decodedToken = JSON.parse(atob(authToken.split('.')[1]));
        const userId = decodedToken.userId;
        savedParams.push(newCarParam);
        fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/recently-viewed/add?userId=${userId}&carId=${newCarParam}`, {
            method: "POST"
        })
            .then(response => response.text())
            .then(result => {
                if (result.includes(`Successfully added the car.`)) {
                    // Save the updated array to the cookie
                    document.cookie = `saved_car_params=${savedParams
                        .filter(s => s !== null && s !== "")
                        .join(',')}; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure`;
                }
            })
            .catch(err => console.log(err))
    } else if (!savedParams.includes(newCarParam)) {
        savedParams.push(newCarParam);
        document.cookie = `saved_car_params=${savedParams.join(',')}; expires=${new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toUTCString()}; path=/;  domain=danov-autoshow-656625355b99.herokuapp.com; secure`;
    }
}

// Usage
const carParamValue = getCarParam();

if (carParamValue) {
    // Save the new car param to the cookie
    saveCarParam(carParamValue);
}
