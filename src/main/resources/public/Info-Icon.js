// Function to toggle the visibility of camera controls information
function toggleControlsInfo() {
    var controlsInfo = document.getElementById('controlls-info');
    if (controlsInfo.style.display === 'none') {
        controlsInfo.style.display = 'block';
        document.getElementById(`loading-overlay-container`).style.top = "68%";
    } else {
        controlsInfo.style.display = 'none';
        document.getElementById(`loading-overlay-container`).style.top = "50%";
    }
}
