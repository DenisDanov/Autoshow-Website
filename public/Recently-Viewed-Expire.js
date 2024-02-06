const existingCookieName = "flag";
const existingCookie = document.cookie.split(';')
    .map(cookie => cookie.trim())
    .find(cookie => cookie.startsWith(existingCookieName + '='));

if (authToken && !existingCookie) {

    var decodedToken = JSON.parse(atob(authToken.split('.')[1]));
    var userId = decodedToken.userId;
    fetch(`https://danov-autoshow-656625355b99.herokuapp.com/api/recently-viewed/get?userId=${userId}&authToken=${authToken}`)
        .then(response => response.json())
        .then(result => {
            const date = new Date(result);
            if (date) {
                updateCookieExpiration("saved_car_params", date);
            }
        })
        .catch(err => console.log(err))

}

function updateCookieExpiration(name, newExpirationDate) {
    // Get the existing cookie value
    const existingCookie = document.cookie.split('; ')
        .find(cookie => cookie.startsWith(name + '='));

    if (existingCookie) {
        // Convert the new expirationDate to a UTC string
        const expires = newExpirationDate.toUTCString();

        // Check if the "maxAgeUpdated" attribute already exists
        const isMaxAgeUpdated = /maxAgeUpdated=true/.test(existingCookie);

        // If not, add the "maxAgeUpdated" attribute
        if (!isMaxAgeUpdated) {
            // Append the new attribute and expiration date to the existing cookie
            const updatedCookieString = `${existingCookie}; maxAgeUpdated=true; expires=${expires}; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure`;

            // Set the cookie again with the updated attributes
            document.cookie = updatedCookieString;
            document.cookie = `flag=; expires=${expires}; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure`;

            console.log('Cookie expiration and maxAgeUpdated attribute updated successfully.');
        } else {
            console.log('maxAgeUpdated attribute already present in the cookie. No update needed.');
        }
    } else {
        console.error('Cookie not found.');
    }
}
