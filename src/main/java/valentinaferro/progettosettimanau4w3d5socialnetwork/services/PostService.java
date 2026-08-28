package valentinaferro.progettosettimanau4w3d5socialnetwork.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.Post;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.enums.Role;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.NotFoundException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.UnauthorizedException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.NewPostDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.repositories.PostRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // l'autore e' sempre l'utente loggato, mai un id passato dal client
    public Post create(NewPostDTO payload, User author) {
        Post newPost = new Post(payload.content(), LocalDate.now());
        newPost.setAuthor(author);
        return this.postRepository.save(newPost);
    }

    public List<Post> findAll() {
        return this.postRepository.findAll();
    }

    public Post findById(long id) {
        return this.postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post con id " + id + " non trovato."));
    }

    // modifica consentita solo all'autore del post o a un MODERATOR
    public Post update(long id, NewPostDTO payload, User currentUser) {
        Post post = this.findById(id);

        boolean isAuthor = post.getAuthor().getId().equals(currentUser.getId());
        boolean isModerator = currentUser.getRole() == Role.MODERATOR;
        if (!isAuthor && !isModerator)
            throw new UnauthorizedException("Puoi modificare solo i tuoi post");

        post.setContent(payload.content());
        return this.postRepository.save(post);
    }
}
