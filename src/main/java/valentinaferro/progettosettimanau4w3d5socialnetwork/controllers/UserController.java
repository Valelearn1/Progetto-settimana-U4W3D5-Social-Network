package valentinaferro.progettosettimanau4w3d5socialnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.NewUserDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping
    public User createUser(@RequestBody NewUserDTO payload) {
        return this.userService.create(payload);
    }
}
