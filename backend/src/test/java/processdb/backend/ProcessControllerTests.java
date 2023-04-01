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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import processdb.backend.api.ProcessController;
import processdb.backend.auth.jwt.JWTHandler;
import processdb.backend.processes.Process;
import processdb.backend.processes.ProcessComment;
import processdb.backend.processes.ProcessCommentRepository;
import processdb.backend.processes.ProcessRepository;
import processdb.backend.users.User;
import processdb.backend.users.UserRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProcessControllerTests {

    private static final String MOCK_TOKEN = "TOKEN";

    private static final String AUTH_HEADER = "Authorization";

    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ProcessCommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

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

        Process process = processRepository.save(createProcess());
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

        Process process = processRepository.save(createProcess());

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

        Process process = processRepository.save(createProcess());

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

        Process process = processRepository.save(createProcess());

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

        Process process = processRepository.save(createProcess());

        mockMVC.perform(delete(String.format("/processes/%d/delete", process.getId())))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteExistingProcessAsAdminWithAuthShouldReturnOk() throws Exception {

        Process process = processRepository.save(createProcess());

        String auth = mockAuthAndGenerateToken();
        mockAdminAuth();

        mockMVC.perform(
                delete(String.format("/processes/%d/delete", process.getId()))
                        .header(AUTH_HEADER, auth)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void deleteExistingProcessAsRegularUserWithAuthShouldReturnError() throws Exception {

        Process process = processRepository.save(createProcess());

        String auth = mockAuthAndGenerateToken();

        mockMVC.perform(
                        delete(String.format("/processes/%d/delete", process.getId()))
                                .header(AUTH_HEADER, auth)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteNonexistentProcessAsAdminWithAuthShouldReturnError() throws Exception {

        String auth = mockAuthAndGenerateToken();
        mockAdminAuth();

        mockMVC.perform(
                        delete("/processes/12/delete")
                                .header(AUTH_HEADER, auth)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllCommentsOfProcessShouldReturnOk() throws Exception {

        ProcessComment comment = createProcessComment();

        mockMVC.perform(
                get(String.format("/processes/%d/comments", comment.getProcess().getId()))
        ).andExpect(status().isOk());
    }

    @Test
    public void addCommentWithoutAuthShouldReturnError() throws Exception {

        Long processId = createProcess().getId();
        String commentJSON = "{\"safe\": \"true\", \"info\": \"this is a safe process\"}";

        mockMVC.perform(
                        post(String.format("/processes/%d/comments/add", processId))
                                .content(commentJSON)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(status().isForbidden());
    }

    @Test
    public void addCommentWithAuthShouldReturnOk() throws Exception {

        Long processId = createProcess().getId();
        String commentJSON = "{\"safe\": \"true\", \"info\": \"this is a safe process\"}";

        String auth = mockAuthAndGenerateToken();

        mockMVC.perform(
                post(String.format("/processes/%d/comments/add", processId))
                        .header(AUTH_HEADER, auth)
                        .content(commentJSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteCommentAsAdminShouldReturnOk() throws Exception {

        ProcessComment comment = createProcessComment();

        String auth = mockAuthAndGenerateToken();
        User admin = createAdmin();
        mockGetTokenUser(admin);

        mockMVC.perform(
                delete(String.format("/processes/%d/comments/%d/delete", comment.getProcessId(), comment.getId()))
                        .header(AUTH_HEADER, auth)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteCommentByAuthorShouldReturnOk() throws Exception {

        ProcessComment comment = createProcessComment();

        String auth = mockAuthAndGenerateToken();
        User user = comment.getAuthor();
        mockGetTokenUser(user);

        mockMVC.perform(
                delete(String.format("/processes/%d/comments/%d/delete", comment.getProcessId(), comment.getId()))
                        .header(AUTH_HEADER, auth)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk());
    }

    @Test
    public void deleteCommentAsNonAdminShouldReturnError() throws Exception {

        ProcessComment comment = createProcessComment();

        String auth = mockAuthAndGenerateToken();
        User user = createNonAdmin();
        mockGetTokenUser(user);

        mockMVC.perform(
                delete(String.format("/processes/%d/comments/%d/delete", comment.getProcessId(), comment.getId()))
                        .header(AUTH_HEADER, auth)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isForbidden());
    }

    //=== Helper Methods ===//

    private String mockAuthAndGenerateToken() {
        when(jwtHandler.isValidToken(getMockAuth())).thenReturn(true);
        return String.format(getMockAuth());
    }

    private void mockAdminAuth() {
        when(jwtHandler.isValidAdminUser(getMockAuth())).thenReturn(true);
    }

    private void mockGetTokenUser(User user) {
        when(jwtHandler.getTokenUser(getMockAuth())).thenReturn(user);
    }

    public String getMockAuth() {
        return String.format("Bearer %s", MOCK_TOKEN);
    }

    private Process createProcess() {

        Process process = processRepository.save(new Process("test", "test.exe", "Windows"));
        return process;
    }

    private User createAdmin() {

        User user = new User("admin", "admin");
        user.setAdmin(true);

        return userRepository.save(user);
    }

    private User createNonAdmin() {

        User user = new User("user", "user");
        return userRepository.save(user);
    }

    private ProcessComment createProcessComment() {

        Process process = new Process("commented", "commented", "Linux");
        processRepository.save(process);

        User user = new User("author", "pw");
        userRepository.save(user);

        ProcessComment comment = new ProcessComment(process, true, "info");
        comment.setAuthor(user);
        commentRepository.save(comment);

        return comment;
    }
}
