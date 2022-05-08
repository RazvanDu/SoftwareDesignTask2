import com.RazvanDu.project.model.*;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Random;

public class TestingUtils {

    private static int currentUser = 0;
    private static int currentRestaurant = 0;
    private static int currentFood = 0;
    private static int currentOrder = 0;

    public static User createUser(int type) {

        User user = new User(randomString(), randomString(), randomString(), type);
        user.setId(currentUser++);

        return user;

    }

    public static MockHttpServletRequest logUser(User user) {

        MockHttpServletRequest request = new MockHttpServletRequest();
        Utils.loggedUsers.put(request.getSession(), user);

        return request;

    }

    public static Restaurant createRestaurant() {

        Restaurant restaurant = new Restaurant(randomString(), randomString(), randomString());
        restaurant.setId(currentRestaurant++);

        return restaurant;

    }

    public static Food createFood(int category, int restaurantID) {

        Food food = new Food(randomString(), randomString(), new Random().nextInt(255), category, restaurantID);
        food.setId(currentFood++);

        return food;

    }

    public static Order createOrder(int restaurantID, int userId, int statusOrder) {

        Order order = new Order(restaurantID, userId, randomString(), statusOrder);
        order.setId(currentOrder++);

        return order;

    }

    public static String randomString() {

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int length = 7;

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(index);
            sb.append(randomChar);
        }

        String randomString = sb.toString();
        return randomString;

    }

}
