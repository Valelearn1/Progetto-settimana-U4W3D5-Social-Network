package valentinaferro.progettosettimanau4w3d5socialnetwork.payloads;

import jakarta.validation.constraints.NotBlank;

public record ChangeRoleDTO(
        @NotBlank(message = "Il ruolo e' obbligatorio")
        String role
) {
}
