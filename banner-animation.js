const siteBannerInner = document.querySelector('.site-banner-inner');
let shiftAmount = 0;
let slideSpeed = calculateSlideSpeed();
let originalBannerImages = Array.from(siteBannerInner.querySelectorAll('.banner-img'));

function calculateSlideSpeed() {
    const screenWidth = window.innerWidth;
    if (screenWidth < 600) {
        return 270;
    } else if (screenWidth >= 600 && screenWidth < 1200) {
        return 700;
    } else if (screenWidth <= 1400) {
        return 1000;
    } else {
        return 1300;
    }
}

let initialShift = 0;

function cloneAndAppendImages() {
    const currentImages = siteBannerInner.querySelectorAll('.banner-img');
    if (currentImages.length < originalBannerImages.length * 100) {
        originalBannerImages.forEach((image) => {
            const clone = image.cloneNode(true);
            siteBannerInner.appendChild(clone);
        });
    }
}

window.addEventListener('resize', () => {
    slideSpeed = calculateSlideSpeed();
});

let previousTimestamp = performance.now();

function shiftBanner() {
    const currentTimestamp = performance.now();
    const elapsed = currentTimestamp - previousTimestamp;
    previousTimestamp = currentTimestamp;

    const distanceToMove = slideSpeed / 1000;
    shiftAmount -= distanceToMove;

    siteBannerInner.style.transform = `translateX(${shiftAmount + initialShift}px)`;

    cloneAndAppendImages();

    requestAnimationFrame(shiftBanner);
}

requestAnimationFrame(shiftBanner);