import com.RazvanDu.project.controller.FoodController;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@WebMvcTest(controllers = FoodController.class)
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class FoodTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    private FoodController foodController;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Test
    public void contextLoads() {
        assertThat(restTemplate).isNotNull();
        assertThat(foodController).isNotNull();
        assertThat(foodRepository).isNotNull();
    }

    @Test
    public void getFoodsTest() {

        List<Food> foods = Arrays.asList(TestingUtils.createFood(0, 0),
                                         TestingUtils.createFood(0, 0),
                                         TestingUtils.createFood(0, 0));

        when(foodRepository.findAll()).thenReturn(foods);
        List<Food> foodListTemp = (List<Food>) foodController.foods().getBody();

        assertEquals(3, foodListTemp.size());

    }


    @Test
    public void addFoodLoggedTest() {

        User user = TestingUtils.createUser(1);
        MockHttpServletRequest request = TestingUtils.logUser(user);

        Restaurant restaurant = TestingUtils.createRestaurant();
        restaurant.setAdminID(user.getId());

        when(restaurantRepository.findByAdminID(user.getId())).thenReturn(Optional.of(restaurant));

        ResponseEntity<String> responseEntity =
                foodController.addFood(TestingUtils.createFood(0, restaurant.getId()), request);

        assertEquals(ResponseEntity.accepted().build(), responseEntity);

    }

    @Test
    public void addFoodNotLoggedTest() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        Food food = TestingUtils.createFood(0, -1);
        ResponseEntity<String> responseEntity = foodController.addFood(food, request);
        List<Food> foodListTemp = (List<Food>) foodController.foods().getBody();

        assertEquals(ResponseEntity.badRequest().build(), responseEntity);
        assertEquals(0, foodListTemp.size());

    }

}