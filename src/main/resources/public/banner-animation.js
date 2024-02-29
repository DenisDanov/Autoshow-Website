
const siteBannerInner = document.querySelector('.site-banner-inner');

let shiftAmount = 0;
let slideSpeed = calculateSlideSpeed();
let originalBannerImages = Array.from(siteBannerInner.querySelectorAll('.banner-img'));

function calculateSlideSpeed() {
    const screenWidth = window.innerWidth;
    if (screenWidth < 600) {
        return 250;
    } else if (screenWidth >= 600 && screenWidth < 1200) {
        return 700;
    } else if (screenWidth <= 1400) {
        return 1000;
    } else {
        return 1280;
    }
}

let initialShift = 0;

function cloneAndAppendImages() {
    const currentImages = siteBannerInner.querySelectorAll('.banner-img');
    const lastImage = currentImages[currentImages.length - 1];
    const lastImageRect = lastImage.getBoundingClientRect();

    if (lastImageRect.right < window.innerWidth) {
        const fragment = document.createDocumentFragment();
        originalBannerImages.forEach((image) => {
            const clone = image.cloneNode(true);
            fragment.appendChild(clone);
        });
        siteBannerInner.appendChild(fragment);
    }
}

window.addEventListener('resize', () => {
    slideSpeed = calculateSlideSpeed();
});

function shiftBanner() {
    const distanceToMove = slideSpeed / 1000;

    shiftAmount -= distanceToMove;

    siteBannerInner.style.transform = `translate3d(${shiftAmount + initialShift}px, 0, 0)`;

    cloneAndAppendImages();

    requestAnimationFrame(shiftBanner);
}

// Check if the sessionStorage exists
const storedSessionShiftAmount = sessionStorage.getItem('sessionShiftAmount');
if (storedSessionShiftAmount) {
    shiftAmount = parseFloat(storedSessionShiftAmount);
    siteBannerInner.style.transform = `translateX(${shiftAmount + initialShift}px)`;
}

// Reset shiftAmount when the browser is restarted
window.addEventListener('beforeunload', () => {
    sessionStorage.setItem('sessionShiftAmount', '0');
});

// Listen for visibility change events
document.addEventListener('visibilitychange', () => {
    isPageVisible = !document.hidden;
});

requestAnimationFrame(shiftBanner);
