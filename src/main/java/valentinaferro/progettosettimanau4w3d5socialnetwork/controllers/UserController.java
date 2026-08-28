package valentinaferro.progettosettimanau4w3d5socialnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.ChangeRoleDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // l'utente attualmente loggato (ricavato dal token)
    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userService.findById(id);
    }

    // cambio ruolo: operazione amministrativa -> solo MODERATOR
    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public User changeRole(@PathVariable long id, @RequestBody ChangeRoleDTO payload) {
        return userService.changeRole(id, payload.role());
    }
}
