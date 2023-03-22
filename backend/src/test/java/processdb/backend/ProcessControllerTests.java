package processdb.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import processdb.backend.api.ProcessController;
import processdb.backend.auth.jwt.JWTHandler;
import processdb.backend.processes.Process;
import processdb.backend.processes.ProcessRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProcessControllerTests {

    private static final String MOCK_TOKEN = "TOKEN";

    private static final String AUTH_HEADER = "Authorization";

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

        String auth = mockAuthAndGenerateToken();

        mockMVC.perform(
                get("/processes")
                        .header(AUTH_HEADER, auth)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void getExistingProcessShouldReturnOk() throws Exception {

        Process process = processRepository.save(new Process("test", "test.exe", "Windows"));
        mockMVC.perform(
                get(String.format("/processes/%d", process.getId()))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void getNonexistentProcessShouldReturnError() throws Exception {

        mockMVC.perform(get("/processes/14").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getWithNonNumericIdShouldReturnError() throws Exception {

        mockMVC.perform(get("/processes/id").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addProcessWithoutAuthShouldReturnError() throws Exception {

        String processJSON = "{\"name\": \"proc\", \"filename\": \"proc.exe\", \"os\": \"Windows\"}";

        mockMVC.perform(
                post("/processes/add")
                        .content(processJSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void addProcessWithAuthShouldReturnOk() throws Exception {

        String processJSON = "{\"name\": \"proc\", \"filename\": \"proc.exe\", \"os\": \"Windows\"}";

        String auth = mockAuthAndGenerateToken();

        mockMVC.perform(
                        post("/processes/add")
                                .header(AUTH_HEADER, auth)
                                .content(processJSON)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void addInvalidProcessShouldReturnError() throws Exception {

        String processJSON = "{\"name\": \"\", \"filename\": \"proc.exe\", \"os\": \"Windows\"}";

        String auth = mockAuthAndGenerateToken();

        mockMVC.perform(
                        post("/processes/add")
                                .header(AUTH_HEADER, auth)
                                .content(processJSON)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateProcessWithoutAuthShouldReturnError() throws Exception {

        String processJSON = "{\"name\": \"proc\", \"filename\": \"proc.exe\", \"os\": \"Windows\"}";

        Process process = processRepository.save(new Process("test", "test.exe", "Windows"));

        mockMVC.perform(
                        post(String.format("/processes/%d/update", process.getId()))
                                .content(processJSON)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateProcessWithValidDetailsAndAuthShouldReturnOk() throws Exception {

        String processJSON = "{\"name\": \"proc\", \"filename\": \"proc.exe\", \"os\": \"Windows\"}";

        Process process = processRepository.save(new Process("test", "test.exe", "Windows"));

        String auth = mockAuthAndGenerateToken();

        mockMVC.perform(
                        post(String.format("/processes/%d/update", process.getId()))
                                .header(AUTH_HEADER, auth)
                                .content(processJSON)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void updateProcessWithInvalidDetailsAndAuthShouldReturnError() throws Exception {

        String processJSON = "{\"name\": \"proc\", \"filename\": \"\", \"os\": \"Windows\"}";

        Process process = processRepository.save(new Process("test", "test.exe", "Windows"));

        String auth = mockAuthAndGenerateToken();

        mockMVC.perform(
                        post(String.format("/processes/%d/update", process.getId()))
                                .header(AUTH_HEADER, auth)
                                .content(processJSON)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteProcessWithoutAuthShouldReturnError() throws Exception {

        Process process = processRepository.save(new Process("test", "test.exe", "Windows"));

        mockMVC.perform(delete(String.format("/processes/%d/delete", process.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteExistingProcessWithAuthShouldReturnOk() throws Exception {

        Process process = processRepository.save(new Process("test", "test.exe", "Windows"));

        String auth = mockAuthAndGenerateToken();

        mockMVC.perform(
                delete(String.format("/processes/%d/delete", process.getId()))
                        .header(AUTH_HEADER, auth)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void deleteNonexistentProcessWithAuthShouldReturnError() throws Exception {

        String auth = mockAuthAndGenerateToken();

        mockMVC.perform(
                        delete("/processes/12/delete")
                                .header(AUTH_HEADER, auth)
                )
                .andExpect(status().isBadRequest());
    }

    //=== Helper Methods ===//

    private String mockAuthAndGenerateToken() {
        when(jwtHandler.isValidToken(String.format("Bearer %s", MOCK_TOKEN))).thenReturn(true);
        return String.format("Bearer %s", MOCK_TOKEN);
    }
}
