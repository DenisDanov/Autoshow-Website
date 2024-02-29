document.addEventListener("DOMContentLoaded", function() {
    const menu = document.getElementById("menu");
    const menuItems = document.querySelectorAll(".menuItem");
    const hamburger = document.querySelector(".hamburger");
    const menuIcon = document.querySelector(".menuIcon");

    function toggleMenu() {
        menu.classList.toggle("showMenu");
        menuIcon.style.display = menuIcon.style.display === "none" ? "block" : "none";
    }

    hamburger.addEventListener("click", toggleMenu);

    menuItems.forEach(function(menuItem) {
        menuItem.addEventListener("click", toggleMenu);
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