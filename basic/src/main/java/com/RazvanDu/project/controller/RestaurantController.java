package com.RazvanDu.project.controller;

import com.RazvanDu.project.model.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.istack.logging.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * The type Restaurant controller.
 */
@Controller
public class RestaurantController {

    private RestaurantRepository restaurantRepository;

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Instantiates a new Restaurant controller.
     *
     * @param restaurantRepository the restaurant repository
     */
    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Export a restaurant's pdf response entity.
     *
     * @param request the request
     * @return the response entity
     * @throws FileNotFoundException the file not found exception
     * @throws DocumentException     the document exception
     */
    @RequestMapping(value = "/exportPDF")
    public ResponseEntity<InputStreamResource> exportPDF(HttpServletRequest request)
            throws FileNotFoundException, DocumentException {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        long id = Utils.loggedUsers.get(request.getSession()).getId();
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByAdminID((int) id);

        if(!restaurantOptional.isPresent()) {
            logger.severe("The customer with id " + id + " isn't an admin but tried to export the menu as PDF!");
            return ResponseEntity.badRequest().build();
        }

        Restaurant restaurant = restaurantOptional.get();
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("menu.pdf"));

        document.open();

        Paragraph paragraph = new Paragraph();
        paragraph.add("------- " + restaurant.getName() + " -------\n\n");
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

        for(Food food : restaurant.getFoods()) {
            paragraph = new Paragraph();
            paragraph.add("Name: " + food.getName() + "\n");
            paragraph.add("Description: " + food.getDescription() + "\n");
            paragraph.add("Price: " + food.getPrice() + "$" + "\n\n");
            document.add(paragraph);
        }

        document.close();

        File file = new File("menu.pdf");
        HttpHeaders respHeaders = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/pdf");

        respHeaders.setContentType(mediaType);
        respHeaders.setContentLength(file.length());
        respHeaders.setContentDispositionFormData("attachment", file.getName());

        InputStreamResource isr = new InputStreamResource(new FileInputStream(file));

        logger.info("The custom with id " + id + " just exported with restaurant's PDF");

        return new ResponseEntity<>(isr, respHeaders, HttpStatus.OK);

    }

    /**
     * Foods by id response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @RequestMapping(value = "/database/foodsByRestaurant/{id}")
    public ResponseEntity<List<Food>> foodsById(@PathVariable Integer id) {

        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent())
            return ResponseEntity.ok(restaurant.get().getFoods());

        return ResponseEntity.notFound().build();

    }

    /**
     * All restaurants response entity.
     *
     * @return the response entity
     */
    @RequestMapping(value = "/database/restaurants/all")
    public ResponseEntity<Iterable<Restaurant>> restaurantsAll() {

        Iterable<Restaurant> restaurants = restaurantRepository.findAll();
        return ResponseEntity.ok(restaurants);

    }

    /**
     * Restaurants filter by name response entity.
     *
     * @param name the name
     * @return the response entity
     */
    @RequestMapping(value = "/database/restaurants/allFilter/{name}")
    public ResponseEntity<Iterable<Restaurant>> restaurantsFilter(@PathVariable String name) {

        Iterable<Restaurant> restaurants = restaurantRepository.findAll();

        if (name.equals("-1"))
            return ResponseEntity.ok(restaurants);

        List<Restaurant> result = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getName().replaceAll(" ", "")
                    .toLowerCase().contains(name.toLowerCase().replaceAll(" ", "")))
                result.add(restaurant);
        }

        return ResponseEntity.ok(result);

    }

    /**
     * Restaurants by id response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @RequestMapping(value = "/database/restaurants/byId/{id}")
    public ResponseEntity<Restaurant> restaurantsById(@PathVariable Integer id) {

        Restaurant restaurant = restaurantRepository.findById(id).get();
        return ResponseEntity.ok(restaurant);

    }

    /**
     * Add restaurant response entity.
     *
     * @param restaurant the restaurant
     * @param request    the request
     * @return the response entity
     */
    @RequestMapping(value = "/database/addRestaurant", method = POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addRestaurant(@RequestBody Restaurant restaurant, HttpServletRequest request) {

        if (!Utils.loggedUsers.containsKey(request.getSession()))
            return ResponseEntity.badRequest().build();

        long id = Utils.loggedUsers.get(request.getSession()).getId();
        restaurant.setAdminID((int) id);
        restaurantRepository.save(restaurant);

        logger.info("The admin with id " + id + " just add the new restaurant " + restaurant.getName());

        return ResponseEntity.accepted().build();

    }

}
