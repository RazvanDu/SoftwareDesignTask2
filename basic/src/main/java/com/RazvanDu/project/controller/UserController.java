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

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class UserController {

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private FoodRepository foodRepository;
    private OrderRespository orderRespository;

    public UserController(UserRepository userRepository, RestaurantRepository restaurantRepository, FoodRepository foodRepository, OrderRespository orderRespository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
        this.orderRespository = orderRespository;
    }

    @RequestMapping(value = "/logout")
    public ResponseEntity<User> logout(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("LOGGING OUT!");

        if (!Utils.loggedUsers.containsKey(request.getSession())) {
            System.out.println("CANNOT DELETE");
            return ResponseEntity.accepted().build();
        }

        Utils.loggedUsers.remove(request.getSession());
        return ResponseEntity.accepted().build();

    }

    @RequestMapping(path = "/database/signupUser", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> newUser(@RequestBody User newUser) {

        newUser.setType(0);

        if (userRepository.findByName(newUser.getName()).isPresent())
            return ResponseEntity.badRequest().build();

        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(newUser.getEmail());

        if (!mat.matches())
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(userRepository.save(newUser));

    }

    @RequestMapping(value = "/database/users/findByName/{name}")
    public ResponseEntity<User> userByName(@PathVariable String name) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent())
            return ResponseEntity.ok(user.get());
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/database/loggedUser")
    public ResponseEntity<User> loggedUser(HttpServletRequest request, HttpServletResponse response) {
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

    @RequestMapping(value = "/database/isLoggedIn")
    public ResponseEntity<String> isLoggedIn(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity<User> user = loggedUser(request, response);
        if (user != null)
            return ResponseEntity.accepted().build();
        return ResponseEntity.badRequest().build();
    }

    @RequestMapping(value = "/database/users/checkAndLogin/{name}/{pass}")
    public ResponseEntity<User> checkAndLogin(@PathVariable String name, @PathVariable String pass, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent()) {
            if (user.get().getHash().equals(pass)) {
                //Session session = Utils.sessionFactory.openSession();
                Utils.loggedUsers.put(request.getSession(), user.get());
                return ResponseEntity.ok(user.get());
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

}
