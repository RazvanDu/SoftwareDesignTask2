package com.RazvanDu.project.controller;

import com.RazvanDu.project.model.Utils;
import com.RazvanDu.project.model.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RestaurantController {

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private FoodRepository foodRepository;
    private OrderRespository orderRespository;

    public RestaurantController(UserRepository userRepository, RestaurantRepository restaurantRepository, FoodRepository foodRepository, OrderRespository orderRespository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
        this.orderRespository = orderRespository;
    }

    @RequestMapping(value = "/database/foodsByRestaurant/{id}")
    public ResponseEntity<List<Food>> foodsById(@PathVariable Integer id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if(restaurant.isPresent())
            return ResponseEntity.ok(restaurant.get().getFoods());
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/database/restaurants/all")
    public ResponseEntity<Iterable<Restaurant>> restaurantsAll() {
        Iterable<Restaurant> restaurants = restaurantRepository.findAll();
        return ResponseEntity.ok(restaurants);
    }

    @RequestMapping(value = "/database/restaurants/allFilter/{name}")
    public ResponseEntity<Iterable<Restaurant>> restaurantsFilter(@PathVariable String name) {
        Iterable<Restaurant> restaurants = restaurantRepository.findAll();

        if(name.equals("-1"))
            return ResponseEntity.ok(restaurants);

        List<Restaurant> result = new ArrayList<>();

        for(Restaurant restaurant : restaurants) {
            if(restaurant.getName().replaceAll(" ", "").toLowerCase().contains(name.toLowerCase().replaceAll(" ", "")))
                result.add(restaurant);
        }

        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/database/restaurants/byId/{id}")
    public ResponseEntity<Restaurant> restaurantsById(@PathVariable Integer id) {
        Restaurant restaurant = restaurantRepository.findById(id).get();
        return ResponseEntity.ok(restaurant);
    }

    @RequestMapping(value = "/database/addRestaurant", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addRestaurant(@RequestBody Restaurant restaurant, HttpServletRequest request, HttpServletResponse response) {
        if(!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();
        long id = Utils.loggedUsers.get(request.getSession()).getId();
        restaurant.setAdminID((int) id);
        restaurantRepository.save(restaurant);
        return ResponseEntity.accepted().build();
    }

}
