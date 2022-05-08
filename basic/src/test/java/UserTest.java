import com.RazvanDu.project.controller.FoodController;
import com.RazvanDu.project.controller.OrderController;
import com.RazvanDu.project.controller.RestaurantController;
import com.RazvanDu.project.controller.UserController;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@WebMvcTest(controllers = FoodController.class)
@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class UserTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    private FoodController foodController;

    @InjectMocks
    private OrderController orderController;

    @InjectMocks
    private UserController userController;

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
        assertThat(userController).isNotNull();
        assertThat(orderController).isNotNull();
        assertThat(orderRespository).isNotNull();
        assertThat(restaurantRepository).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    public void logoutTest() {

        User user = TestingUtils.createUser(0);
        MockHttpServletRequest request = TestingUtils.logUser(user);

        ResponseEntity<User> responseEntity = userController.logout(request);

        assertEquals(ResponseEntity.accepted().build().getStatusCode(), responseEntity.getStatusCode());
        assert(!Utils.loggedUsers.containsKey(request.getSession()));

    }

    @Test
    public void loggedUserTest() {

        User user = TestingUtils.createUser(0);
        MockHttpServletRequest request = TestingUtils.logUser(user);

        ResponseEntity<User> responseEntity = userController.loggedUser(request);

        assertEquals(ResponseEntity.ok(user), responseEntity);

    }

    @Test
    public void isLoggedInUserTest() {

        User user = TestingUtils.createUser(0);
        MockHttpServletRequest request = TestingUtils.logUser(user);

        ResponseEntity<String> responseEntity = userController.isLoggedIn(request);

        assertEquals(ResponseEntity.accepted().build(), responseEntity);

    }

    @Test
    public void checkAndLoginTest() {

        User user = TestingUtils.createUser(0);
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));

        ResponseEntity<User> responseEntity = userController.checkAndLogin(user.getName(), user.getHash(), request);

        assertEquals(ResponseEntity.ok(user), responseEntity);
        assert(Utils.loggedUsers.containsKey(request.getSession()));

    }

    @Test
    public void checkAndLoginInvalidPasswordTest() {

        User user = TestingUtils.createUser(0);
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));

        ResponseEntity<User> responseEntity = userController
                .checkAndLogin(user.getName(), TestingUtils.randomString(), request);

        assertEquals(ResponseEntity.notFound().build(), responseEntity);
        assert(!Utils.loggedUsers.containsKey(request.getSession()));

    }

    @Test
    public void signupInvalidEmailTest() {

        User user = TestingUtils.createUser(0);

        ResponseEntity<User> responseEntity = userController.newUser(user);

        assertEquals(ResponseEntity.badRequest().build(), responseEntity);

    }

    @Test
    public void signupSuccessfulTest() {

        User user = TestingUtils.createUser(0);
        user.setEmail("something@gmail.com");

        ResponseEntity<User> responseEntity = userController.newUser(user);

        assertEquals(ResponseEntity.ok().build().getStatusCode(), responseEntity.getStatusCode());

    }


    @Test
    public void findByName() {

        User user = TestingUtils.createUser(0);

        when(userRepository.findByName(user.getName())).thenReturn(Optional.of(user));

        ResponseEntity<User> responseEntity = userController.userByName(user.getName());

        assertEquals(ResponseEntity.ok(user), responseEntity);

    }

}