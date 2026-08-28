package valentinaferro.progettosettimanau4w3d5socialnetwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.Post;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.PostLike;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.NotFoundException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.ValidationException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.repositories.PostLikeRepository;

@Service
public class LikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostService postService;

    // aggiunge un like: un utente non puo' mettere piu' di un like allo stesso post
    public PostLike addLike(long postId, User user) {
        Post post = this.postService.findById(postId);

        if (this.postLikeRepository.existsByUserAndPost(user, post))
            throw new ValidationException("Hai gia' messo like a questo post");

        return this.postLikeRepository.save(new PostLike(user, post));
    }

    // rimuove un like gia' messo
    public void removeLike(long postId, User user) {
        Post post = this.postService.findById(postId);

        PostLike like = this.postLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new NotFoundException("Non hai messo like a questo post"));

        this.postLikeRepository.delete(like);
    }
}
