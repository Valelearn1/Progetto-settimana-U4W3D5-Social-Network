package valentinaferro.progettosettimanau4w3d5socialnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.ValidationException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.LoginDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.LoginRespDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.NewUserDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.AuthService;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.UserService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Validated NewUserDTO payload, BindingResult validationResult) {
        checkValidation(validationResult);
        return userService.create(payload);
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody @Validated LoginDTO payload, BindingResult validationResult) {
        checkValidation(validationResult);
        String token = authService.checkCredentialsAndGenerateToken(payload);
        return new LoginRespDTO(token);
    }

    // se il payload non rispetta i vincoli dei DTO -> 400 con i messaggi di errore
    private void checkValidation(BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            String messaggi = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new ValidationException("Payload non valido: " + messaggi);
        }
    }
}
