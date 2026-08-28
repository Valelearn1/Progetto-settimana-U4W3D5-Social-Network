package valentinaferro.progettosettimanau4w3d5socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.Post;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.PostLike;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);

    Optional<PostLike> findByUserAndPost(User user, Post post);
}
