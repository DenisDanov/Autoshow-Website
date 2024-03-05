// Suppose you're fetching data for username
const usernameSpinner = document.getElementById('username-spinner');
const emailSpinner = document.getElementById('email-spinner');
const favoritesSpinner = document.getElementById('favorites-spinner');
const carOrdersSpinner = document.getElementById('car-orders-spinner');
const recentlyViewedSpinner = document.getElementById('recently-viewed-spinner');

// Show loading spinner
usernameSpinner.style.display = 'block';

// Fetch data from backend
favoritesLoaded
    .then(data => {
        usernameSpinner.style.display = 'none';
        emailSpinner.style.display = `none`;
        favoritesSpinner.style.display = `none`;
        carOrdersSpinner.style.display = `none`;
        recentlyViewedSpinner.style.display = `none`;
    });