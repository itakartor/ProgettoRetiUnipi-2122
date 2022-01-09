package server;

import config.ConfigField;
import server.registerRMI.RegisterInterfaceRemote;
import server.registerRMI.RegisterInterfaceRemoteImpl;
import server.registerRMI.TaskSave;
import server.resource.*;
import server.task.*;
import util.UtilFile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerMain {

    private static final int BUFFER_SIZE = 5000;
    private static final ListUsersConnessi listUsersConnessi = new ListUsersConnessi(); // associa gli id dei client con gli utenti loggati
    private static final Path pathFileConfig = Paths.get("src/main/java/config/serverConfig.txt");

    public static void main(String[] args) throws IOException {

        // configurazione del server
        ConfigField configServer = UtilFile.readConfigurationServer(pathFileConfig.toString()); // legge la configurazione ma non la applica
        // System.out.println(configServer.toString());
        String pathFile = configServer.getPathFile();
        String nameFilePosts = configServer.getNameFilePosts();
        String nameFileUsers = configServer.getNameFileUsers();
        String nameFileWallets = configServer.getNameFileWallets();
        String ipMulticast = configServer.getMulticastIp();
        Integer portMulticast = configServer.getMulticastPort();
        Integer percAutore = configServer.getPercAutore();
        Integer percCuratore = configServer.getPercCuratore();
        Integer udpPort = configServer.getUdpPort();
        Integer msTimetout = configServer.getMsTimeout();
        if (percCuratore + percAutore > 100)
        {
            System.out.println("[ERROR]: Le percentuali preimpostate nel server config superano il 100%");
            throw new IOException("[ERROR]: Le percentuali preimpostate per le ricompense nel file serverConfig.txt superano il 100%");
        }
        //= 1919; // 6666
        int DEFAULT_PORT = configServer.getTcpPort();
        // = 6666;
        int regPort = configServer.getRegisterPort();
        String SERVER_NAME = configServer.getRegisterHost();
        // System.out.println(SERVER_NAME);
        // creazione lista dei post
        ListPost listPost = new ListPost(pathFile, nameFilePosts);
        ListWallet listWallets = new ListWallet(pathFile, nameFileWallets);
        // infatti voglio che la lista sia unica per evitare continue modifiche in tutti gli utenti
        // ed evitare problemi di sincronizzazione attraverso operazioni come voto, commento o delete


        // parte RMI
        // Creazione di un'istanza dell'oggetto remote
        RegisterInterfaceRemoteImpl remoteService = new RegisterInterfaceRemoteImpl(pathFile,nameFileUsers,listWallets);

        // thread dedito al salvataggio della lista con i vari cambiamenti, in modo tale da evitare sprechi
        Thread threadSave = new Thread(new TaskSave(msTimetout, pathFile, nameFileUsers, nameFilePosts, nameFileWallets, remoteService.getObjectListUser(), listPost, listWallets));
        threadSave.setDaemon(true); // è demone così se il programma viene terminato non aspetta
        threadSave.start();

        /* Esportazione dell'Oggetto */
        RegisterInterfaceRemote stub = (RegisterInterfaceRemote) UnicastRemoteObject.exportObject(remoteService, 0);
        /* Creazione di un registry sulla porta args[0]*/
        LocateRegistry.createRegistry(regPort);
        Registry r=LocateRegistry.getRegistry(regPort);
        /* Pubblicazione dello stub nel registry */
        r.rebind(SERVER_NAME, stub);
        // System.out.println("Server ready RMI");
        // Thread.sleep(5000);
        // System.out.println("Lista Utente "+ listUser.toString());

        // inizializzazione di un pool di thread
        ExecutorService service = Executors.newCachedThreadPool();

        // thread per il guadagno di tutti gli utenti
        Thread threadEarn = new Thread(new TaskEarn(ipMulticast, portMulticast, udpPort, percCuratore,percAutore, msTimetout, listPost, listWallets));
        threadEarn.setDaemon(true);
        threadEarn.start();

        //socket, selector e channel
        ServerSocketChannel serverChannel; //socket del server
        Selector selector;
        try { // configurazione server con creazione e bind di socket e selector
            serverChannel = ServerSocketChannel.open(); // apro socket
            ServerSocket ss = serverChannel.socket(); // la metto come socket
            InetSocketAddress address = new InetSocketAddress(DEFAULT_PORT);
            ss.bind(address); // faccio la bind
            serverChannel.configureBlocking(false); // non bloccante
            selector = Selector.open(); // apro il selettore
            serverChannel.register(selector, SelectionKey.OP_ACCEPT); // lo registro nel selector
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        while (true) { // esecuzione del server
            // System.out.println("sto prendendo il selector");
            try {
                int nChiavi = selector.select();
                // System.out.println("ho n chiavi: "+ nChiavi); // verifico quante richieste sono attive
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
            // System.out.println("ho passato la select");
            /*
            listaUtentiLoggati:
            [
                {
                    idClient:1234,
                    idUtente:null -> se possibile mettiamo il puntatore all'oggetto o una stringa che rappresenti Id oppure Username
                },
            ]
            connessione TCP tra client e server con un id dell'utente uguale a null
            vanno recuperate le key dei client registrati così da porter creare una lista con id client associato all'id dell'utente
            che viene recuperato quando avviene il login
            utente -> login username password -> se accede senza problemi l'utente il server recupera l'id Utente e lo salva nella lista utenti loggati
            utente -> logout -> metto a null il mio id utente che ho nella lista

             */
            Set<SelectionKey> readyKeys = selector.selectedKeys(); // chiavi dei client con richieste pronte
            // System.out.println("queste sono le chiavi delle task" + readyKeys);
            Iterator<SelectionKey> iterator = readyKeys.iterator(); // iteratore delle chiavi

            while (iterator.hasNext()) { // processo le richieste dei client -> login,register,nuovo post, voto, commento, ecc.
                SelectionKey key = iterator.next();
                iterator.remove();
                // System.out.println("stampo le key:  "+key);
                // rimuove la chiave dal Selected Set, ma non dal Registered Set
                // cosa deve fare il server a seconda della richiesta -> divide le task tra vari thread
                try { // leggo le task e le devo mettere in una lista per farle processare ad altri thread
                    if (key.isAcceptable()) {
                        registrazione(selector, key); // viene registrata
                    } else if (key.isReadable()) {
                        // legge il contenuto
                        // MEGA SWOTHC DELLA MORTE CON I FULMINI
                        String output; // stringa da modificare per rispondere al client
                        Comando input = leggiCanale(selector, key);
                        String idClient = input.getIdClient();
                        StringTokenizer st;
                        String tokenComando;
                        st = new StringTokenizer(input.getComando());
                        tokenComando = st.nextToken();// dovrebbe avere la stringa del comando

                        System.out.println("[SERVER]: questo è il token del comando -> " + tokenComando);
                        ByteBuffer buffer = (ByteBuffer)key.attachment();// buffer della chiave
                        // per ogni comando viene creata una task(Callable) che restituisce una risposta da inviare al client
                        switch (tokenComando)
                        {
                            case"primo":
                            {
                                // System.out.println("sono nella accettazione client");
                                System.out.println("[SERVER]: Id del client durante l'accettazione " + idClient);
                                output = idClient;
                                break;
                            }
                            case"!quit": // in caso che il client invii !quit rimuovo la sua connessione dalla lista
                            {
                                // System.out.println(listUsersConnessi.getListClientConnessi().toString());
                                Future<String> future = service.submit(new TaskLogout(idClient,listUsersConnessi)); // faccio prima la logout e poi rimuovo il client
                                listUsersConnessi.getListClientConnessi().remove(idClient);
                                // System.out.println(listUsersConnessi.getListClientConnessi().toString());
                                key.cancel();
                                try {
                                    key.channel().close();
                                } catch (IOException cex) {
                                    cex.printStackTrace();
                                }
                                continue;
                            }
                            case"login":
                            {
                                if(st.countTokens()<2)
                                {
                                    output = "[ERROR]: per effetuare il login e' necessario un username e una password validi";
                                }
                                else {
                                    String username = st.nextToken();
                                    String password = st.nextToken();
                                    Future<String> future = service.submit(new TaskLogin(username, password, remoteService.getListUser(), idClient, listUsersConnessi));
                                    // output = "ti sei loggato " + username +" " + password + " " + idClient;
                                    output = future.get();
                                    // System.out.println(output);
                                }
                                break;
                            }
                            case"logout":
                            {
                                Future<String> future = service.submit(new TaskLogout(idClient,listUsersConnessi));
                                output = future.get();
                                break;
                            }
                            case"help":
                            {
                                StringBuilder string = new StringBuilder("Comandi per il server: \n");
                                string.append("-help : mostra i comandi utilizzabili sulla piattaforma\n");
                                string.append("-!quit : chiude immediatamente il client sloggando dall'utente in caso che sia loggato\n");
                                string.append("-register <USERNAME> <PASSWORD> <TAG1> [TAG2] [TAG3] : e' possibile registrare l'utente,\n" +
                                        " ma bisogna dare un username univoco,\n" +
                                        " una password di minimo 6 caratteri\n" +
                                        " e minimo 1 tag o massimo 5 tags\n");
                                string.append("-login <USERNAME> <PASSWORD>: l'utente viene connesso,\n" +
                                        " senza questa operazione non sara' possibile eseguire le altre operazioni sottostanti\n");
                                string.append("-logout : l'utente si disconnetera' dal client\n");
                                string.append("-list users : mostrera' gli utenti che hanno almeno un tag in comune con l'utente connesso\n");
                                string.append("-list followers : mostrera' le persone seguite dall'utente connesso\n");
                                string.append("-list following : mostrera' le persone che seguono l'utente connesso\n");
                                string.append("-follow <USERNAME> : l'utente iniziera' ad avere continui aggiornamenti nei propri feed da parte dell'utente seguito\n");
                                string.append("-blog : verranno mostrati i post pubblicati dall'utente connesso\n");
                                string.append("-post \"<TITLE>\" \"<CONTENT>\" : l'utente pubblichera' un post nel proprio blog che potra' essere visto dai seguaci,\n" +
                                        "attenzione bisogna rispettare la sintassi del comando per scrivere piu' parole nei campi\n");
                                string.append("-show post <IDPOST> : verra' mostrato ogni informazione relativa al post indicato attraverso l'id\n");
                                string.append("-show feed : verranno mostrati i post degli utenti seguiti dall'utente connesso\n");
                                string.append("-delete <IDPOST> : verra' cancellato un post di cui si e' autori\n");
                                string.append("-rewin <IDPOST> : verra' condiviso un post di un altro utente sul proprio blog per dargli maggiore visibilita'\n");
                                string.append("-rate <IDPOST> <+1 o -1>: verra' votato un post di un altro utente, il post puo' essere votato una volta sola e non posso votare i post di cui sono proprietario\n");
                                string.append("-comment <IDPOST> \"<CONTENT>\": verra' commentato un post di un altro utente e non posso commentare i post di cui sono proprietario");
                                string.append("-wallet: restituisce le informazioni relative al proprio wallet ");
                                string.append("-wallet btc: restituisce il valore del wallet convertito in bitcoin");
                                output = string.toString();
                                // System.out.println("dentro allo swithc "+output);
                                break;
                            }
                            case"list":
                            {
                                switch (st.nextToken())
                                {
                                    case"users":
                                    {
                                        Future<String> future = service.submit(new TaskListUsers(remoteService.getListUser(),listUsersConnessi,idClient));
                                        output = future.get();
                                        System.out.println(output);
                                        break;
                                    }
                                    case"followers":
                                    {
                                        Future<String> future = service.submit(new TaskListFollowers(remoteService.getListUser(),listUsersConnessi,idClient));
                                        output = future.get();
                                        System.out.println(output);
                                        break;
                                    }
                                    case"following":
                                    {
                                        Future<String> future = service.submit(new TaskListFollowing(remoteService.getListUser(),listUsersConnessi,idClient));
                                        output = future.get();
                                        // System.out.println(output);
                                        break;
                                    }
                                    default:
                                    {
                                        output="[ERROR]:lista non trovata";
                                    }
                                }
                                break;
                            }
                            case"follow": // follow username
                            {
                                String username = st.nextToken();
                                Future<String> future = service.submit(new TaskFollowUser(username, idClient, remoteService.getObjectListUser(), listUsersConnessi));
                                output = future.get();
                                if(listUsersConnessi.getListClientConnessi().get(idClient) != null)
                                    remoteService.update("l'utente " + listUsersConnessi.getListClientConnessi().get(idClient).getUsername()+" ha iniziato a seguirti",listUsersConnessi,username);
                                break;
                            }
                            case"unfollow": // follow username idClient
                            {
                                String username = st.nextToken();
                                Future<String> future = service.submit(new TaskUnfollowUser(username, idClient, remoteService.getObjectListUser(), listUsersConnessi));
                                output = future.get();
                                remoteService.update("l'utente " + listUsersConnessi.getListClientConnessi().get(idClient).getUsername()+" ha smesso di seguirti",listUsersConnessi,username);

                                break;
                            }
                            case"blog":
                            {
                                Future<Set<Post>> future = service.submit(new TaskBlog(listUsersConnessi,idClient, listPost));
                                // se mi ritorna null vuol dire che ci sono stati dei problemi
                                if(future.get() != null) {// formattazione output
                                    StringBuilder result = new StringBuilder("    My Blog                \n");
                                    result.append(" Id Post    |       Titolo  \n-------------------------------------------------------\n");
                                    if (future.get().isEmpty()) {
                                        result.append("         Non hai posts        ");
                                    } else {
                                        for (Post p : future.get()) {
                                            result.append(p.toString());
                                        }
                                    }
                                    output = result.toString();
                                }
                                else
                                    output = "[SERVER]: L'utente non e' loggato";

                                break;
                            }  // post <title> <content>.
                            case"post":
                            {
                                    // ci sono 4 token: 1)comando,2)titolo,3)uno spazio,4)contenuto
                                    // in caso che il comando sia post ritokenizzo l'input perchè è diverso
                                    // per dare la possibilità di scrivere più parole nel titolo e nel contenuto
                                    // System.out.println("sono nel ramo else");
                                    st = new StringTokenizer(input.getComando(), "\"");
                                    if(st.countTokens()<4)
                                    {
                                        output = "[ERROR]: per effetuare la funzione post e' necessario un titolo e un contenuto valido tra \"\" ";
                                    }
                                    else
                                    {
                                        tokenComando = st.nextToken(); // non è utilizzato e dovrebbe contenere sempre la parola "post "
                                        String title = st.nextToken();
                                        st.nextToken(); // uno spazio vuoto che non serve
                                        String content = st.nextToken();

                                        // System.out.println("id del client parsato: " +idClient);
                                        Future<String> future = service.submit(new TaskNewPost(listUsersConnessi, idClient, title, content, listPost));
                                        output = future.get();
                                    }
                                break;
                            }
                            case"show":
                            {
                                switch (st.nextToken())
                                {
                                    case"post":
                                    {
                                        // System.out.println("questo è il counter "+st.countTokens());
                                        if(st.countTokens()<1)
                                        {
                                            output = "[ERROR]: per effetuare la funzione show post e' necessario un idPost valido";
                                        }
                                        else {
                                            String idPost = st.nextToken();

                                            Future<String> future = service.submit(new TaskShowPost(listUsersConnessi,idClient,idPost, listPost));
                                            output = future.get();
                                        }

                                        break;
                                    }
                                    case"feed": // è stata cambiata rispetto alle altre task per riutilizzare la funzione di raccolta dei feed
                                    {

                                        Future<Set<Post>> future = service.submit(new TaskShowFeed(listUsersConnessi,idClient, listPost));
                                        StringBuilder result;
                                        if(future.get() != null)
                                        {
                                            // formattazione output
                                            result = new StringBuilder("       Feed                \n");
                                            result.append("  ID Post   |      Autore     |    Title  \n-------------------------------------------------------\n");
                                            if(future.get().isEmpty())
                                            {
                                                result.append("         Non hai feed        ");
                                            }
                                            else
                                            {
                                                for (Post p: future.get()) {
                                                    result.append(p.toString());
                                                }
                                            }
                                        }
                                        else
                                            result = new StringBuilder("[SERVER]:Richiesta show feed fallita");

                                        output = result.toString();
                                        break;
                                    }
                                    default:
                                    {
                                        output="[ERROR]:Comando errato";
                                    }
                                }
                                break;
                            }
                            case"delete":
                            {
                                if(st.countTokens()<1)
                                {
                                    output = "[ERROR]: per effetuare la funzione delete e' necessario idPost valido ";
                                }
                                else {
                                    String idPost = st.nextToken();
                                    Future<String> future = service.submit(new TaskDelete(listUsersConnessi, idClient, idPost, listPost));
                                    output = future.get();
                                }
                                break;
                            }
                            case"rewin":
                            {
                                if(st.countTokens()<1)
                                {
                                    output = "[ERROR]: per effetuare la funzione rewin e' necessario idPost valido ";
                                }
                                else {
                                    String idPost = st.nextToken();
                                    Future<String> future = service.submit(new TaskRewin(listUsersConnessi, idClient, idPost, listPost));
                                    output = future.get();
                                }
                                break;
                            }
                            case"rate":
                            {
                                if(st.countTokens()<2)
                                {
                                    output = "[ERROR]: per effetuare la funzione rate e' necessario idPost e un voto[+1,-1] valido ";
                                }
                                else {
                                    String idPost = st.nextToken();
                                    String vote = st.nextToken();
                                    if (!vote.equals("+1") && !vote.equals("-1")) {
                                        output = "[SERVER]: Voto non riconosciuto, forse volevo scrivere +1 o -1";
                                        break;
                                    }
                                    Set<Post> feed = service.submit(new TaskShowFeed(listUsersConnessi, idClient, listPost)).get(); // ottengo i feed riutilizzando una task
                                    Future<String> future = service.submit(new TaskRate(listUsersConnessi, idClient, listPost, vote, feed, idPost));
                                    output = future.get();
                                }
                                break;
                            }
                            case"comment":
                            {

                                if(st.countTokens()<2)
                                {
                                    output = "[ERROR]: per effetuare la funzione comment e' necessario idPost e un commento valido ";
                                }
                                else {
                                    String idPost = st.nextToken(); // dopo aver preso l'idPost ritokenizzo per ottenre il commento tra ""
                                    st = new StringTokenizer(input.getComando(), "\""); // comment idPost "comment"
                                    st.nextToken(); // parte inutile gia verificata
                                    String comment = st.nextToken();
                                    if(comment.length()<1) // commento vuoto
                                    {
                                        output = "[SERVER]: Commento non valido";
                                        break;
                                    }
                                    Set<Post> feed = service.submit(new TaskShowFeed(listUsersConnessi, idClient, listPost)).get(); // ottengo i feed riutilizzando una task
                                    Future<String> future = service.submit(new TaskComment(listUsersConnessi, idClient, listPost, comment, feed, idPost));
                                    output = future.get();
                                }
                                break;
                            }
                            case"wallet":
                            {
                                String token;
                                try
                                {
                                    token = st.nextToken();
                                    if(token.equals("btc"))// se è pieno ed uguale a btc eseguo l'altra funzione
                                    {
                                        Future<String> future = service.submit(new TaskWalletBTC(listUsersConnessi, idClient,listWallets));
                                        output = future.get();
                                    }
                                    else // se il comando è stato inserito in modo errato
                                    {
                                        output = "[ERROR]: Comando wallet errato";
                                    }
                                }
                                catch (NoSuchElementException e) // se non trova il token dopo la parola wallet fa la taskWallet, altrimenti controlla per wallet btc
                                {
                                    Future<String> future = service.submit(new TaskWallet(listUsersConnessi, idClient,listWallets));
                                    output = future.get();
                                }
                                break;
                            }
                            default:
                            {
                                output = "[SERVER]: Comando non riconosciuto";
                                System.out.println("[SERVER]:Comando non riconosciuto -> " + tokenComando);
                            }
                        }
                        // meccanismo di risposta al client
                        if (output == null)
                        {
                            output = "[ERROR]:OUTPUT NULL";
                        }
                        // System.out.println("questo e' l'output " +output);
                        // System.out.println(buffer.toString());
                        buffer.put(output.getBytes(StandardCharsets.UTF_8));
                        // System.out.println(buffer.toString());
                        buffer.flip();
                        // System.out.println(buffer.toString());
                    }else if(key.isWritable()){
                        scritturaCanale(selector, key);// scrive il contenuto
                    }
                } catch (IOException ex) {

                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        cex.printStackTrace();
                    }
                } catch (ExecutionException | InterruptedException | NoSuchElementException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void registrazione(Selector sel, SelectionKey selectionKey) throws IOException{
        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel(); // prendo la key del client per recuperare il socket
        SocketChannel client = server.accept();                           // accetto la richiesta del server
        String clientId = client.socket().getInputStream().toString().substring(client.socket().getInputStream().toString().lastIndexOf("@") + 1 ); // id univoco del client
        System.out.println("[SERVER]:Connessione accettata dal client: " + clientId);
        listUsersConnessi.putListClientConnessi(clientId,null);// inizializzo l'elemento della lista dove segnerò l'utente loggato
        ByteBuffer clientByteBuffer = ByteBuffer.wrap(new byte[BUFFER_SIZE]);
        client.configureBlocking(false);                                    // dico che non deve essere bloccante
        client.register(sel, SelectionKey.OP_READ, clientByteBuffer);       // gli comunico che si deve aspettare qualcosa dal client
    }

    private static Comando leggiCanale(Selector sel, SelectionKey selectionKey) throws IOException {
        // System.out.println("sto leggendo dal canale");
        StringBuilder inputString = new StringBuilder(); // stringbuilder per richiesta/risposta (per fare l'append)
        SocketChannel client = (SocketChannel) selectionKey.channel();               // prendo il socket del client
        String clientId = client.socket().getInputStream().toString().substring(client.socket().getInputStream().toString().lastIndexOf("@") + 1 ); // id univoco del client
        // System.out.println("ID client? --> "+ client.socket().getInputStream().toString());
        client.configureBlocking(false);
        // System.out.println("fino a qui");
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();                  // prendo l'attachment

        int nCaratteriLetti = client.read(buffer); // legge dal client

        // System.out.println("[SERVER]:ho letto tot caratteri" + nCaratteriLetti);
        if(nCaratteriLetti < 0){
            client.close();
            throw new IOException("channel read failed.");
        }
        else
        {
            // la lettura porta tutto nel buffer locale e poi devo portarlo
            // carattere per carattere nello string builder
            buffer.flip();
            while (buffer.hasRemaining()) inputString.append((char) buffer.get()); // legge cose

            // System.out.println("[SERVER]:HO RICEVUTO DAL CLIENT QUESTO: "+inputString.toString());
            buffer.clear();
            // buffer.flip();//pronto per scrivere
            client.register(sel, SelectionKey.OP_WRITE, buffer); // dico al server che dovrà scrivere qualcosa al client
            return new Comando(clientId,inputString.toString());
        }
    }

    private static void scritturaCanale(Selector sel, SelectionKey selectionKey) throws IOException {
        StringBuilder inputString = new StringBuilder();
        SocketChannel client;
        client = (SocketChannel) selectionKey.channel();
        client.configureBlocking(false);
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
        // System.out.println("scrittura "+buffer.toString());
        while (buffer.hasRemaining()) inputString.append((char) buffer.get());
        System.out.println("[DEBUG]: Scrittura del server: "+inputString);
        buffer.flip();
        client.write(buffer); // scrive al client quello che c'e nel buffer
        buffer.clear();
        client.register(sel, SelectionKey.OP_READ, buffer);  // aspetta di leggere qualcosa dal client
    }
}
