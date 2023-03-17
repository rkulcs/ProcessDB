package processdb.backend.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import processdb.backend.users.User;

import java.util.Date;

@Component("jwtHandler")
public class JWTHandler {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expirationInMillis}")
    private int expirationInMillis;

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationInMillis))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isValidToken(String auth) {

        try {
            String token = auth.split(TOKEN_PREFIX)[1];
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
