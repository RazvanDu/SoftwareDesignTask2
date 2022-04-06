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

import com.RazvanDu.project.model.*;
import org.apache.coyote.Response;
import org.hibernate.Session;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.BorderUIResource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class HomeController {

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private FoodRepository foodRepository;
    private OrderRespository orderRespository;

    public HomeController(UserRepository userRepository, RestaurantRepository restaurantRepository, FoodRepository foodRepository, OrderRespository orderRespository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
        this.orderRespository = orderRespository;
    }

	@RequestMapping(value = "/")
	public String index() {
	    return "index";
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

	@RequestMapping(value = "/cart")
	public String cart() {
        System.out.println("On Cart");
	    return "index";
	}

	@RequestMapping(value = "/order")
	public String orderr() {
        System.out.println("On Order");
	    return "index";
	}

	@RequestMapping(value = "/addFood")
	public String addFood() {
        System.out.println("On Add Food");
	    return "index";
	}

	@RequestMapping(value = "/manageOrders")
	public String manageOrders() {
        System.out.println("On Manage Orders");
	    return "index";
	}

}
