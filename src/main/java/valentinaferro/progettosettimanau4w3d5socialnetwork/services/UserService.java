package valentinaferro.progettosettimanau4w3d5socialnetwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.enums.Role;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.ValidationException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.NewUserDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User create(NewUserDTO payload) {
        // 1. Validazione, controlli vari (email già esistente)
        if (this.userRepository.findByEmail(payload.email()).isPresent())
            throw new ValidationException("L'email " + payload.email() + " è già in uso");

        // 2. Save dello user
        User newUser = new User(payload.username(), payload.fullName(), payload.email());
        newUser.setPassword(passwordEncoder.encode(payload.password()));
        newUser.setRole(Role.MEMBER);
        return this.userRepository.save(newUser);
    }

    // metodo per cercare l'utente per email
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new ValidationException("Utente con email " + email + " non trovato."));
    }
}
