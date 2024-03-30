package com.example.app.controllers;

import com.example.app.controllers.utils.AuthTokenValidationUtil;
import com.example.app.controllers.utils.CookieUtils;
import com.example.app.controllers.utils.VehicleExistCheckUtil;
import com.example.app.data.DTOs.CarDTO;
import com.example.app.data.DTOs.RecentlyViewedCarDTO;
import com.example.app.data.entities.FavoriteVehiclesEntity;
import com.example.app.data.entities.RecentlyViewedToken;
import com.example.app.data.entities.User;
import com.example.app.services.FavoriteVehiclesService;
import com.example.app.services.RecentlyViewedTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ViewController {

    private final RecentlyViewedTokenService recentlyViewedTokenService;

    private final FavoriteVehiclesService favoriteVehiclesService;

    private final AuthTokenValidationUtil authTokenValidationUtil;

    public static final List<CarDTO> CAR_DTO_LIST = new ArrayList<>();

    static {
        CAR_DTO_LIST.add(new CarDTO("../images/Lamborghini-Urus-2022.png", "Lamborghini Urus 2022",
                "3D Models/Lamborghini-Urus-2022.glb", false));
        CAR_DTO_LIST.add(new CarDTO("../images/Porsche-Carrera-2015.png", "Porsche Carrera 2015",
                "3D Models/Porsche-Carrera-2015.glb", false));
        CAR_DTO_LIST.add(new CarDTO("../images/Lamborghini-Aventador-2020.png", "Lamborghini Aventador 2020",
                "3D Models/Lamborghini-Aventador-2020.glb", false));
        CAR_DTO_LIST.add(new CarDTO("../images/Lamborghini-Gallardo-2007.png", "Lamborghini Gallardo 2007",
                "3D Models/Lamborghini-Gallardo-2007.glb", false));
        CAR_DTO_LIST.add(new CarDTO("../images/Toyota-Supra-Gr-2020.png", "Toyota Supra Gr 2020",
                "3D Models/Toyota-Supra-Gr-2020.glb", false));
        CAR_DTO_LIST.add(new CarDTO("../images/Porsche-Boxster-2016.png", "Porsche Boxster 2016",
                "3D Models/Porsche-Boxster-2016.glb", false));
        CAR_DTO_LIST.add(new CarDTO("../images/BMW-X5-2022.png", "BMW X5 2022",
                "3D Models/BMW-X5-2022.glb", false));
        CAR_DTO_LIST.add(new CarDTO("../images/Mclaren-P1-2015.png", "McLaren P1 2015",
                "3D Models/Mclaren-P1-2015.glb", false));
        CAR_DTO_LIST.add(new CarDTO("../images/Tesla-Model-3-2020.png", "Tesla Model 3 2020",
                "3D Models/Tesla-Model-3-2020.glb", false));
    }

    public ViewController(RecentlyViewedTokenService recentlyViewedTokenService,
                          FavoriteVehiclesService favoriteVehiclesService,
                          AuthTokenValidationUtil authTokenValidationUtil) {
        this.recentlyViewedTokenService = recentlyViewedTokenService;
        this.favoriteVehiclesService = favoriteVehiclesService;
        this.authTokenValidationUtil = authTokenValidationUtil;
    }

    @GetMapping({"/", "index", "3D Models", "images", "js scripts", "css"})
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        String[] navHtmlAndToken = getNavigationHtml(authToken, authTokenValidationUtil, response);
        String navigationHtml = navHtmlAndToken[0];
        authToken = navHtmlAndToken[1];
        if (authToken != null) {
            return new ModelAndView("index")
                    .addObject("nav", navigationHtml)
                    .addObject("recentlyViewed", getRecentlyViewedHtml(authToken,
                            recentlyViewedTokenService,
                            favoriteVehiclesService.findByUser_Id(CookieUtils.getUserIdFromAuthToken(authToken))));
        }
        return new ModelAndView("index")
                .addObject("nav", navigationHtml);
    }

    @GetMapping("auto-show")
    public ModelAndView autoshow(HttpServletRequest request, HttpServletResponse response) {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        String[] navHtmlAndToken = getNavigationHtml(authToken, authTokenValidationUtil, response);
        String navigationHtml = navHtmlAndToken[0];
        authToken = navHtmlAndToken[1];
        if (authToken != null) {
            return new ModelAndView("auto-show")
                    .addObject("nav", navigationHtml)
                    .addObject("cars", getFavoriteCars(CookieUtils.getUserIdFromAuthToken(authToken)))
                    .addObject("recentlyViewed", getRecentlyViewedHtml(authToken,
                            recentlyViewedTokenService,
                            favoriteVehiclesService.findByUser_Id(CookieUtils.getUserIdFromAuthToken(authToken))));
        }
        return new ModelAndView("auto-show")
                .addObject("nav", navigationHtml)
                .addObject("cars", getFavoriteCars(0));
    }

    private List<CarDTO> getFavoriteCars(long userId) {
        if (userId != 0) {
            List<FavoriteVehiclesEntity> favCars = favoriteVehiclesService.findByUser_Id(userId);
            if (favCars.isEmpty()) {
                return CAR_DTO_LIST;
            } else  {
                List<CarDTO> carDTOS = CAR_DTO_LIST.stream().map(CarDTO::new).collect(Collectors.toList());
                Set<String> favoriteIds = favCars.stream()
                        .map(FavoriteVehiclesEntity::getVehicleId)
                        .collect(Collectors.toSet());

                for (CarDTO carDTO : carDTOS) {
                    if (favoriteIds.contains(carDTO.getCarModelPath())) {
                        carDTO.setInFavorites(true);
                    }
                }
                return carDTOS;
            }
        } else {
            return CAR_DTO_LIST;
        }
    }

    @GetMapping("newsletter-unsubscribe.html")
    public ModelAndView newsletterUnsubscribe(HttpServletRequest request, HttpServletResponse response) {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        String[] navHtmlAndToken = getNavigationHtml(authToken, authTokenValidationUtil, response);
        String navigationHtml = navHtmlAndToken[0];
        return new ModelAndView("newsletter-unsubscribe")
                .addObject("nav", navigationHtml);
    }

    @GetMapping("reset-password.html")
    public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response) {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        String[] navHtmlAndToken = getNavigationHtml(authToken, authTokenValidationUtil, response);
        String navigationHtml = navHtmlAndToken[0];
        return new ModelAndView("reset-password")
                .addObject("nav", navigationHtml);
    }

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpServletRequest request) {
        if (CookieUtils.getAuthTokenCookie(request) != null) {
            return "redirect:/index";
        }
        model.addAttribute("loginUser", new User());
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model, HttpServletRequest request) {
        if (CookieUtils.getAuthTokenCookie(request) != null) {
            return "redirect:/index";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    public static List<RecentlyViewedCarDTO> getRecentlyViewedHtml(String authToken,
                                                                   RecentlyViewedTokenService recentlyViewedTokenService,
                                                                   List<FavoriteVehiclesEntity> favoriteVehiclesEntities) {
        long id = CookieUtils.getUserIdFromAuthToken(authToken);
        Optional<RecentlyViewedToken> recentlyViewedTokenOptional = recentlyViewedTokenService.findByUser_Id(id);
        if (recentlyViewedTokenOptional.isPresent() &&
                !recentlyViewedTokenOptional.get().getRecentlyViewedCars().isEmpty()) {
            RecentlyViewedToken recentlyViewedToken = recentlyViewedTokenOptional.get();
            List<String> cars = new ArrayList<>(List.of(recentlyViewedToken.getRecentlyViewedCars().split(",")));
            Collections.reverse(cars);

            return cars.stream().map(car -> {
                        if (VehicleExistCheckUtil.doesItExist(car.split("3D Models/")[1]
                                .split("\\.")[0])) {
                            RecentlyViewedCarDTO recentlyViewedCarDTO = new RecentlyViewedCarDTO();
                            recentlyViewedCarDTO.setVehicleImg(car.split("3D Models/")[1]
                                    .split("\\.")[0]);
                            recentlyViewedCarDTO.setVehicleName(car.split("3D Models/")[1].
                                    replaceAll("-", " ")
                                    .split("\\.")[0]);
                            recentlyViewedCarDTO.setVehicleId(car);
                            String[] favsParts = isVehicleInFavs(favoriteVehiclesEntities, car);
                            recentlyViewedCarDTO.setCarFav(favsParts[0]);
                            recentlyViewedCarDTO.setCarFavInput(favsParts[1]);
                            return recentlyViewedCarDTO;
                        }
                        return null;
                    }).filter(Objects::nonNull).
                    collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private static String[] isVehicleInFavs(List<FavoriteVehiclesEntity> favoriteVehiclesEntities, String car) {
        for (FavoriteVehiclesEntity favoriteVehiclesEntity : favoriteVehiclesEntities) {
            if (favoriteVehiclesEntity.getVehicleId().contains(car)) {
                return new String[]{"Remove from Favorites", "true"};
            }
        }
        return new String[]{"Add to Favorites", "false"};
    }

    public static String[] getNavigationHtml(String authToken, AuthTokenValidationUtil authTokenValidationUtil, HttpServletResponse response) {
        long userId = CookieUtils.getUserIdFromAuthToken(authToken);
        if (authToken != null && authTokenValidationUtil.
                isAuthTokenValid(userId, authToken, response)) {
            return new String[]{"""
                    <header class="nav-header" id="header">
                    <a href="/index" class="logo">
                            <img class="logo-img" src="../images/logo.png" alt="Logo">
                        </a>
                        <div class="h1">
                            <h1>Where Luxury Meets Performance</h1>
                        </div>
                        <div class="container">
                            <button class="hamburger" id="hamburgerButton">
                                <i class="fas fa-bars"></i>
                                <span class="menuText">Menu</span>
                            </button>
                            <ul class="menu" id="menu">
                                <li><a class="menuItem" href="/index" onclick="setHamburgerRelative(event)"><i
                                        class="fas fa-home"></i>
                                    Home</a></li>
                                <li id="profile"><a class="menuItem"
                                                                           href="/profile"
                                                                           onclick="setHamburgerRelative(event)"><i class="fas fa-user"></i>Profile</a>
                                </li>
                                <li><a class="menuItem" href="/auto-show" onclick="setHamburgerRelative(event)"><i
                                        class="fas fa-car"></i>
                                    Auto
                                    Show</a></li>
                                <li style="display: none;" id="log-in-text"><a class="menuItem" href="/login"
                                                        onclick="setHamburgerRelative(event)"><i class="fas fa-sign-in-alt"></i>Log in</a>
                                </li>
                                <li id="log-out-text" ><a class="menuItem" href="#"
                                                                                onclick="setHamburgerRelative(event)"><i
                                        class="fas fa-sign-in-alt"></i>Log out</a></li>
                                <li><a class="menuItem" href="#" onclick="setHamburgerRelative(event)"><i
                                        class="fas fa-info-circle"></i> About</a></li>
                            </ul>
                        </div>
                        <nav class="navs">
                            <nav>
                                <ul>
                                    <li><a href="/index"><i class="fas fa-home"></i></a></li>
                                    <li id="profile"><a
                                            href="/profile"><i
                                            class="fas fa-user"></i></a></li>
                                    <li><a href="/auto-show"><i class="fas fa-car"></i></a></li>
                                    <li style="display: none;" id="log-in-icon"><a href="/login"><i
                                            class="fas fa-sign-in-alt"></i></a></li>
                                    <li id="log-out-icon"><a href="#"><i class="fas fa-sign-out-alt"></i></a>
                                    </li>
                                    <li><a href="#"><i class="fas fa-info-circle"></i></a></li>
                                </ul>
                            </nav>
                            <nav>
                                <ul id="nav-text">
                                    <li><a href="/index">Home</a></li>
                                    <li id="profile"><a
                                            href="/profile">Profile</a></li>
                                    <li class="auto-show"><a href="/auto-show">Auto Show</a></li>
                                    <li style="display: none;" id="log-in-text"><a href="/login">Log in</a>
                                    </li>
                                    <li id="log-out-text"><a href="#">Log out</a></li>
                                    <li><a href="#">About</a></li>
                                </ul>
                            </nav>
                        </nav>
                        </header>
                    """, authTokenValidationUtil.getValidAuthToken(userId)};
        } else {
            return new String[]{"""
                          <header class="nav-header" id="header">
                         <a href="/index" class="logo">
                                <img class="logo-img" src="../images/logo.png" alt="Logo">
                            </a>
                            <div class="h1">
                                <h1>Where Luxury Meets Performance</h1>
                            </div>
                            <div class="container">
                                <button class="hamburger" id="hamburgerButton">
                                    <i class="fas fa-bars"></i>
                                    <span class="menuText">Menu</span>
                                </button>
                                <ul class="menu" id="menu">
                                    <li><a class="menuItem" href="/index" onclick="setHamburgerRelative(event)"><i
                                            class="fas fa-home"></i>
                                        Home</a></li>
                                    <li style="display: none;" id="profile"><a class="menuItem"
                                                                               href="/profile"
                                                                               onclick="setHamburgerRelative(event)"><i class="fas fa-user"></i>Profile</a>
                                    </li>
                                    <li><a class="menuItem" href="/auto-show" onclick="setHamburgerRelative(event)"><i
                                            class="fas fa-car"></i>
                                        Auto
                                        Show</a></li>
                                    <li id="log-in-text"><a class="menuItem" href="/login"
                                                            onclick="setHamburgerRelative(event)"><i class="fas fa-sign-in-alt"></i>Log in</a>
                                    </li>
                                    <li style="display: none;" id="log-out-text"><a class="menuItem" href="#"
                                                                                    onclick="setHamburgerRelative(event)"><i
                                            class="fas fa-sign-in-alt"></i>Log out</a></li>
                                    <li><a class="menuItem" href="#" onclick="setHamburgerRelative(event)"><i
                                            class="fas fa-info-circle"></i> About</a></li>
                                </ul>
                            </div>
                            <nav class="navs">
                                <nav>
                                    <ul>
                                        <li><a href="/index"><i class="fas fa-home"></i></a></li>
                                        <li style="display: none;" id="profile"><a
                                                href="/profile"><i
                                                class="fas fa-user"></i></a></li>
                                        <li><a href="/auto-show"><i class="fas fa-car"></i></a></li>
                                        <li id="log-in-icon"><a href="/login"><i
                                                class="fas fa-sign-in-alt"></i></a></li>
                                        <li style="display: none;" id="log-out-icon"><a href="#"><i class="fas fa-sign-out-alt"></i></a>
                                        </li>
                                        <li><a href="#"><i class="fas fa-info-circle"></i></a></li>
                                    </ul>
                                </nav>
                                <nav>
                                    <ul id="nav-text">
                                        <li><a href="/index">Home</a></li>
                                        <li style="display: none;" id="profile"><a
                                                href="/profile">Profile</a></li>
                                        <li class="auto-show"><a href="/auto-show">Auto Show</a></li>
                                        <li id="log-in-text"><a href="/login">Log in</a>
                                        </li>
                                        <li style="display: none;" id="log-out-text"><a href="#">Log out</a></li>
                                        <li><a href="#">About</a></li>
                                    </ul>
                                </nav>
                            </nav>
                            </header>
                    """, ""};
        }
    }
}
