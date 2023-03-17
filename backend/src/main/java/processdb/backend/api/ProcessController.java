package processdb.backend.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import processdb.backend.auth.jwt.JWTHandler;
import processdb.backend.processes.ProcessRepository;

@RestController
@RequestMapping("/processes")
public class ProcessController {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private JWTHandler jwtHandler;

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAll() {
        return ResponseEntity.ok(processRepository.findAll().toString());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable String id) {

        try {
            return ResponseEntity.ok(processRepository.findById(Long.parseLong(id)).toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid process ID.");
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("Process not found.");
        }
    }

    @PreAuthorize("@jwtHandler.isValidToken(#request.getHeader('Authorization'))")
    @PostMapping("/{id}/add")
    public ResponseEntity<String> addProcess(HttpServletRequest request, @PathVariable String id) {
        return ResponseEntity.ok("TODO");
    }
}
