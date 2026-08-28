package valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import valentinaferro.progettosettimanau4w3d5socialnetwork.payloads.ErrorsDTO;

import java.time.LocalDateTime;

@RestControllerAdvice // cattura le eccezioni lanciate dai controller/service
public class ErrorHandlers {

    // dati non validi: email gia' in uso, ruolo inesistente, like duplicato...
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsDTO handleValidationEx(ValidationException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    // credenziali sbagliate al login
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public ErrorsDTO handleUnauthorizedEx(UnauthorizedException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    // un @PreAuthorize fallito (es. non-MODERATOR che prova a cambiare un ruolo)
    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorsDTO handleForbiddenEx(AuthorizationDeniedException ex) {
        return new ErrorsDTO("Non hai i permessi per accedere a questa risorsa", LocalDateTime.now());
    }

    // risorsa inesistente: utente/post con quell'id non trovato
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorsDTO handleNotFoundEx(NotFoundException ex) {
        return new ErrorsDTO(ex.getMessage(), LocalDateTime.now());
    }

    // rete di sicurezza: qualsiasi altra eccezione non gestita sopra
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ErrorsDTO handleGenericEx(Exception ex) {
        ex.printStackTrace(); // per non perdere lo stack trace in console e poter debuggare
        return new ErrorsDTO("Errore interno del server", LocalDateTime.now());
    }
}
