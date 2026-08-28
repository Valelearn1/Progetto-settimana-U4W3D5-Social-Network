package valentinaferro.progettosettimanau4w3d5socialnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.Post;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.PostLike;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.ValidationException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.NewPostDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.LikeService;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.PostService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody @Validated NewPostDTO payload, BindingResult validationResult,
                       @AuthenticationPrincipal User currentUser) {
        checkValidation(validationResult);
        return postService.create(payload, currentUser);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable long id) {
        return postService.findById(id);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable long id, @RequestBody @Validated NewPostDTO payload, BindingResult validationResult,
                           @AuthenticationPrincipal User currentUser) {
        checkValidation(validationResult);
        return postService.update(id, payload, currentUser);
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

    // --- Like ---

    @PostMapping("/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public PostLike addLike(@PathVariable long id, @AuthenticationPrincipal User currentUser) {
        return likeService.addLike(id, currentUser);
    }

    @DeleteMapping("/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable long id, @AuthenticationPrincipal User currentUser) {
        likeService.removeLike(id, currentUser);
    }
}
