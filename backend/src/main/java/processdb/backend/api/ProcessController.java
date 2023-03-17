package processdb.backend.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import processdb.backend.processes.ProcessRepository;

@RestController
@RequestMapping("/processes")
public class ProcessController {

    @Autowired
    private ProcessRepository processRepository;

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
}
