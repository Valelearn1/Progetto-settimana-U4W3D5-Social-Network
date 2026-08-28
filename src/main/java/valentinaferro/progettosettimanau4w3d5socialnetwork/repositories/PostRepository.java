package valentinaferro.progettosettimanau4w3d5socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
