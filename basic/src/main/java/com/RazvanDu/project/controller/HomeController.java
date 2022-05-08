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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Home controller.
 */
@Controller
public class HomeController {

    /**
     * Index page.
     *
     * @return the string
     */
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    /**
     * Dashboard page.
     *
     * @return the string
     */
    @RequestMapping(value = "/dashboard")
    public String dashboard() {
        return "index";
    }

    /**
     * Preferences page.
     *
     * @return the string
     */
    @RequestMapping(value = "/preferences")
    public String preferences() {
        return "index";
    }

    /**
     * Login page.
     *
     * @return the string
     */
    @RequestMapping(value = "/login")
    public String login() {
        return "index";
    }

    /**
     * Signup page.
     *
     * @return the string
     */
    @RequestMapping(value = "/signup")
    public String signup() {
        return "index";
    }

    /**
     * Cart page.
     *
     * @return the string
     */
    @RequestMapping(value = "/cart")
    public String cart() {
        return "index";
    }

    /**
     * Order page.
     *
     * @return the string
     */
    @RequestMapping(value = "/order")
    public String orderr() {
        return "index";
    }

    /**
     * Add food page.
     *
     * @return the string
     */
    @RequestMapping(value = "/addFood")
    public String addFood() {
        return "index";
    }

    /**
     * Manage orders page.
     *
     * @return the string
     */
    @RequestMapping(value = "/manageOrders")
    public String manageOrders() {
        return "index";
    }

}
