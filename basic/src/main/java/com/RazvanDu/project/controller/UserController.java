package com.RazvanDu.project.controller;

import com.RazvanDu.project.model.*;
import com.sun.istack.logging.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * The type User controller.
 */
@Controller
public class UserController {

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Instantiates a new User controller.
     *
     * @param userRepository       the user repository
     * @param restaurantRepository the restaurant repository
     */
    public UserController(UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Logout response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/logout")
    public ResponseEntity<User> logout(HttpServletRequest request) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.accepted().build();

        logger.info("The user with id " + Utils.loggedUsers.get(request.getSession()) + "just logged out!");

        Utils.loggedUsers.remove(request.getSession());
        return ResponseEntity.accepted().build();

    }

    /**
     * New user response entity.
     *
     * @param newUser the new user
     * @return the response entity
     */
    @RequestMapping(path = "/database/signupUser", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> newUser(@RequestBody User newUser) {

        newUser.setType(0);

        if (userRepository.findByName(newUser.getName()).isPresent())
            return ResponseEntity.badRequest().build();

        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(newUser.getEmail());

        if (!mat.matches())
            return ResponseEntity.badRequest().build();

        logger.info("The user " + newUser.getId() + " has just been created!");

        return ResponseEntity.ok(userRepository.save(newUser));

    }

    /**
     * User by name response entity.
     *
     * @param name the name
     * @return the response entity
     */
    @RequestMapping(value = "/database/users/findByName/{name}")
    public ResponseEntity<User> userByName(@PathVariable String name) {

        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent())
            return ResponseEntity.ok(user.get());

        return ResponseEntity.notFound().build();

    }

    /**
     * Logged user response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/loggedUser")
    public ResponseEntity<User> loggedUser(HttpServletRequest request) {

        User user = null;

        if (Utils.loggedUsers.containsKey(request.getSession()))
            user = Utils.loggedUsers.get(request.getSession());

        if (user != null) {
            if (user.getType() == 1) {
                if (!restaurantRepository.findByAdminID(user.getId()).isPresent())
                    user.setNewAdmin(1);
                else
                    user.setNewAdmin(0);
            } else {
                user.setNewAdmin(0);
            }
        }

        return ResponseEntity.ok(user);

    }

    /**
     * Is logged in response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/isLoggedIn")
    public ResponseEntity<String> isLoggedIn(HttpServletRequest request) {

        ResponseEntity<User> user = loggedUser(request);
        if (user.getBody() != null)
            return ResponseEntity.accepted().build();

        return ResponseEntity.badRequest().build();

    }

    /**
     * Check and login response entity.
     *
     * @param name    the name
     * @param pass    the pass
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/users/checkAndLogin/{name}/{pass}")
    public ResponseEntity<User> checkAndLogin(@PathVariable String name,
                                              @PathVariable String pass,
                                              HttpServletRequest request) {

        Optional<User> user = userRepository.findByName(name);

        if (user.isPresent()) {
            if (user.get().getHash().equals(pass)) {
                Utils.loggedUsers.put(request.getSession(), user.get());
                logger.info("The user " + user.get().getName() + " just logged in!");
                return ResponseEntity.ok(user.get());
            }
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();

    }

}
