package processdb.backend.auth.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import processdb.backend.auth.controller.requests.LoginRequest;
import processdb.backend.auth.controller.requests.RegistrationRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {

        return ResponseEntity.ok("TODO: Registration");
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        return ResponseEntity.ok("TODO: Login");
    }
}
