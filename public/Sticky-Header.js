window.addEventListener('scroll', function() {
    var header = document.getElementById('header');
    var scrollPosition = window.scrollY || window.pageYOffset; // Handle cross-browser compatibility
    var threshold = 0;

    if (scrollPosition > threshold) {
        header.classList.add('fixed');
    } else {
        header.classList.remove('fixed');
    }
});
