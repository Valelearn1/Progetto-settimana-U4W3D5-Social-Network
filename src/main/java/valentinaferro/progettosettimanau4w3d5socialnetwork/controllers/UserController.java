package valentinaferro.progettosettimanau4w3d5socialnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.ValidationException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.ChangeRoleDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

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
    public User changeRole(@PathVariable long id, @RequestBody @Validated ChangeRoleDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messaggi = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new ValidationException("Payload non valido: " + messaggi);
        }
        return userService.changeRole(id, payload.role());
    }
}
