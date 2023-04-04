package processdb.backend.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import processdb.backend.auth.jwt.JWTHandler;
import processdb.backend.processes.Process;
import processdb.backend.processes.ProcessComment;
import processdb.backend.processes.ProcessCommentRepository;
import processdb.backend.processes.ProcessRepository;
import processdb.backend.users.User;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/processes")
@CrossOrigin("*")
public class ProcessController {

    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ProcessCommentRepository commentRepository;

    @Autowired
    private JWTHandler jwtHandler;

    @Autowired
    private ObjectMapper objectMapper;

    //===== Processes =====//

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAll() throws JsonProcessingException {

        String processesJSON = objectMapper.writeValueAsString(processRepository.findAll());
        return ResponseEntity.ok(processesJSON);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findById(@PathVariable String id) {

        try {
            Process process = processRepository.findById(Long.parseLong(id));

            if (process == null)
                return ResponseEntity.badRequest().body("Process not found.");

            String processJSON = objectMapper.writeValueAsString(process);

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
            process.setAddedBy(getUser(request));
            processRepository.save(process);
            return ResponseEntity.ok("Process added to database.");
        } catch (JsonProcessingException | TransactionSystemException e) {
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
        } catch (JsonProcessingException | TransactionSystemException e) {
            return ResponseEntity.badRequest().body("Invalid process details.");
        }
    }

    @PreAuthorize("@jwtHandler.isValidAdminUser(#request.getHeader('Authorization'))")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteProcess(@PathVariable String id, HttpServletRequest request) {

        try {
            long processID = Long.parseLong(id);
            Process process = processRepository.findById(processID);

            if (process == null)
                return ResponseEntity.badRequest().body("Invalid process ID.");

            processRepository.deleteById(processID);
            return ResponseEntity.ok("Process deleted.");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid process ID.");
        }
    }

    //===== Process Comments =====//

    @GetMapping("/{id}/comments")
    public ResponseEntity<String> getProcessComments(@PathVariable String id, HttpServletRequest request) {

        try {
            Process process = processRepository.findById(Long.parseLong(id));
            List<ProcessComment> comments = commentRepository.findAllByProcess(process);
            String processesJSON = objectMapper.writeValueAsString(comments);
            return ResponseEntity.ok(processesJSON);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid process ID.");
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Failed to retrieve process comments.");
        }
    }

    @PreAuthorize("@jwtHandler.isValidToken(#request.getHeader('Authorization'))")
    @PostMapping("/{id}/comments/add")
    public ResponseEntity<String> addComment(@PathVariable String id, HttpServletRequest request,
                                             @RequestBody String commentJSON) {

        try {
            ProcessComment comment = objectMapper.readValue(commentJSON, ProcessComment.class);
            comment.setAuthor(getUser(request));
            comment.setProcess(processRepository.findById(Long.parseLong(id)));
            comment.setDateWritten(new Date());
            commentRepository.save(comment);

            return ResponseEntity.ok("Comment added.");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid process ID.");
        } catch (JsonProcessingException | TransactionSystemException e) {
            return ResponseEntity.badRequest().body("Invalid process comment.");
        }
    }

    @PreAuthorize("@jwtHandler.isValidToken(#request.getHeader('Authorization'))")
    @DeleteMapping("/{processId}/comments/{commentId}/delete")
    public ResponseEntity<String> deleteComment(@PathVariable String processId, HttpServletRequest request,
                                                @PathVariable String commentId) {

        try {
            ProcessComment comment = commentRepository.findById(Long.parseLong(commentId));
            User author = comment.getAuthor();
            User requestSender = getUser(request);

            if (!author.getId().equals(requestSender.getId()) && !requestSender.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this comment.");
            }

            commentRepository.deleteById(comment.getId());
            return ResponseEntity.ok("Comment deleted.");
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid process ID.");
        } catch (TransactionSystemException e) {
            return ResponseEntity.badRequest().body("Failed to delete the comment.");
        }
    }

    //===== Helper Methods =====//

    private User getUser(HttpServletRequest request) {
        return  jwtHandler.getTokenUser(request.getHeader("Authorization"));
    }
}
