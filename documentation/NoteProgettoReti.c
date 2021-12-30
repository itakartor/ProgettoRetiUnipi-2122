Progetto di reti

Cose da fare:
-Server multiThread con selector per gestire più client
-Multi cast degli utenti ogni volta che il thread inizia a controllare i post per aggiornare i portafogli
-gestione portafoglio con thread associato
-gestione post
-gestione utenti
-serializzazione e deserelizzazione
-gestione dei tag durante la registrazione dell'utente 
-gestione password con hash+seme

Oggetti:

-Utente
+info di base -> chieste all'utente durante la registrazione
+Id univoco -> autogenerato
+lista di tag -> chieste all'utente durante la registrazione (MAX 5)
				(esempio: sport musica cinema)
+listaFollowing -> persone che l'utente segue
+listaDiPostCreati -> all'inizio è vuota
+listaDiPostDegliAltri -> all'inizio è vuota e si riempie dopo ogni follow()
.follow()-> un utente x può seguire un utente y
.unfollow() -> un utente x smette di seguire 
.feed() -> l'utente x vede solo i contenuti degli utenti che segue
.bacheca() -> l'utente x può vedere la lista dei post di cui è autore
.newPost() -> l'utente x crea un nuovo post b 
.vote() -> voto un post in modo positivo o negativo
.comment() -> aggiungo un comment a ad un post b
.getListaUtenti() -> ottiene la lista degli utenti che hanno almeno un tag
						in comune con l'utente 
.rewin() -> riPosto un post di un utente V che seguo per dargli più visibilità
			ha senso che il post appaia duplice su più utenti?
			cioè se sono U e seguo V e H, poi H riPosta il post di V
			devo vedere tutti e due i post?
.getPost(int idPost) -> serve per far vedere uno dei post che ho 
.getPortafoglio()
.deletePost()

-Post
+infoVarie-> messe dall'utente
+counterVotePositive
+couterVoteNegative
+idUnivoco -> autoGenerato
+proprietario -> utente che ha scritto il post
+condivisori -> utenti che hanno rewin il post 
.votePositive() 
.voteNegative()  
.newComment(string comment)

-Portafoglio
+importo
+idUnivoco
+idProprietario
.convertWincoinBitcoin() -> converto i wincoin in bitcoin

-Server
+importoRicompensa -> importo monetario da dividere tra curatori e autore
+RicompensaCuratore -> percentuale della ricompensa da dare agli
						Utenti che lasciano commenti o voti positivi
+RicompensaAutore -> percentuale della ricompensa che deve essere data
						al proprietario del post, se il post suscita
						interesse
+tempoDiControllo -> ogni quanto un thread o più controlla tutti i post
					per aggiornare le ricompense degli utenti
.earn() -> guadagno dei punti se faccio un comment(), un vote()
			oppure se i suoi post ottengono votes (solo positivi)
			o comments da altri utenti
-Client Multicast per gli aggiornamenti
+voglio avere un Utente oppure un id o qualcosa che mi faccia ottenere alcune info senza doverle richiedere al server?
.register(username, password, lista di tag[non ci sono dei tag predefiniti, caratteri minuscoli]) -> registrazione di un utente controlla che username sia unico fatta con RMI
.login()
.logout()

.controlloFollower() -> RMI callback bisogna notificare all'utente se una follower si aggiunge o si rimuove



Schema OneNote: https://unipiit-my.sharepoint.com/:o:/g/personal/c_vitiello6_studenti_unipi_it/En60zfZYjpZPn7rKglzqFvQBGaz6IyoiB6FuIHxr7VLv_w




-Register utente -> usage- register <USERNAME> <PASSWORD> <TAG1> [<TAG2>] [<TAG3>] [<TAG4>] [<TAG5>]
Controlli:
-usarname vuoto // non verificabile
-password vuota // non verificabile
-password non rispetta i requisiti -> password almeno 8 caratteri
-minimo 1 tags
-massimo 5 tags 
-username univoco 


Cosa manca:
-parte grossa del follower e del to follow
-Wallet
-cacchiate sui post
-controllare errori vari ed eventuali: per esempio se l'utente si dimentica dei parametri



