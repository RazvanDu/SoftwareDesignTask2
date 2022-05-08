package com.RazvanDu.project.controller;

import com.RazvanDu.project.model.*;
import com.sun.istack.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * The type Order controller.
 */
@Controller
public class OrderController {

    private UserRepository userRepository;
    private RestaurantRepository restaurantRepository;
    private OrderRespository orderRespository;

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private JavaMailSender emailSender;

    /**
     * Instantiates a new Order controller.
     *
     * @param userRepository       the user repository
     * @param restaurantRepository the restaurant repository
     * @param orderRespository     the order respository
     */
    public OrderController(UserRepository userRepository,
                           RestaurantRepository restaurantRepository,
                           OrderRespository orderRespository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderRespository = orderRespository;
    }

    /**
     * Foods by id response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/getCart")
    public ResponseEntity<List<Food>> foodsById(HttpServletRequest request) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        long id = Utils.loggedUsers.get(request.getSession()).getId();

        if (Utils.cart.containsKey(id))
            return ResponseEntity.ok(Utils.cart.get(id));

        return ResponseEntity.ok(new ArrayList<>());

    }

    /**
     * Add to cart response entity.
     *
     * @param food    the food
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/addCart", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Integer>> addToCart(@RequestBody Food food,
                                                          HttpServletRequest request) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        long id = Utils.loggedUsers.get(request.getSession()).getId();

        if (!Utils.cart.containsKey(id))
            Utils.cart.put((id), new ArrayList<>());
        Utils.cart.get(id).add(food);

        logger.info("Customer with the id " + id + " just added a " + food.getName() + " to his cart!");

        Map<String, Integer> mapp = new HashMap<>();
        mapp.put("price", Utils.cart.get(id).stream().mapToInt(Food::getPrice).sum());

        return ResponseEntity.ok(mapp);

    }

    /**
     * Set ordering target response entity.
     *
     * @param restaurant the restaurant
     * @param request    the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/addOrdering", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Food>> addOrdering(@RequestBody Restaurant restaurant,
                                                  HttpServletRequest request) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        long id = Utils.loggedUsers.get(request.getSession()).getId();
        Utils.ordering.put(id, restaurant.getId());

        logger.info("Customer with id " + id + " is now ordering from restaurant " + restaurant.getName());

        return ResponseEntity.accepted().build();

    }

    /**
     * User's current ordering restaurant id response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/getOrdering")
    public ResponseEntity<Map<String, Integer>> orderingTarget(HttpServletRequest request) {

        Map<String, Integer> mapp = new HashMap<>();
        mapp.put("id", -1);

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.ok(mapp);

        long id = Utils.loggedUsers.get(request.getSession()).getId();
        if (orderRespository.findCurrentOrder((int) id).isPresent()) {
            mapp.put("id", 100000);
            return ResponseEntity.ok(mapp);
        }

        if (!Utils.ordering.containsKey(id))
            return ResponseEntity.ok(mapp);

        mapp.put("id", Utils.ordering.get(id));
        return ResponseEntity.ok(mapp);

    }

    /**
     * Order by id response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/orders/get")
    public ResponseEntity<Order> orderById(HttpServletRequest request) {

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

        if (target == null) {
            logger.warning("Customer with id " + id + " tried to find his order without having one!");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(target);

    }

    /**
     * Orders by admin id response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/orders/admin")
    public ResponseEntity<List<Order>> ordersByAdminId(HttpServletRequest request) {

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

    /**
     * Order by id old response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/orders/old")
    public ResponseEntity<List<Order>> orderByIdOld(HttpServletRequest request) {

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

    /**
     * Order cart response entity.
     *
     * @param address the address
     * @param special the special
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(path = "/database/orderCart", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> orderCart(@RequestParam String address,
                                            @RequestParam String special,
                                            HttpServletRequest request) {

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

        Restaurant restaurant = restaurantRepository.findById(Utils.ordering.get(id)).get();

        orderRespository.save(newOrder);

        if(userRepository.findById(restaurant.getAdminID()).stream().findAny().get().getEmail().contains("@")) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ducktestingg@gmail.com");
            message.setTo(userRepository.findById(restaurant.getAdminID()).stream().findAny().get().getEmail());
            message.setSubject("Your order from " + userRepository.findById((int) id).get().getName());

            String text = "You have been ordered: \n\n";

            int total = 0;

            for (Food food : Utils.cart.get(id)) {
                text += food.getName() + "\n";
                total += food.getPrice();
            }

            text += "\nFor a total of " + total + "$" + "\n";
            text += "The address is: " + address + "\n";
            text += "The special requirements are: " + special + "\n";

            message.setText(text);
            emailSender.send(message);

        }

        Utils.cart.remove(id);
        Utils.ordering.remove(id);

        logger.info("An order has just been send by the user with id " + id + " to " + restaurant.getName());

        return ResponseEntity.accepted().build();

    }

    /**
     * Cancel order response entity.
     *
     * @param order   the order
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(path = "/database/cancelOrder", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> cancelOrder(@RequestBody Order order,
                                              HttpServletRequest request) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        order.setStatusOrder(5);

        orderRespository.save(order);

        logger.info("Customer with id " + order.getUserID() + " just cancelled his order with id " + order.getId());

        return ResponseEntity.accepted().build();

    }

    /**
     * Advance order response entity.
     *
     * @param order   the order
     * @param request the request
     * @return the response entity
     */
    @RequestMapping(path = "/database/advanceOrder", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> advanceOrder(@RequestBody Order order,
                                               HttpServletRequest request) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        order.setStatusOrder(Math.min(4, order.getStatusOrder() + 1));

        orderRespository.save(order);

        logger.info("The order of customer with id " + order.getUserID() + " just advanced!");

        return ResponseEntity.accepted().build();

    }

}
