package processdb.backend.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import processdb.backend.auth.controller.requests.LoginRequest;
import processdb.backend.auth.controller.requests.RegistrationRequest;
import processdb.backend.auth.jwt.JWTHandler;
import processdb.backend.users.User;
import processdb.backend.users.UserRepository;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTHandler jwtHandler;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {

        if (request.getPassword() == null || !request.getPassword().equals(request.getPasswordConfirmation()))
            return ResponseEntity.badRequest().body("Password confirmation does not match password.");

        try {
            User user = new User(request.getUsername(), request.getPassword());
            userRepository.save(user);
            return ResponseEntity.ok(jwtHandler.generateToken(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to register user.");
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername());
        boolean isValidLogin = (user == null) ? false
                                              :user.matchesCredentials(request.getUsername(), request.getPassword());

        if (isValidLogin)
            return ResponseEntity.ok(jwtHandler.generateToken(user));
        else
            return ResponseEntity.badRequest().body("Failed to log in.");
    }
}
