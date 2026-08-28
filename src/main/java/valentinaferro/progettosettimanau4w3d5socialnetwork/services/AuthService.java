package valentinaferro.progettosettimanau4w3d5socialnetwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.UnauthorizedException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.LoginDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.security.JWTTools;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTools jwtTools;

    public String checkCredentialsAndGenerateToken(LoginDTO payload) {
        User user = userService.findByEmail(payload.email());
        if (passwordEncoder.matches(payload.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizedException("Credenziali non valide.");
        }
    }
}
