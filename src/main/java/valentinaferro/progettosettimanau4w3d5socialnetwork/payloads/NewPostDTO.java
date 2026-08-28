package valentinaferro.progettosettimanau4w3d5socialnetwork.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewPostDTO(
        @NotBlank(message = "Il contenuto e' obbligatorio")
        @Size(max = 500, message = "Il contenuto non puo' superare i 500 caratteri")
        String content
) {
}
