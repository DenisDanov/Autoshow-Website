// Function to show the pop-up
function showPopup() {
    document.getElementById('popup-container').style.display = 'block';
}

// Function to hide the pop-up
function hidePopup() {
    document.getElementById('popup-container').style.display = 'none';
}

// Function to handle ordering a car
function popUpOrderCar() {
    hidePopup();
    document.getElementById('order-car-menu').style.display = `flex`;
}

// Function to handle canceling the order
function cancelPopUpOrder() {
    hidePopup();
}

// Show the pop-up when the page loads
window.onload = function () {
    showPopup();
    setTimeout(function () {
        hidePopup();
    }, 30000);
};