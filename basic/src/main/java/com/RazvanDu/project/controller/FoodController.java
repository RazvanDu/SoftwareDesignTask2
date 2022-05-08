package com.RazvanDu.project.controller;

import com.RazvanDu.project.model.*;
import com.sun.istack.logging.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * The type Food controller.
 */
@Controller
public class FoodController {

    private RestaurantRepository restaurantRepository;
    private FoodRepository foodRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Instantiates a new Food controller.
     *
     * @param restaurantRepository the restaurant repository
     * @param foodRepository       the food repository
     */
    public FoodController(RestaurantRepository restaurantRepository, FoodRepository foodRepository) {

        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;

    }

    /**
     * Foods response entity.
     *
     * @return the response entity
     */
    @RequestMapping(value = "/database/foods/all")
    public ResponseEntity<Iterable<Food>> foods() {

        Iterable<Food> foods = foodRepository.findAll();
        return ResponseEntity.ok(foods);

    }

    /**
     * Add food response entity.
     *
     * @param food    the food
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/addFood", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addFood(@RequestBody Food food, HttpServletRequest request) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        long id = Utils.loggedUsers.get(request.getSession()).getId();
        Restaurant target = restaurantRepository.findByAdminID((int) id).get();
        food.setRestaurantID(target.getId());
        foodRepository.save(food);
        logger.info("A new food with the name " + food.getName() + " has been added to restaurant " +
                target.getName() + "!");
        return ResponseEntity.accepted().build();

    }

}
