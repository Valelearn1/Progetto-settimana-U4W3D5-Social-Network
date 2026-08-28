package valentinaferro.progettosettimanau4w3d5socialnetwork.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import valentinaferro.progettosettimanau4w3d5socialnetwork.enums.Role;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
// non serializzare in JSON i campi tecnici che arrivano da UserDetails
@JsonIgnoreProperties({"authorities", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String fullName;

    private String email;

    @JsonIgnore // la password non compare mai nel JSON di risposta
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String username, String fullName, String email) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
    }

    // --- UserDetails ---
    // il ruolo diventa una "authority" col suo stesso nome ("MEMBER" / "MODERATOR"),
    // quella che i @PreAuthorize("hasAuthority('MODERATOR')") vanno a controllare
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    // getUsername() e getPassword() li genera gia' Lombok con @Getter
}
