package valentinaferro.progettosettimanau4w3d5socialnetwork.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
        @NotBlank(message = "Lo username e' obbligatorio")
        @Size(min = 2, max = 30, message = "Lo username deve essere tra 2 e 30 caratteri")
        String username,

        @NotBlank(message = "Il nome completo e' obbligatorio")
        String fullName,

        @NotBlank(message = "L'email e' obbligatoria")
        @Email(message = "L'email deve essere valida")
        String email,

        @NotBlank(message = "La password e' obbligatoria")
        @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
        String password
) {
}
