package processdb.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import processdb.backend.auth.controller.AuthController;
import processdb.backend.auth.jwt.JWTHandler;
import processdb.backend.users.User;
import processdb.backend.users.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private JWTHandler jwtHandler;

    @Autowired
    private AuthController controller;

    //=== Registration Tests ===//

    @Test
    public void successfulRegistrationShouldReturnOk() throws Exception {

        String body = "{\"username\": \"test\", \"password\": \"test\", \"passwordConfirmation\": \"test\"}";

        mockMVC.perform(
                post("/auth/register")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void nonMatchingPasswordsShouldReturnError() throws Exception {

        String body = "{\"username\": \"user2\", \"password\": \"user\", \"passwordConfirmation\": \"password\"}";

        mockMVC.perform(
                        post("/auth/register")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotRegisterExistingUser() throws Exception {

        String body = "{\"username\": \"user\", \"password\": \"user\", \"passwordConfirmation\": \"user\"}";

        // Save user in DB
        User user = new User("user", "user");
        userRepository.save(user);

        // Attempt to register as user with same credentials as previously saved user
        mockMVC.perform(
                        post("/auth/register")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest());
    }

    //=== Login Tests ===//

    @Test
    public void successfulLoginShouldReturnOk() throws Exception {

        String body = "{\"username\": \"bob\", \"password\": \"bob\"}";

        User user = new User("bob", "bob");
        userRepository.save(user);

        // Attempt to log in as a user that is saved in the database
        mockMVC.perform(
                        post("/auth/login")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void loginAsNonexistentUserShouldReturnError() throws Exception {

        String body = "{\"username\": \"nonexistent\", \"password\": \"password\"}";

        // Attempt to log in as a user that is saved in the database
        mockMVC.perform(
                        post("/auth/login")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginWithInvalidPasswordShouldReturnError() throws Exception {

        User user = new User("gene", "parmesan");
        userRepository.save(user);

        String body = "{\"username\": \"gene\", \"password\": \"gene\"}";

        // Attempt to log in with a different password
        mockMVC.perform(
                        post("/auth/login")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest());
    }
}
