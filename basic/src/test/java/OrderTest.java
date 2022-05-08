import com.RazvanDu.project.controller.FoodController;
import com.RazvanDu.project.controller.OrderController;
import com.RazvanDu.project.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@WebMvcTest(controllers = OrderController.class)
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class OrderTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    private FoodController foodController;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private OrderRespository orderRespository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
        assertThat(restTemplate).isNotNull();
        assertThat(foodController).isNotNull();
        assertThat(foodRepository).isNotNull();
        assertThat(orderController).isNotNull();
        assertThat(orderRespository).isNotNull();
        assertThat(restaurantRepository).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    public void addToCartWorkingTest() {

        MockHttpServletRequest request = TestingUtils.logUser(TestingUtils.createUser(0));

        ResponseEntity<Map<String, Integer>> responseEntity =
                orderController.addToCart(TestingUtils.createFood(0, 0), request);

        assertEquals(1, responseEntity.getBody().size());

    }

    @Test
    public void addToCartNotLoggedTest() {

        ResponseEntity<Map<String, Integer>> responseEntity =
                orderController.addToCart(TestingUtils.createFood(0, 0), new MockHttpServletRequest());

        assertEquals(ResponseEntity.badRequest().build(), responseEntity);

    }

    @Test
    public void getFoodsByUser() {

        MockHttpServletRequest request = TestingUtils.logUser(TestingUtils.createUser(0));

        orderController.addToCart(TestingUtils.createFood(0, 0), request);

        assertEquals(1, orderController.foodsById(request).getBody().size());

    }

    @Test
    public void orderTest() {

        User user = TestingUtils.createUser(0);
        MockHttpServletRequest request = TestingUtils.logUser(user);
        Restaurant restaurant = TestingUtils.createRestaurant();

        orderController.addToCart(TestingUtils.createFood(0, 1), request);
        orderController.addOrdering(restaurant, request);

        assertEquals(restaurant.getId(), Utils.ordering.get((long) user.getId()));

    }

    @Test
    public void orderedItemsTest() {

        User user = TestingUtils.createUser(0);
        MockHttpServletRequest request = TestingUtils.logUser(user);
        Restaurant restaurant = TestingUtils.createRestaurant();

        when(orderRespository.findCurrentOrder((int) user.getId())).thenReturn(Optional.empty());

        orderController.addToCart(TestingUtils.createFood(0, 1), request);
        orderController.addOrdering(restaurant, request);

        assertEquals(1, orderController.orderingTarget(request).getBody().size());

    }

    @Test
    public void orderCartTest() {

        User user = TestingUtils.createUser(0);
        User admin = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(user);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(userRepository.findById(restaurant.getAdminID())).thenReturn(Optional.of(admin));

        orderController.addToCart(TestingUtils.createFood(0, restaurant.getId()), request);
        orderController.addToCart(TestingUtils.createFood(0, restaurant.getId()), request);
        orderController.addOrdering(restaurant, request);

        ResponseEntity<String> responseEntity = orderController
                .orderCart(
                        TestingUtils.randomString(),
                        TestingUtils.randomString(),
                        request);

        assertEquals(ResponseEntity.accepted().build(), responseEntity);
        assertThat(!Utils.cart.containsKey((long) user.getId()));
        assertThat(!Utils.ordering.containsKey((long) user.getId()));

    }

    @Test
    public void currentNoOrderTest() {

        User user = TestingUtils.createUser(0);
        User admin = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(user);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());

        List<Order> orders = new ArrayList<>();
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 4));

        when(orderRespository.findAllByUserID(user.getId())).thenReturn(orders);

        ResponseEntity<Order> responseEntity = orderController
                .orderById(request);

        assertEquals(ResponseEntity.badRequest().build(), responseEntity);

    }

    @Test
    public void currentWithOrderTest() {

        User user = TestingUtils.createUser(0);
        User admin = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(user);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());

        List<Order> orders = new ArrayList<>();
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 4));
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 1));

        when(orderRespository.findAllByUserID(user.getId())).thenReturn(orders);

        ResponseEntity<Order> responseEntity = orderController
                .orderById(request);

        assertEquals(ResponseEntity.ok(orders.get(1)), responseEntity);

    }

    @Test
    public void currentOldWithOrderTest() {

        User user = TestingUtils.createUser(0);
        User admin = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(user);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());

        List<Order> orders = new ArrayList<>();
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 4));
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 1));

        when(orderRespository.findAllByUserID(user.getId())).thenReturn(orders);
        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        ResponseEntity<List<Order>> responseEntity = orderController
                .orderByIdOld(request);

        assertEquals(ResponseEntity.ok(List.of(orders.get(0))), responseEntity);

    }

    @Test
    public void cancelOrderTest() {

        User user = TestingUtils.createUser(0);
        User admin = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(user);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());

        List<Order> orders = new ArrayList<>();
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 4));
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 1));

        ResponseEntity<String> responseEntity = orderController
                .cancelOrder(orders.get(1), request);

        assertEquals(ResponseEntity.accepted().build(), responseEntity);
        assertEquals(5, (int) orders.get(1).getStatusOrder());

    }

    @Test
    public void advanceOrderTest() {

        User user = TestingUtils.createUser(0);
        User admin = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(user);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());

        List<Order> orders = new ArrayList<>();
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 4));
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 1));

        ResponseEntity<String> responseEntity = orderController
                .advanceOrder(orders.get(1), request);

        assertEquals(ResponseEntity.accepted().build(), responseEntity);
        assertEquals(2, (int) orders.get(1).getStatusOrder());

    }

    @Test
    public void adminOrdersTest() {

        User user = TestingUtils.createUser(0);
        User admin = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(admin);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());

        List<Order> orders = new ArrayList<>();
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 4));
        orders.add(TestingUtils.createOrder(restaurant.getId(), user.getId(), 1));

        when(restaurantRepository.findByAdminID(admin.getId())).thenReturn(Optional.of(restaurant));
        when(orderRespository.findAllByRestaurantID(restaurant.getId())).thenReturn(orders);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ResponseEntity<List<Order>> responseEntity = orderController
                .ordersByAdminId(request);

        assertEquals(ResponseEntity.ok(List.of(orders.get(1))), responseEntity);

    }

}