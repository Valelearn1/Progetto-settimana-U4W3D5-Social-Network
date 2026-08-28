package valentinaferro.progettosettimanau4w3d5socialnetwork.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    // durata del token: 1 ora, in millisecondi
    private static final long EXPIRATION_MS = 1000 * 60 * 60;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // genera un token per l'utente passato
    public String createToken(User user) {
        return Jwts.builder()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .subject(String.valueOf(user.getId()))
                .signWith(getKey())
                .compact();
    }

    // verifica firma + scadenza: se non valido lancia un'eccezione
    public void verifyToken(String token) {
        Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token);
    }

    // estrae l'id utente (il "subject") dal token
    public String getIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


}
