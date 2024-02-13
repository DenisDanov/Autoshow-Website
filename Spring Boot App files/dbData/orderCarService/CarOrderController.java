package com.example.demo.dbData.orderCarService;

import com.example.demo.dbData.AuthenticationToken;
import com.example.demo.dbData.AuthenticationTokensRepository;
import com.example.demo.dbData.ReplacedTokens.ReplacedAuthTokensRepo;
import com.example.demo.dbData.User;
import com.example.demo.dbData.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/carOrders")
public class CarOrderController {

    private final UserRepository userRepository;

    private final CarOrdersRepository carOrdersRepository;

    private final AuthenticationTokensRepository authenticationTokensRepository;

    private final ReplacedAuthTokensRepo replacedAuthTokensRepo;

    @Autowired
    public CarOrderController(UserRepository userRepository,
                              CarOrdersRepository carOrdersRepository,
                              AuthenticationTokensRepository authenticationTokensRepository,
                              ReplacedAuthTokensRepo replacedAuthTokensRepo) {
        this.userRepository = userRepository;
        this.carOrdersRepository = carOrdersRepository;
        this.authenticationTokensRepository = authenticationTokensRepository;
        this.replacedAuthTokensRepo = replacedAuthTokensRepo;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@RequestBody CarOrderRequest request,
                                           HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userRepository.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokensRepository.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            User user = userOptional.get();

            CarOrder carOrder = new CarOrder(request.getCarManufacturer(),
                    request.getCarModel(),
                    request.getCarYear());

            List<CarOrdersEntity> carOrders = carOrdersRepository.findByUser_Id(userId);
            if (!carOrders.isEmpty()) {
                if (carOrders.stream().noneMatch(carOrder1 ->
                        carOrder1.getCarManufacturer().equals(carOrder.getCarManufacturer()) &&
                                carOrder1.getCarModel().equals(carOrder.getCarModel()) &&
                                carOrder1.getCarYear() == (Integer.parseInt(carOrder.getCarYear())))) {
                    carOrdersRepository.save(new CarOrdersEntity(user, carOrder.getCarManufacturer(),
                            carOrder.getCarModel(), Integer.parseInt(carOrder.getCarYear()), carOrder.getOrderStatus(),
                            carOrder.getDateOfOrder()));
                    userRepository.save(user);
                    return ResponseEntity.ok("Order sent successfully.");
                } else {
                    return ResponseEntity.ok("Car order already exists.");
                }
            } else {
                carOrdersRepository.save(new CarOrdersEntity(user, carOrder.getCarManufacturer(),
                        carOrder.getCarModel(), Integer.parseInt(carOrder.getCarYear()), carOrder.getOrderStatus(),
                        carOrder.getDateOfOrder()));
                return ResponseEntity.ok("Order sent successfully.");
            }
        } else {
            authenticationToken = authenticationTokensRepository.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensRepo.findByReplacedToken(request.getAuthToken()) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site
                cookie.setSecure(true);
                cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");

                response.addCookie(cookie);
                replacedAuthTokensRepo.deleteByReplacedToken(request.getAuthToken());
                User user = userOptional.get();

                CarOrder carOrder = new CarOrder(request.getCarManufacturer(),
                        request.getCarModel(),
                        request.getCarYear());

                List<CarOrdersEntity> carOrders = carOrdersRepository.findByUser_Id(userId);
                if (!carOrders.isEmpty()) {
                    if (carOrders.stream().noneMatch(carOrder1 ->
                            carOrder1.getCarManufacturer().equals(carOrder.getCarManufacturer()) &&
                                    carOrder1.getCarModel().equals(carOrder.getCarModel()) &&
                                    carOrder1.getCarYear() == (Integer.parseInt(carOrder.getCarYear())))) {
                        carOrdersRepository.save(new CarOrdersEntity(user, carOrder.getCarManufacturer(),
                                carOrder.getCarModel(), Integer.parseInt(carOrder.getCarYear()), carOrder.getOrderStatus(),
                                carOrder.getDateOfOrder()));
                        userRepository.save(user);
                        return ResponseEntity.ok("Order sent successfully.");
                    } else {
                        return ResponseEntity.ok("Car order already exists.");
                    }
                } else {
                    carOrdersRepository.save(new CarOrdersEntity(user, carOrder.getCarManufacturer(),
                            carOrder.getCarModel(), Integer.parseInt(carOrder.getCarYear()), carOrder.getOrderStatus(),
                            carOrder.getDateOfOrder()));
                    return ResponseEntity.ok("Order sent successfully.");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // TODO make the request need auth token
    @GetMapping("/get")
    public ResponseEntity<List<CarOrder>> getOrders(@RequestParam("id") Long userId,
                                                    HttpServletResponse response) {
        // Find the user by ID
        Optional<User> userOptional = userRepository.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokensRepository.findByUser_Id(userId);
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            List<CarOrder> getAllOrders = carOrdersRepository
                    .findByUser_Id(userId)
                    .stream()
                    .map(carOrder -> new CarOrder(
                            carOrder.getCarManufacturer(),
                            carOrder.getCarModel(),
                            String.valueOf(carOrder.getCarYear()),
                            carOrder.getOrderStatus(),
                            (Date) carOrder.getDateOfOrder()))
                    .toList();
            return ResponseEntity.ok(getAllOrders);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCarOrder(@RequestBody RemoveCarOrderRequest request,
                                                 HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userRepository.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokensRepository.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            if (carOrdersRepository.deleteCarOrder(request.getCarManufacturer(),
                    userId,
                    request.getCarModel(),
                    request.getCarYear()) > 0) {
                return ResponseEntity.ok("Car order successfully removed.");
            } else {
                return ResponseEntity.ok("Car order doesn't exist.");
            }
        } else {
            authenticationToken = authenticationTokensRepository.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensRepo.findByReplacedToken(request.getAuthToken()) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site
                cookie.setSecure(true);
                cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");

                response.addCookie(cookie);
                replacedAuthTokensRepo.deleteByReplacedToken(request.getAuthToken());
                if (carOrdersRepository.deleteCarOrder(request.getCarManufacturer(),
                        userId,
                        request.getCarModel(),
                        request.getCarYear()) > 0) {
                    return ResponseEntity.ok("Car order successfully removed.");
                } else {
                    return ResponseEntity.ok("Car order doesn't exist.");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PutMapping("/modify")
    public ResponseEntity<ModifyCarOrderResponse> modifyCarOrder(@RequestBody ModifyCarOrder request,
                                                                 HttpServletResponse response) {
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userRepository.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokensRepository.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            if (carOrdersRepository.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                    request.getCurrentManufacturer(),
                    request.getCurrentModel(),
                    Integer.parseInt(request.getCurrentYear())).isPresent()) {

                CarOrdersEntity carOrder = carOrdersRepository.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                        request.getCurrentManufacturer(),
                        request.getCurrentModel(),
                        Integer.parseInt(request.getCurrentYear())).get();

                if (carOrder.getCarManufacturer().equals(request.getNewManufacturer()) &&
                        carOrder.getCarModel().equals(request.getNewModel()) &&
                        carOrder.getCarYear() == Integer.parseInt(request.getNewYear())) {
                    carOrder.setDateOfOrder(Date.valueOf(String.valueOf(LocalDate.now())));
                    carOrdersRepository.save(carOrder);
                    return ResponseEntity.ok(new ModifyCarOrderResponse("Same order is made and the period is extended.",
                            carOrder.getCarManufacturer(),
                            carOrder.getCarModel(),
                            String.valueOf(carOrder.getCarYear()),
                            String.valueOf(carOrder.getDateOfOrder()),
                            carOrder.getOrderStatus()));
                } else {
                    List<CarOrdersEntity> carOrders = carOrdersRepository.findByUser_Id(userId);
                    if (carOrders.stream().noneMatch(carOrder1 ->
                            carOrder1.getCarManufacturer().equals(request.getNewManufacturer()) &&
                                    carOrder1.getCarModel().equals(request.getNewModel()) &&
                                    carOrder1.getCarYear() == Integer.parseInt(request.getNewYear()))) {

                        carOrder.setCarModel(request.getNewModel());
                        carOrder.setCarManufacturer(request.getNewManufacturer());
                        carOrder.setCarYear(Integer.parseInt(request.getNewYear()));
                        carOrder.setDateOfOrder(Date.valueOf(String.valueOf(LocalDate.now())));
                        carOrdersRepository.save(carOrder);

                        return ResponseEntity.ok(new ModifyCarOrderResponse("New order is made and the period is extended.",
                                carOrder.getCarManufacturer(),
                                carOrder.getCarModel(),
                                String.valueOf(carOrder.getCarYear()),
                                String.valueOf(carOrder.getDateOfOrder()),
                                carOrder.getOrderStatus()));
                    } else {
                        return ResponseEntity.ok(new ModifyCarOrderResponse("An order matching your request already exists.",
                                "",
                                "",
                                "",
                                "",
                                ""));
                    }
                }
            } else {
                return ResponseEntity.ok(new ModifyCarOrderResponse("Order is not found.",
                        "",
                        "",
                        "",
                        "",
                        ""));
            }
        } else {
            authenticationToken = authenticationTokensRepository.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensRepo.findByReplacedToken(request.getAuthToken()) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site
                cookie.setSecure(true);
                cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");

                response.addCookie(cookie);
                replacedAuthTokensRepo.deleteByReplacedToken(request.getAuthToken());
                if (carOrdersRepository.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                        request.getCurrentManufacturer(),
                        request.getCurrentModel(),
                        Integer.parseInt(request.getCurrentYear())).isPresent()) {

                    CarOrdersEntity carOrder = carOrdersRepository.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                            request.getCurrentManufacturer(),
                            request.getCurrentModel(),
                            Integer.parseInt(request.getCurrentYear())).get();

                    if (carOrder.getCarManufacturer().equals(request.getNewManufacturer()) &&
                            carOrder.getCarModel().equals(request.getNewModel()) &&
                            carOrder.getCarYear() == Integer.parseInt(request.getNewYear())) {
                        carOrder.setDateOfOrder(Date.valueOf(String.valueOf(LocalDate.now())));
                        carOrdersRepository.save(carOrder);
                        return ResponseEntity.ok(new ModifyCarOrderResponse("Same order is made and the period is extended.",
                                carOrder.getCarManufacturer(),
                                carOrder.getCarModel(),
                                String.valueOf(carOrder.getCarYear()),
                                String.valueOf(carOrder.getDateOfOrder()),
                                carOrder.getOrderStatus()));
                    } else {
                        List<CarOrdersEntity> carOrders = carOrdersRepository.findByUser_Id(userId);
                        if (carOrders.stream().noneMatch(carOrder1 ->
                                carOrder1.getCarManufacturer().equals(request.getNewManufacturer()) &&
                                        carOrder1.getCarModel().equals(request.getNewModel()) &&
                                        carOrder1.getCarYear() == Integer.parseInt(request.getNewYear()))) {

                            carOrder.setCarModel(request.getNewModel());
                            carOrder.setCarManufacturer(request.getNewManufacturer());
                            carOrder.setCarYear(Integer.parseInt(request.getNewYear()));
                            carOrder.setDateOfOrder(Date.valueOf(String.valueOf(LocalDate.now())));
                            carOrdersRepository.save(carOrder);

                            return ResponseEntity.ok(new ModifyCarOrderResponse("New order is made and the period is extended.",
                                    carOrder.getCarManufacturer(),
                                    carOrder.getCarModel(),
                                    String.valueOf(carOrder.getCarYear()),
                                    String.valueOf(carOrder.getDateOfOrder()),
                                    carOrder.getOrderStatus()));
                        } else {
                            return ResponseEntity.ok(new ModifyCarOrderResponse("An order matching your request already exists.",
                                    "",
                                    "",
                                    "",
                                    "",
                                    ""));
                        }
                    }
                } else {
                    return ResponseEntity.ok(new ModifyCarOrderResponse("Order is not found.",
                            "",
                            "",
                            "",
                            "",
                            ""));
                }
            }
            return ResponseEntity.ok(new ModifyCarOrderResponse("User is not found.",
                    "",
                    "",
                    "",
                    "",
                    ""));
        }
    }
}
