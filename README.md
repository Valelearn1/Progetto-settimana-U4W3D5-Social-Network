# Progetto settimana U4W3D5 — Social Network

API REST su entità `User`, `Post`, `Like` con autenticazione JWT e autorizzazione per ruolo / proprietà della risorsa.

## Configurazione

Le credenziali del database sono in `src/main/resources/application.properties`.
La secret per la firma dei token JWT è in `src/main/resources/env.properties`, caricato
all'avvio tramite `spring.config.import`.

> In un progetto reale `env.properties` sarebbe in `.gitignore` e la secret verrebbe
> passata come variabile d'ambiente. Qui è versionata di proposito, per semplificare
> la correzione del progetto.

Requisiti: Java 17+, PostgreSQL. Creare un database e allineare `spring.datasource.url`.
Avvio: `./mvnw spring-boot:run` (porta `3001`).

## Ruoli

`MEMBER` (default alla registrazione) e `MODERATOR`.

## Regole di autorizzazione

| Operazione | Regola | Perché |
| --- | --- | --- |
| registrazione, login | pubblica | serve prima di avere un account |
| lettura post (tutti / per id) | autenticato | contenuto riservato agli iscritti |
| creazione post | autenticato, l'autore = utente loggato | ognuno pubblica a nome proprio |
| aggiornamento post | autore del post **oppure** `MODERATOR` | proprietà della risorsa + moderazione |
| cambio ruolo utente | solo `MODERATOR` | operazione amministrativa |
| like / unlike | autenticato; un like per utente per post | vincolo `UniqueConstraint` sull'entità |
