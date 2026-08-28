package valentinaferro.progettosettimanau4w3d5socialnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET /users, GET /users/{id} e cambio ruolo: in arrivo nei prossimi passi
}
