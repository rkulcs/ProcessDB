package processdb.backend.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import processdb.backend.users.User;
import processdb.backend.users.UserRepository;

import java.util.Date;

@Component("jwtHandler")
public class JWTHandler {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expirationInMillis}")
    private int expirationInMillis;

    @Autowired
    private UserRepository userRepository;

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationInMillis))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isValidAdminUser(String auth) {
        try {
            String token = getTokenFromAuthHeader(auth);
            String username = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
            User user = userRepository.findByUsername(username);
            return user.isAdmin();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidToken(String auth) {

        try {
            String token = getTokenFromAuthHeader(auth);
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getTokenFromAuthHeader(String auth) {
        return auth.split(TOKEN_PREFIX)[1];
    }
}
