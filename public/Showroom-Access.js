// Helper function to get the value of a cookie by name
function getCookiesCars(name) {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    if (match) return match[2];
    return null;
}

// Helper function to count occurrences of a specific carParam in a comma-separated list
function countCarParam(token, carParam) {
    const carParams = token.split(',').map(param => param.trim());
    return carParams.filter(param => param === carParam).length;
}

function showroomAccess(href) {
    // Create a URLSearchParams object to work with the query parameters
    const params = new URLSearchParams(href.split('?')[1]);

    // Get the value of the 'car' parameter
    const carParam = params.get('car');

    // Get the existing value of the showroomToken cookie
    const existingToken = getCookiesCars('showroomToken');

    // Check if the new carParam is not already in the existing token
    if (!existingToken || countCarParam(existingToken, carParam) === 0) {
        // Concatenate the existing value with the new carParam
        const newToken = existingToken ? `${existingToken},${carParam}` : carParam;

        // Set the updated cookie with the authentication token
        document.cookie = 'showroomToken=' + newToken + '; expires=' + new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toUTCString() + '; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure';
    }
}

function removeCarFromCookie(carUrl) {
    // Get the existing value of the cookie
    const existingToken = getCookiesCars('showroomToken');
    const params = new URLSearchParams(carUrl.split('?')[1]);

    // Get the value of the 'car' parameter from the URL
    const carParamToRemove = params.get('car');

    // Check if the cookie exists and has at least one carParam
    if (existingToken) {
        // Split the cookie value into an array of individual car parameters
        const carParams = existingToken.split(',').map(param => param.trim());

        // Remove the specified carParam from the array
        const updatedToken = carParams.filter(param => param !== carParamToRemove).join(',');

        // Update the cookie with the modified value
        document.cookie = 'showroomToken=' + updatedToken + '; expires=' + new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toUTCString() + '; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure';
    }
}

// Helper function to get the value of a cookie by name
function getCookiesCars(name) {
    const match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    if (match) return match[2];
    return null;
}
