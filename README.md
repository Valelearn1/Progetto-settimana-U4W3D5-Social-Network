# Progetto settimana U4W3D5 — Social Network

API REST costruite sopra le entità `User`, `Post`, `Like` della settimana precedente,
con autenticazione JWT e autorizzazione basata su **ruolo** e su **proprietà della risorsa**.

## Stack

- Java 17, Spring Boot 4.1
- Spring Web, Spring Data JPA, Spring Security
- PostgreSQL
- JWT (libreria `jjwt` 0.13)
- BCrypt per l'hashing delle password
- Lombok, Bean Validation

## Configurazione e avvio

1. Creare un database PostgreSQL (nel progetto si chiama `U4W3D5`).
2. In `src/main/resources/application.properties` allineare:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/U4W3D5
   spring.datasource.username=postgres
   spring.datasource.password=...
   ```
   Le tabelle vengono create/aggiornate da Hibernate (`spring.jpa.hibernate.ddl-auto=update`).
3. La secret per la firma dei token JWT è in `src/main/resources/env.properties`
   (`jwt.secret=...`), caricato all'avvio tramite `spring.config.import`.

   > In un progetto reale `env.properties` sarebbe in `.gitignore` e la secret verrebbe
   > passata come variabile d'ambiente. Qui è versionata di proposito, per semplificare
   > la correzione.
4. Avvio: `./mvnw spring-boot:run` — il server ascolta sulla porta **3001**.

## Autenticazione

- `POST /auth/register` crea un utente con password hashata (BCrypt) e ruolo `MEMBER`.
- `POST /auth/login` verifica le credenziali e restituisce `{ "accessToken": "<JWT>" }`.
- Ogni richiesta agli endpoint protetti deve portare l'header
  `Authorization: Bearer <accessToken>`.
- Il token contiene nel payload l'id dell'utente (`subject`) e una scadenza di 1 ora.
  Ad ogni richiesta un filtro (`JWTAuthFilter`) verifica firma e scadenza, ricarica
  l'utente dal DB e lo mette nel `SecurityContext` (con il ruolo come *authority*).
- L'app è **stateless**: nessuna sessione lato server.

## Ruoli

`MEMBER` (assegnato di default alla registrazione) e `MODERATOR`.
Il `MODERATOR` è il ruolo con privilegi amministrativi.

### Cosa può fare ogni ruolo

| Azione | MEMBER | MODERATOR |
| --- | :---: | :---: |
| Registrazione / login | ✅ (pubblico) | ✅ (pubblico) |
| Leggere utenti / post | ✅ | ✅ |
| Creare un post | ✅ | ✅ |
| Modificare un **proprio** post | ✅ | ✅ |
| Modificare un post **di altri** | ❌ | ✅ |
| Like / unlike | ✅ | ✅ |
| Cambiare il ruolo di un utente | ❌ | ✅ |

Il MODERATOR può fare tutto ciò che fa il MEMBER, più: modificare qualsiasi post
(non solo i propri) e cambiare il ruolo di qualsiasi utente.

## Endpoint

| Metodo | Path | Auth | Ruolo | Body | Descrizione |
| --- | --- | --- | --- | --- | --- |
| POST | `/auth/register` | no | — | `NewUserDTO` | Registrazione nuovo utente |
| POST | `/auth/login` | no | — | `LoginDTO` | Login, restituisce il JWT |
| GET | `/users/me` | sì | qualsiasi | — | Profilo dell'utente loggato |
| GET | `/users` | sì | qualsiasi | — | Lista utenti |
| GET | `/users/{id}` | sì | qualsiasi | — | Utente per id |
| PUT | `/users/{id}/role` | sì | `MODERATOR` | `ChangeRoleDTO` | Cambia il ruolo di un utente |
| POST | `/posts` | sì | qualsiasi | `NewPostDTO` | Crea un post (autore = utente loggato) |
| GET | `/posts` | sì | qualsiasi | — | Tutti i post |
| GET | `/posts/{id}` | sì | qualsiasi | — | Post per id |
| PUT | `/posts/{id}` | sì | autore **o** `MODERATOR` | `NewPostDTO` | Modifica un post |
| POST | `/posts/{id}/like` | sì | qualsiasi | — | Aggiunge un like (max 1 per utente per post) |
| DELETE | `/posts/{id}/like` | sì | qualsiasi | — | Rimuove il proprio like |

### Payload

```jsonc
// NewUserDTO
{ "username": "vale", "fullName": "Valentina Ferro", "email": "vale@test.it", "password": "Password1!" }

// LoginDTO
{ "email": "vale@test.it", "password": "Password1!" }

// ChangeRoleDTO
{ "role": "MODERATOR" }   // MEMBER | MODERATOR

// NewPostDTO
{ "content": "Testo del post" }
```

## Regole di autorizzazione scelte

Per ogni operazione protetta, la regola scelta e la motivazione:

| Operazione | Regola | Meccanismo | Perché |
| --- | --- | --- | --- |
| **Registrazione / login** | pubbliche | `permitAll` in `SecurityConfig` su `/auth/**` | Servono prima di possedere un account: non possono richiedere autenticazione. |
| **Lettura utenti / post** (`GET`) | utente autenticato | regola globale `anyRequest().authenticated()` | I contenuti del social sono riservati agli iscritti, ma qualsiasi iscritto può leggerli. |
| **Creazione post** | utente autenticato; l'autore è **sempre** l'utente loggato | l'autore è preso dal `SecurityContext` (`@AuthenticationPrincipal`), non da un id nel body | Impedisce di pubblicare a nome di altri. |
| **Modifica post** | **autore del post** oppure **`MODERATOR`** | controllo di *ownership* in `PostService.update` (confronto `post.author.id` con l'utente loggato) + eccezione se non autorizzato | Ognuno gestisce i propri contenuti; il moderatore può intervenire su tutti per moderazione. |
| **Cambio ruolo di un utente** | solo **`MODERATOR`** | `@PreAuthorize("hasAuthority('MODERATOR')")` sull'endpoint | È un'azione amministrativa: assegnare privilegi non può essere alla portata di un utente normale. |
| **Like / unlike** | utente autenticato; **un solo like** per utente per post | `@UniqueConstraint(user_id, post_id)` sull'entità + controllo `existsByUserAndPost` in `LikeService` | Un like rappresenta l'apprezzamento di *quel* utente: duplicarlo non ha senso. L'unlike agisce solo sul proprio like. |

Non tutte le operazioni hanno le stesse regole: le letture bastano con l'autenticazione,
la modifica di un post combina *ownership* e ruolo, il cambio ruolo è puramente *role-based*.

## Gestione degli errori

Un `@RestControllerAdvice` (`ErrorHandlers`) uniforma le risposte d'errore nel formato
`{ "message": "...", "timestamp": "..." }`:

| Situazione | Status |
| --- | --- |
| Payload non valido, email già in uso, ruolo inesistente, like duplicato | `400` |
| Credenziali di login errate | `401` |
| Ruolo insufficiente (`@PreAuthorize` fallito) | `403` |
| Utente / post inesistente | `404` |
| Errore non previsto | `500` |

## Note e limiti noti

- Se il token è **assente o scaduto** su un endpoint protetto la risposta è `403`
  (l'eccezione è sollevata dentro il filtro, che gira prima del `@RestControllerAdvice`).
- Resta attiva la password di sviluppo generata da Spring Security (log all'avvio):
  non è usata da nessun endpoint, l'autenticazione passa solo dal JWT.

## Materiale di consegna

- Questo `README.md`
- Collezione Postman con le richieste testate (file `.json` nel repository)
- Screenshot delle principali richieste/risposte Postman
- Screenshot dei dati nelle tabelle del database
