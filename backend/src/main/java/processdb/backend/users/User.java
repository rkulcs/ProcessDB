package processdb.backend.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;

    public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public User() {}

    public User(String username, String password) {

        this.username = username;
        setPassword(password);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public boolean matchesCredentials(String username, String rawPassword) {
        return (this.username.equals(username) && PASSWORD_ENCODER.matches(rawPassword, getPassword()));
    }
}
