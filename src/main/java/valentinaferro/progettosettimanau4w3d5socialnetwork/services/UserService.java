package valentinaferro.progettosettimanau4w3d5socialnetwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.enums.Role;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.NotFoundException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.ValidationException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.NewUserDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.repositories.UserRepository;

import java.util.List;

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

    // metodo per cercare l'utente per id
    public User findById(long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente con id " + id + " non trovato."));
    }

    // lista di tutti gli utenti
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    // cambio ruolo di un utente esistente
    public User changeRole(long id, String newRole) {
        User user = this.findById(id);
        try {
            user.setRole(Role.valueOf(newRole.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Ruolo '" + newRole + "' non valido. Valori ammessi: MEMBER, MODERATOR");
        }
        return this.userRepository.save(user);
    }
}
