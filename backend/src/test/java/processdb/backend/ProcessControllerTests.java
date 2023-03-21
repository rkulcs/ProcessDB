package processdb.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import processdb.backend.api.ProcessController;
import processdb.backend.auth.jwt.JWTHandler;
import processdb.backend.processes.ProcessRepository;
import processdb.backend.users.User;
import processdb.backend.users.UserRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProcessControllerTests {

    private static final String MOCK_TOKEN = "TOKEN";

    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private ProcessRepository processRepository;

    @MockBean
    private JWTHandler jwtHandler;

    @Autowired
    @InjectMocks
    private ProcessController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(jwtHandler);
    }

    @Test
    public void getAllShouldReturnOkWithoutAuth() throws Exception {

        mockMVC.perform(get("/processes").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllShouldReturnOkWithAuth() throws Exception {

        User user = new User("test", "test");
        when(jwtHandler.generateToken(user)).thenReturn(MOCK_TOKEN);
        when(jwtHandler.isValidToken(String.format("Bearer %s", MOCK_TOKEN))).thenReturn(true);

        String userToken = jwtHandler.generateToken(user);

        mockMVC.perform(
                get("/processes")
                        .header("Authorization", userToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk());
    }
}
