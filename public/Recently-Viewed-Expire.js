if (authToken && isSessionCookie("saved_car_params")) {
    var decodedToken = JSON.parse(atob(authToken.split('.')[1]));
    var userId = decodedToken.userId;
    fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/recently-viewed/get?userId=${userId}&authToken=${authToken}`)
    .then(response => response.json())
    .then(result => {
        const date = new Date(result);
        if (date) {
            updateCookieExpiration("saved_car_params",date);
        }
    })
    .catch(err => console.log(err))
}

function updateCookieExpiration(name, newExpirationDate) {
    // Get the existing cookie value
    const existingCookie = document.cookie.split(';')
        .map(cookie => cookie.trim())
        .find(cookie => cookie.startsWith(name + '='));

    if (existingCookie) {
        // Parse the existing cookie to extract its value and other attributes
        const [cookieName, cookieValue, ...cookieAttributes] = existingCookie.split(';');

        // Convert the new expirationDate to a UTC string
        const expires = newExpirationDate.toUTCString();

        // Construct the updated cookie string with the same value and updated expiration date
        const updatedCookieString = `${cookieName}=${cookieValue}; expires=${expires}; ${cookieAttributes.join('; ')}; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure`;

        // Set the cookie again with the updated expiration date
        document.cookie = updatedCookieString;

        console.log('Cookie expiration updated successfully.');
    } else {
        console.error('Cookie not found.');
    }
}

function isSessionCookie(cookieName) {
    // Get all cookies from the document
    const allCookies = document.cookie;

    // If the cookie string is empty or undefined, return false
    if (!allCookies) {
        return false;
    }

    // Split the cookie string into individual cookies
    const cookies = allCookies.split(';').map(cookie => cookie.trim());

    // Find the specific cookie by name
    const targetCookie = cookies.find(cookie => cookie.startsWith(cookieName + '='));

    if (targetCookie) {
        // Split the target cookie to extract its attributes
        const [, cookieValue, ...cookieAttributes] = targetCookie.split(';');

        // Check if the "expires" attribute is present
        const expiresAttribute = cookieAttributes.find(attr => attr.trim().startsWith('expires='));

        // If "expires" attribute is not present, it's a session cookie
        return !expiresAttribute;
    }

    // If the cookie with the specified name is not found, return false
    return false;
}

