package processdb.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
@ExtendWith(MockitoExtension.class)
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

        User user = new User("user", "user");
        userRepository.save(user);

        mockMVC.perform(
                        post("/auth/register")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest());
    }
}
