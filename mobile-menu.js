document.addEventListener("DOMContentLoaded", function() {
    const menu = document.getElementById("menu");
    const menuItems = document.querySelectorAll(".menuItem");
    const hamburger = document.querySelector(".hamburger");
    const closeIcon = document.querySelector(".closeIcon");
    const menuIcon = document.querySelector(".menuIcon");

    function toggleMenu() {
        menu.classList.toggle("showMenu");
        closeIcon.style.display = closeIcon.style.display === "none" ? "block" : "none";
        menuIcon.style.display = menuIcon.style.display === "none" ? "block" : "none";
    }

    hamburger.addEventListener("click", toggleMenu);

    menuItems.forEach(function(menuItem) {
        menuItem.addEventListener("click", toggleMenu);
    });
});

document.addEventListener("DOMContentLoaded", function() {
    var header = document.querySelector('.nav-header');
    var img = document.querySelector('.logo-img');
    var div = document.querySelector('.site-banner');
    var headerHeight = header.clientHeight;
    var isFixed = false;
  
    window.addEventListener('scroll', function() {
      var scrollPosition = window.scrollY || window.pageYOffset;
      if (scrollPosition >= headerHeight - 47 && !isFixed) {
        header.classList.add('fixed-header');
        img.classList.add('fixed-logo')
        isFixed = true;
     } else if (scrollPosition <= headerHeight - 47 && isFixed) {
        header.classList.remove('fixed-header');
        img.classList.remove('fixed-logo')
        div.classList.remove('fixed')
        isFixed = false;
      }
    });
});

var hamburgerButton = document.getElementById('hamburgerButton');

hamburgerButton.addEventListener('click', function () {
    if (hamburgerButton.classList.contains('active')) {
        hamburgerButton.classList.remove('active');
    } else {
        hamburgerButton.classList.add('active');
    }
});

var hamburgerButton = document.getElementById('hamburgerButton');

function setHamburgerRelative(event) {
    hamburgerButton.classList.remove("active");
}