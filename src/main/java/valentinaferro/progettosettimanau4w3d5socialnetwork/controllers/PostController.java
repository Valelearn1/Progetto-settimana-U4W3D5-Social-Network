package valentinaferro.progettosettimanau4w3d5socialnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.Post;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.NewPostDTO;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody NewPostDTO payload, @AuthenticationPrincipal User currentUser) {
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
    public Post updatePost(@PathVariable long id, @RequestBody NewPostDTO payload, @AuthenticationPrincipal User currentUser) {
        return postService.update(id, payload, currentUser);
    }
}
