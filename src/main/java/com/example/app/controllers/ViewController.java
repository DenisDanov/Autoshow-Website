package com.example.app.controllers;

import com.example.app.controllers.utils.AuthTokenValidationUtil;
import com.example.app.controllers.utils.CookieUtils;
import com.example.app.controllers.utils.VehicleExistCheckUtil;
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

@Controller
public class ViewController {

    private final RecentlyViewedTokenService recentlyViewedTokenService;

    private final FavoriteVehiclesService favoriteVehiclesService;

    private final AuthTokenValidationUtil authTokenValidationUtil;

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
        String navigationHtml = getNavigationHtml(authToken, authTokenValidationUtil, response);
        if (authToken != null) {
            return new ModelAndView("index")
                    .addObject("nav", navigationHtml)
                    .addObject("recentlyViewed", getRecentlyViewedHtml(authToken, recentlyViewedTokenService,
                            favoriteVehiclesService, authTokenValidationUtil));
        }
        return new ModelAndView("index")
                .addObject("nav", navigationHtml);
    }

    @GetMapping("auto-show")
    public ModelAndView autoshow(HttpServletRequest request, HttpServletResponse response) {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        String navigationHtml = getNavigationHtml(authToken, authTokenValidationUtil, response);
        if (authToken != null) {
            return new ModelAndView("auto-show")
                    .addObject("nav", navigationHtml)
                    .addObject("recentlyViewed", getRecentlyViewedHtml(authToken,
                            recentlyViewedTokenService,
                            favoriteVehiclesService,
                            authTokenValidationUtil))
                    .addObject("favsMap",
                            autoShowCarsHtml(CookieUtils.getUserIdFromAuthToken(authToken)));
        }
        return new ModelAndView("auto-show")
                .addObject("nav", navigationHtml)
                .addObject("favsMap", new HashMap<String, String>());
    }

    private Map<String, String> autoShowCarsHtml(long userId) {
        Map<String, String> favVehiclesMap = new HashMap<>();
        this.favoriteVehiclesService.findByUser_Id(userId).forEach(favVehicle -> {
            favVehiclesMap.put(favVehicle.getVehicleName(), "Remove From Favorites,true");
        });
        return favVehiclesMap;
    }

    @GetMapping("newsletter-unsubscribe.html")
    public ModelAndView newsletterUnsubscribe(HttpServletRequest request, HttpServletResponse response) {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        String navigationHtml = getNavigationHtml(authToken, authTokenValidationUtil, response);
        return new ModelAndView("newsletter-unsubscribe")
                .addObject("nav", navigationHtml);
    }

    @GetMapping("reset-password.html")
    public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response) {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        String navigationHtml = getNavigationHtml(authToken, authTokenValidationUtil, response);
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

    public static String getRecentlyViewedHtml(String authToken,
                                               RecentlyViewedTokenService recentlyViewedTokenService,
                                               FavoriteVehiclesService favoriteVehiclesService,
                                               AuthTokenValidationUtil authTokenValidationUtil) {
        long id = CookieUtils.getUserIdFromAuthToken(authToken);
        Optional<RecentlyViewedToken> recentlyViewedTokenOptional = recentlyViewedTokenService.findByUser_Id(id);
        if (recentlyViewedTokenOptional.isPresent() &&
                !recentlyViewedTokenOptional.get().getRecentlyViewedCars().isEmpty()) {
            RecentlyViewedToken recentlyViewedToken = recentlyViewedTokenOptional.get();
            StringBuilder fullContainer = new StringBuilder();
            List<String> cars = new ArrayList<>(List.of(recentlyViewedToken.getRecentlyViewedCars().split(",")));
            Collections.reverse(cars);
            for (String car : cars) {
                if (VehicleExistCheckUtil.doesItExist(car.split("3D Models/")[1]
                        .split("\\.")[0])) {
                    List<String> isVehicleFav = isVehicleInFavs(car, id, favoriteVehiclesService);
                    fullContainer.append("""
                            <div class="car-card">
                                   <div class="img-container">
                                                      <img src="../images/%s.png" alt="Car 2">
                                                  </div>
                                                  <div class="car-info">
                                                      <h3>%s</h3>
                                                  </div>
                                                  <div class="favorites">
                                                  <h3>%s</h3>
                                                  <label class="add-fav">
                                                      <input class="%s" %s type="checkbox" />
                                                      <i class="icon-heart fas fa-heart">
                                                          <i class="icon-plus-sign fa-solid fa-plus"></i>
                                                      </i>
                                                  </label>
                                              </div>
                                                  <a href="showroom.html?car=%s" class="view-button">View in
                                                      Showroom</a>
                                   </div>
                                  """
                            .formatted(car.split("3D Models/")[1]
                                            .split("\\.")[0],
                                    car.split("3D Models/")[1].replaceAll("-", " ")
                                            .split("\\.")[0],
                                    isVehicleFav.get(0),
                                    isVehicleFav.get(1).split("=")[0],
                                    isVehicleFav.get(1),
                                    car));
                }
            }
            return fullContainer.toString();
        } else {
            return "";
        }
    }

    private static List<String> isVehicleInFavs(String car, long userId, FavoriteVehiclesService favoriteVehiclesService) {
        for (FavoriteVehiclesEntity favVehicle : favoriteVehiclesService.findByUser_Id(userId)) {
            if (favVehicle.getVehicleId().contains(car)) {
                return List.of("Remove from Favorites", """
                        checked="true"
                        """);
            }
        }
        return List.of("Add to Favorites", "");
    }

    public static String getNavigationHtml(String authToken, AuthTokenValidationUtil authTokenValidationUtil, HttpServletResponse response) {
        if (authToken != null && authTokenValidationUtil.
                isAuthTokenValid(CookieUtils.getUserIdFromAuthToken(authToken), authToken, response)) {
            return """
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
                    """;
        } else {
            return """
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
                    """;
        }
    }
}
