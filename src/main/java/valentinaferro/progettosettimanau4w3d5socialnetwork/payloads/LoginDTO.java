package valentinaferro.progettosettimanau4w3d5socialnetwork.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "L'email e' obbligatoria")
        @Email(message = "L'email deve essere valida")
        String email,

        @NotBlank(message = "La password e' obbligatoria")
        String password
) {
}
