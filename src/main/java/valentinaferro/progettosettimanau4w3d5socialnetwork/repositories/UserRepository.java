package valentinaferro.progettosettimanau4w3d5socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
