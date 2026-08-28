package valentinaferro.progettosettimanau4w3d5socialnetwork.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // senza questa i @PreAuthorize sugli endpoint non funzionano
public class SecurityConfig {

    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // niente form di login HTML di default
        httpSecurity.formLogin(formLogin -> formLogin.disable());
        // CSRF inutile con autenticazione a token JWT
        httpSecurity.csrf(csrf -> csrf.disable());
        // JWT e' stateless: nessuna sessione lato server
        httpSecurity.sessionManagement(sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // /auth/** pubblici (register, login); tutto il resto richiede autenticazione
        httpSecurity.authorizeHttpRequests(req -> req
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated());
        // il nostro filtro JWT gira prima del filtro standard di Spring Security
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // abilita la configurazione CORS definita sotto
        httpSecurity.cors(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // strength 12: piu' alto = hashing piu' lento e piu' sicuro
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // whitelist dei frontend autorizzati a chiamare il backend (senza slash finale)
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
