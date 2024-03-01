var screenWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
recentlyViewedLoaded.then(response => {
    let recentlyViewedCarsCount = document.querySelector(`.recently-viewed-cars`).children.length;
    if (recentlyViewedCarsCount > 3) {
        recentlyViewedCarsCount = 3;
    } else {
        document.querySelectorAll('.slider-container .recently-viewed-cars .car-card').forEach(entrie => {
            entrie.setAttribute(`max-width`,`true`);
        });        
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
