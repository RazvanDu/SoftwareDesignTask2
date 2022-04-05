/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.RazvanDu.project.controller;

import com.RazvanDu.project.ReactAndSpringDataRestApplication;
import com.RazvanDu.project.model.*;
import org.apache.coyote.Response;
import org.hibernate.Session;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class HomeController {

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private FoodRepository foodRepository;

    public HomeController(UserRepository userRepository, RestaurantRepository restaurantRepository, FoodRepository foodRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
    }

	@RequestMapping(value = "/")
	public String index(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("On Index");

        String logStatus = "You are not logged in!";

        if(ReactAndSpringDataRestApplication.loggedUsers.containsKey(request.getSession()))
            logStatus = "Hello " + ReactAndSpringDataRestApplication.loggedUsers.get(request.getSession()) + "!";

	    return "index";
	}

    @RequestMapping(value = "/database/loggedUser")
    public ResponseEntity<User> loggedUser(HttpServletRequest request, HttpServletResponse response) {

        User user = null;

        if(ReactAndSpringDataRestApplication.loggedUsers.containsKey(request.getSession()))
            user = ReactAndSpringDataRestApplication.loggedUsers.get(request.getSession());

        System.out.println("... " + user);

        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/logout")
    public ResponseEntity<User> logout(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("LOGGING OUT!");

        if(!ReactAndSpringDataRestApplication.loggedUsers.containsKey(request.getSession())) {
            System.out.println("CANNOT DELETE");
            return ResponseEntity.accepted().build();
        }

        ReactAndSpringDataRestApplication.loggedUsers.remove(request.getSession());
        return ResponseEntity.accepted().build();

    }

    @RequestMapping(path="/database/signupUser", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    //public ResponseEntity<User> newUser(@RequestBody String name, @RequestBody String hash, @RequestBody String email) {
    public ResponseEntity<User> newUser(@RequestBody User newUser) {

        //System.out.println(newUser);

        newUser.setType(0);

        if(userRepository.findByName(newUser.getName()).isPresent())
            return ResponseEntity.badRequest().build();

        System.out.println("FIRST " + newUser.getName() + " + " + newUser.getHash() + " + " + newUser.getEmail());

        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(newUser.getEmail());

        if(!mat.matches())
            return ResponseEntity.badRequest().build();

        System.out.println("SECOND");

        return ResponseEntity.ok(userRepository.save(newUser));

    }

	@RequestMapping(value = "/database/users/findByName/{name}")
	public ResponseEntity<User> userByName(@PathVariable String name) {
        Optional<User> user = userRepository.findByName(name);
        if(user.isPresent())
            return ResponseEntity.ok(user.get());
        return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/database/foodsByRestaurant/{id}")
	public ResponseEntity<List<Food>> foodsById(@PathVariable Integer id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if(restaurant.isPresent())
            return ResponseEntity.ok(restaurant.get().getFoods());
        return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/database/getCart")
	public ResponseEntity<List<Food>> foodsById(HttpServletRequest request, HttpServletResponse response) {
        if(!ReactAndSpringDataRestApplication.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();
        long id = ReactAndSpringDataRestApplication.loggedUsers.get(request.getSession()).getId();
        if(ReactAndSpringDataRestApplication.cart.containsKey(id))
            return ResponseEntity.ok(ReactAndSpringDataRestApplication.cart.get(id));
        return ResponseEntity.ok(new ArrayList<>());
	}

	@RequestMapping(value = "/database/addCart", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Food>> addToCart(@RequestBody Food food, HttpServletRequest request, HttpServletResponse response) {
        if(!ReactAndSpringDataRestApplication.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();
        long id = ReactAndSpringDataRestApplication.loggedUsers.get(request.getSession()).getId();
        if(!ReactAndSpringDataRestApplication.cart.containsKey(id))
            ReactAndSpringDataRestApplication.cart.put((id), new ArrayList<>());
        //Optional<Food> food = foodRepository.findById(foodId);
        //if(!food.isPresent())
        //    return ResponseEntity.badRequest().build();
        ReactAndSpringDataRestApplication.cart.get(id).add(food);
        return ResponseEntity.accepted().build();
	}

	@RequestMapping(value = "/database/addOrdering", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Food>> addOrdering(@RequestBody Restaurant restaurant, HttpServletRequest request, HttpServletResponse response) {
        if(!ReactAndSpringDataRestApplication.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();
        long id = ReactAndSpringDataRestApplication.loggedUsers.get(request.getSession()).getId();
        ReactAndSpringDataRestApplication.ordering.put(id, restaurant.getId());
        System.out.println("Ordering from " + restaurant.getName() + "!");
        return ResponseEntity.accepted().build();
	}

	@RequestMapping(value = "/database/getOrdering")
	public ResponseEntity<Map<String, Integer>> orderingTarget(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Integer> mapp = new HashMap<>();
        mapp.put("id", -1);
        if(!ReactAndSpringDataRestApplication.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.ok(mapp);
        long id = ReactAndSpringDataRestApplication.loggedUsers.get(request.getSession()).getId();
        if(!ReactAndSpringDataRestApplication.ordering.containsKey(id))
            return ResponseEntity.ok(mapp);
        mapp.put("id", ReactAndSpringDataRestApplication.ordering.get(id));
        return ResponseEntity.ok(mapp);
	}

	@RequestMapping(value = "/database/restaurants/all")
	public ResponseEntity<Iterable<Restaurant>> restaurants() {
        Iterable<Restaurant> restaurants = restaurantRepository.findAll();
        return ResponseEntity.ok(restaurants);
	}

	@RequestMapping(value = "/database/restaurants/allFilter/{name}")
	public ResponseEntity<Iterable<Restaurant>> restaurants(@PathVariable String name) {
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

	@RequestMapping(value = "/database/isLoggedIn")
	public ResponseEntity<String> isLoggedIn(HttpServletRequest request, HttpServletResponse response) {
        if(ReactAndSpringDataRestApplication.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.accepted().build();
        return ResponseEntity.badRequest().build();
	}

	@RequestMapping(value = "/database/foods/all")
	public ResponseEntity<Iterable<Food>> foods() {
        Iterable<Food> foods = foodRepository.findAll();
        return ResponseEntity.ok(foods);
	}

	@RequestMapping(value = "/database/users/checkAndLogin/{name}/{pass}")
	public ResponseEntity<User> checkAndLogin(@PathVariable String name, @PathVariable String pass, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user = userRepository.findByName(name);
        if(user.isPresent()) {
            if (user.get().getHash().equals(pass)) {
                //Session session = ReactAndSpringDataRestApplication.sessionFactory.openSession();
                ReactAndSpringDataRestApplication.loggedUsers.put(request.getSession(), user.get());
                return ResponseEntity.ok(user.get());
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/dashboard")
	public String dashboard() {
        System.out.println("On Dashboard");
	    return "index";
	}

	@RequestMapping(value = "/preferences")
	public String preferences() {
        System.out.println("On Preferences");
	    return "index";
	}

	@RequestMapping(value = "/login")
	public String login() {
        System.out.println("On Login");
	    return "index";
	}

	@RequestMapping(value = "/signup")
	public String signup() {
        System.out.println("On Signup");
	    return "index";
	}

}
