var screenWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
recentlyViewedLoaded.then(response => {
    let recentlyViewedCarsCount = document.querySelector(`.recently-viewed-cars`).children.length;
    if (recentlyViewedCarsCount > 3) {
        recentlyViewedCarsCount = 3;
    }
    if (screenWidth <= 600) {
        recentlyViewedCarsCount = 1;
    }
    $(document).ready(function () {
        $('.recently-viewed-cars').slick({
            slidesToShow: recentlyViewedCarsCount,
            slidesToScroll: 1,
            infinite: false,
        });
    });
});
