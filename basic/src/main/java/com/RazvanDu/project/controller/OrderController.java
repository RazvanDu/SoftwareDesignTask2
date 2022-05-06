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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class OrderController {

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private FoodRepository foodRepository;
    private OrderRespository orderRespository;

    public OrderController(UserRepository userRepository, RestaurantRepository restaurantRepository, FoodRepository foodRepository, OrderRespository orderRespository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.foodRepository = foodRepository;
        this.orderRespository = orderRespository;
    }

    @RequestMapping(value = "/database/getCart")
    public ResponseEntity<List<Food>> foodsById(HttpServletRequest request, HttpServletResponse response) {
        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();
        long id = Utils.loggedUsers.get(request.getSession()).getId();
        if (Utils.cart.containsKey(id))
            return ResponseEntity.ok(Utils.cart.get(id));
        return ResponseEntity.ok(new ArrayList<>());
    }

    @RequestMapping(value = "/database/addCart", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Integer>> addToCart(@RequestBody Food food, HttpServletRequest request, HttpServletResponse response) {
        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();
        long id = Utils.loggedUsers.get(request.getSession()).getId();
        if (!Utils.cart.containsKey(id))
            Utils.cart.put((id), new ArrayList<>());
        //Optional<Food> food = foodRepository.findById(foodId);
        //if(!food.isPresent())
        //    return ResponseEntity.badRequest().build();
        Utils.cart.get(id).add(food);
        Map<String, Integer> mapp = new HashMap<>();
        mapp.put("price", Utils.cart.get(id).stream().mapToInt(Food::getPrice).sum());
        return ResponseEntity.ok(mapp);
    }

    @RequestMapping(value = "/database/addOrdering", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Food>> addOrdering(@RequestBody Restaurant restaurant, HttpServletRequest request, HttpServletResponse response) {
        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();
        long id = Utils.loggedUsers.get(request.getSession()).getId();
        Utils.ordering.put(id, restaurant.getId());
        System.out.println("Ordering from " + restaurant.getName() + "!");
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/database/getOrdering")
    public ResponseEntity<Map<String, Integer>> orderingTarget(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Integer> mapp = new HashMap<>();
        mapp.put("id", -1);
        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.ok(mapp);
        long id = Utils.loggedUsers.get(request.getSession()).getId();
        System.out.println(orderRespository.findCurrentOrder((int) id));
        if (orderRespository.findCurrentOrder((int) id).isPresent()) {
            mapp.put("id", 100000);
            return ResponseEntity.ok(mapp);
        }
        if (!Utils.ordering.containsKey(id))
            return ResponseEntity.ok(mapp);
        mapp.put("id", Utils.ordering.get(id));
        return ResponseEntity.ok(mapp);
    }

    @RequestMapping(value = "/database/orders/get")
    public ResponseEntity<Order> orderById(HttpServletRequest request, HttpServletResponse response) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        int id = Utils.loggedUsers.get(request.getSession()).getId();

        List<Order> orders = orderRespository.findAllByUserID(id);

        Order target = null;

        for (Order order : orders) {
            if (order.getStatusOrder() == 1 || order.getStatusOrder() == 2 || order.getStatusOrder() == 3) {
                target = order;
                break;
            }
        }

        if (target == null)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(target);

    }

    @RequestMapping(value = "/database/orders/admin")
    public ResponseEntity<List<Order>> ordersByAdminId(HttpServletRequest request, HttpServletResponse response) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        int id = Utils.loggedUsers.get(request.getSession()).getId();

        Restaurant restaurant = restaurantRepository.findByAdminID(id).get();

        List<Order> orders = orderRespository.findAllByRestaurantID(restaurant.getId());

        List<Order> targets = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatusOrder() == 1 || order.getStatusOrder() == 2 || order.getStatusOrder() == 3) {
                order.setRestaurantName(restaurant.getName());
                order.setUserName(userRepository.findById(order.getUserID()).get().getName());
                targets.add(order);
            }
        }

        return ResponseEntity.ok(targets);
    }

    @RequestMapping(value = "/database/orders/old")
    public ResponseEntity<List<Order>> orderByIdOld(HttpServletRequest request, HttpServletResponse response) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        int id = Utils.loggedUsers.get(request.getSession()).getId();

        List<Order> orders = orderRespository.findAllByUserID(id);

        List<Order> targets = new ArrayList<>();

        for (Order order : orders) {
            if (order.getStatusOrder() == 4 || order.getStatusOrder() == 5) {
                order.setRestaurantName(restaurantRepository.findById(order.getRestaurantID()).get().getName());
                targets.add(order);
            }
        }

        return ResponseEntity.ok(targets);
    }

    /*@RequestMapping(value = "/database/orders/oldRestaurants")
    public ResponseEntity<List<Restaurant>> oldRestaurants(HttpServletRequest request, HttpServletResponse response) {

        if(!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        int id = Utils.loggedUsers.get(request.getSession()).getId();

        List<Order> orders = orderRespository.findAllByUserID(id);

        List<Order> targets = new ArrayList<>();

        for(Order order : orders) {
            if (order.getStatusOrder() == 4 || order.getStatusOrder() == 5)
                targets.add(order);
        }

        List<Restaurant> result = new ArrayList<>();

        for(Order order : targets) {
            result.add(restaurantRepository.findById(order.getId()).get());
        }

        return ResponseEntity.ok(result);
    }*/

    @RequestMapping(path = "/database/orderCart", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderCart(HttpServletRequest request, HttpServletResponse response) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        long id = Utils.loggedUsers.get(request.getSession()).getId();

        if (!Utils.cart.containsKey(id))
            return ResponseEntity.badRequest().build();

        if (!Utils.ordering.containsKey(id))
            return ResponseEntity.badRequest().build();

        Order newOrder = new Order(
                Utils.ordering.get(id),
                (int) id,
                Utils.cart.get(id).stream().map(food -> food.getName()).collect(Collectors.joining(", ")),
                1
        );

        orderRespository.save(newOrder);

        Utils.cart.remove(id);
        Utils.ordering.remove(id);

        return ResponseEntity.accepted().build();

    }

    @RequestMapping(path = "/database/cancelOrder", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    //public ResponseEntity<User> newUser(@RequestBody String name, @RequestBody String hash, @RequestBody String email) {
    public ResponseEntity<String> cancelOrder(@RequestBody Order order, HttpServletRequest request, HttpServletResponse response) {

        //System.out.println(newUser);

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        order.setStatusOrder(5);

        orderRespository.save(order);

        return ResponseEntity.accepted().build();

    }

    @RequestMapping(path = "/database/advanceOrder", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    //public ResponseEntity<User> newUser(@RequestBody String name, @RequestBody String hash, @RequestBody String email) {
    public ResponseEntity<String> advanceOrder(@RequestBody Order order, HttpServletRequest request, HttpServletResponse response) {

        //System.out.println(newUser);

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        order.setStatusOrder(Math.min(4, order.getStatusOrder() + 1));

        orderRespository.save(order);

        return ResponseEntity.accepted().build();

    }

}
