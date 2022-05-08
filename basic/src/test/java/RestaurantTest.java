import com.RazvanDu.project.controller.FoodController;
import com.RazvanDu.project.controller.OrderController;
import com.RazvanDu.project.controller.RestaurantController;
import com.RazvanDu.project.model.*;
import com.itextpdf.text.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@WebMvcTest(controllers = FoodController.class)
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class RestaurantTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    private FoodController foodController;

    @InjectMocks
    private OrderController orderController;

    @InjectMocks
    private RestaurantController restaurantController;

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
        assertThat(restaurantController).isNotNull();
        assertThat(orderController).isNotNull();
        assertThat(orderRespository).isNotNull();
        assertThat(restaurantRepository).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    public void orderCartTest() throws FileNotFoundException, DocumentException {

        User admin = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(admin);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());
        restaurant.setFoods(List.of(TestingUtils.createFood(0, restaurant.getId())));

        when(restaurantRepository.findByAdminID(admin.getId())).thenReturn(Optional.of(restaurant));

        ResponseEntity<InputStreamResource> responseEntity = restaurantController
                .exportPDF(request);

        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());

    }

    @Test
    public void restaurantsFilterTest() {

        User admin1 = TestingUtils.createUser(1);
        User admin2 = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(admin1);
        Restaurant restaurant1 = TestingUtils.createRestaurant();
        restaurant1.setAdminID(admin1.getId());
        restaurant1.setFoods(List.of(TestingUtils.createFood(0, restaurant1.getId())));
        Restaurant restaurant2 = TestingUtils.createRestaurant();
        restaurant2.setAdminID(admin2.getId());
        restaurant2.setFoods(List.of(TestingUtils.createFood(0, restaurant2.getId())));

        restaurant1.setName("First Restaurant");
        restaurant2.setName("Second Restaurant");

        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1, restaurant2));

        ResponseEntity<Iterable<Restaurant>> responseEntity = restaurantController
                .restaurantsFilter("First");

        assertEquals(ResponseEntity.ok(List.of(restaurant1)), responseEntity);

    }

    @Test
    public void restaurantByIdTest() {

        User admin = TestingUtils.createUser(1);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());
        restaurant.setFoods(List.of(TestingUtils.createFood(0, restaurant.getId())));

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        ResponseEntity<Restaurant> responseEntity = restaurantController
                .restaurantsById(restaurant.getId());

        assertEquals(ResponseEntity.ok(restaurant), responseEntity);

    }

    @Test
    public void addRestaurantTest() {

        User admin = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(admin);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());
        restaurant.setFoods(List.of(TestingUtils.createFood(0, restaurant.getId())));

        ResponseEntity<String> responseEntity = restaurantController
                .addRestaurant(restaurant, request);

        assertEquals(ResponseEntity.accepted().build(), responseEntity);

    }

    @Test
    public void allRestaurantsTest() {

        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(0);
        restaurant.setFoods(List.of(TestingUtils.createFood(0, restaurant.getId())));

        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        ResponseEntity<Iterable<Restaurant>> responseEntity = restaurantController
                .restaurantsAll();

        assertEquals(List.of(restaurant), responseEntity.getBody());

    }

    @Test
    public void foodsByIdTest() {

        User admin = TestingUtils.createUser(1);
        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(admin.getId());

        List<Food> foods = List.of(TestingUtils.createFood(0, restaurant.getId()));
        restaurant.setFoods(foods);

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));

        ResponseEntity<List<Food>> responseEntity = restaurantController
                .foodsById(restaurant.getId());

        assertEquals(foods, responseEntity.getBody());

    }

}