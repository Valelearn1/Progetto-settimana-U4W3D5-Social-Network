package valentinaferro.progettosettimanau4w3d5socialnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.LoginDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.LoginRespDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.NewUserDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.AuthService;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody NewUserDTO body) {
        return userService.create(body);
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody LoginDTO body) {
        String token = authService.checkCredentialsAndGenerateToken(body);
        return new LoginRespDTO(token);
    }
}
