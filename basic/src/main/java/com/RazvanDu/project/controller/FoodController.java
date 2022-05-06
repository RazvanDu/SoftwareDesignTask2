package com.RazvanDu.project.controller;

import com.RazvanDu.project.model.Utils;
import com.RazvanDu.project.model.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class FoodController {

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private FoodRepository foodRepository;
    private OrderRespository orderRespository;

    public FoodController(UserRepository userRepository, RestaurantRepository restaurantRepository, FoodRepository foodRepository, OrderRespository orderRespository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
        this.orderRespository = orderRespository;
    }

    @RequestMapping(value = "/database/foods/all")
    public ResponseEntity<Iterable<Food>> foods() {
        Iterable<Food> foods = foodRepository.findAll();
        return ResponseEntity.ok(foods);
    }

    @RequestMapping(value = "/database/addFood", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addFood(@RequestBody Food food, HttpServletRequest request, HttpServletResponse response) {
        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();
        long id = Utils.loggedUsers.get(request.getSession()).getId();
        Restaurant target = restaurantRepository.findByAdminID((int) id).get();
        food.setRestaurantID(target.getId());
        foodRepository.save(food);
        return ResponseEntity.accepted().build();
    }

}
