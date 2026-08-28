package valentinaferro.progettosettimanau4w3d5socialnetwork.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import valentinaferro.progettosettimanau4w3d5socialnetwork.entities.User;
import valentinaferro.progettosettimanau4w3d5socialnetwork.exceptions.UnauthorizedException;
import valentinaferro.progettosettimanau4w3d5socialnetwork.services.UserService;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // 1. senza header "Authorization: Bearer ..." -> 401
        if (header == null || !header.startsWith("Bearer "))
            throw new UnauthorizedException("Inserisci il token nell'Authorization Header nel formato corretto");

        // 2. estraggo il token
        String accessToken = header.replace("Bearer ", "");

        try {
            // 3. verifico firma e scadenza
            jwtTools.verifyToken(accessToken);

            // 4. dal token ricavo l'id utente e carico l'utente dal DB
            String userId = jwtTools.getIdFromToken(accessToken);
            User currentUser = userService.findById(Long.parseLong(userId));

            // 5. metto l'utente autenticato nel SecurityContext (con le sue authority / ruolo)
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(currentUser, null, currentUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 6. token ok -> proseguo verso il controller
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new UnauthorizedException("Token non valido o scaduto, effettua di nuovo il login");
        }
    }

    // il filtro non interviene sugli endpoint pubblici di autenticazione
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
