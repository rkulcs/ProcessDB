package processdb.backend.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import processdb.backend.auth.jwt.JWTHandler;
import processdb.backend.processes.Process;
import processdb.backend.processes.ProcessRepository;

@RestController
@RequestMapping("/processes")
public class ProcessController {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private JWTHandler jwtHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok(processRepository.findAll().toString());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable String id) {

        try {
            String processJSON = objectMapper.writeValueAsString(processRepository.findById(Long.parseLong(id)));
            return ResponseEntity.ok(processJSON);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid process ID.");
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("Process not found.");
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("An error occurred while retrieving the requested process.");
        }
    }

    @PreAuthorize("@jwtHandler.isValidToken(#request.getHeader('Authorization'))")
    @PostMapping("/add")
    public ResponseEntity<String> addProcess(HttpServletRequest request, @RequestBody String processJSON) {

        try {
            Process process = objectMapper.readValue(processJSON, Process.class);
            processRepository.save(process);
            return ResponseEntity.ok("Process added to database.");
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid process entry.");
        }
    }

    @PreAuthorize("@jwtHandler.isValidToken(#request.getHeader('Authorization'))")
    @PostMapping("/{id}/update")
    public ResponseEntity<String> updateProcess(@PathVariable String id, HttpServletRequest request,
                                                @RequestBody String processJSON) {

        try {
            Process process = processRepository.findById(Long.parseLong(id));
            Process updatedProcess = objectMapper.readValue(processJSON, Process.class);
            process.copyNonIdFieldsOf(updatedProcess);
            processRepository.save(process);
            return ResponseEntity.ok("Process updated in database.");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid process ID.");
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid process details.");
        }
    }

    @PreAuthorize("@jwtHandler.isValidToken(#request.getHeader('Authorization'))")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteProcess(@PathVariable String id, HttpServletRequest request) {

        try {
            processRepository.deleteById(Long.parseLong(id));
            return ResponseEntity.ok("Process deleted.");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid process ID.");
        }
    }
}
