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
import com.RazvanDu.project.model.Restaurant;
import com.RazvanDu.project.model.RestaurantRepository;
import com.RazvanDu.project.model.User;
import com.RazvanDu.project.model.UserRepository;
import org.apache.coyote.Response;
import org.hibernate.Session;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Controller

public class HomeController {

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    public HomeController(UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
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

	@RequestMapping(value = "/database/restaurants/all")
	public ResponseEntity<Iterable<Restaurant>> restaurants() {
        Iterable<Restaurant> restaurants = restaurantRepository.findAll();
        return ResponseEntity.ok(restaurants);
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
// end::code[]
