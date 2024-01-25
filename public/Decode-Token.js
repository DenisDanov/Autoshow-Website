// Function to get the value of a cookie by name
function getCookieViewToken(name) {
    var cookies = document.cookie.split(';');
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].trim();
        if (cookie.indexOf(name + '=') === 0) {
            return cookie.substring(name.length + 1);
        }
    }
    return null;
}

// Function to set a cookie with a decoded value
function setDecodedCookie(name, decodedValue) {
    var cookieString = name + '=' + decodedValue + '; path=/; domain=danov-autoshow-656625355b99.herokuapp.com; secure';
    document.cookie = cookieString;
}

// Get the value of a cookie, decode it, and set it back decoded
var cookieName = 'saved_car_params';
var encodedCookieValue = getCookieViewToken(cookieName);

if (encodedCookieValue) {
    var decodedCookieValue = decodeURIComponent(encodedCookieValue);
    setDecodedCookie(cookieName, decodedCookieValue);
}
