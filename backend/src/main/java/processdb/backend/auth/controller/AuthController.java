package processdb.backend.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import processdb.backend.auth.controller.requests.LoginRequest;
import processdb.backend.auth.controller.requests.RegistrationRequest;
import processdb.backend.auth.jwt.JWTHandler;
import processdb.backend.users.User;
import processdb.backend.users.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTHandler jwtHandler;

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {

        User user = new User(request.getUsername(), request.getPassword());
        userRepository.save(user);
        return ResponseEntity.ok(jwtHandler.generateToken(user));
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername());
        boolean isValidLogin = user.matchesCredentials(request.getUsername(), request.getPassword());

        if (isValidLogin)
            return ResponseEntity.ok(jwtHandler.generateToken(user));
        else
            return ResponseEntity.badRequest().build();
    }
}
