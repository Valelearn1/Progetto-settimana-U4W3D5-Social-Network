# Progetto-settimana-U4W3D5-Social-Network

# Progetto-settimana-U4W3D5-Social-Network

Regole di autorizzazione (bozza da mettere poi nel README)

| Operazione | Regola | Perché |
| --- | --- | --- |
| registrazione, login | pubblica | serve prima di avere un account |
| lettura post (tutti / per id) | autenticato | contenuto riservato agli iscritti |
| creazione post | autenticato, l'autore = utente loggato | ognuno pubblica a nome proprio |
| aggiornamento post | autore del post oppure MODERATOR | proprietà della risorsa + moderazione |
| cambio ruolo utente | solo MODERATOR | operazione amministrativa |
| like / unlike | autenticato; un like per utente per post | vincolo UniqueConstraint già presente |
