package server;

import server.registerRMI.RegisterInterfaceRemote;
import server.registerRMI.RegisterInterfaceRemoteImpl;
import server.resource.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int DEFAULT_PORT = 1919;
    private static final int RegPort = 6666;
    private static final Map<String, User> listClientConnessi = new HashMap<>(); // associa gli id dei client con gli utenti loggati
    public static void main(String[] args) throws IOException {
        //LISTA UTENTI
        Set<User> listUser = new HashSet<>();//metodo per ricaricare gli utenti dal file json
        Set<String> t = new HashSet<>();
        t.add("sport");
        t.add("video");
        User user = new User("carmine",t,1);
        listUser.add(user);
        Integer counterUser = listUser.size();


        //parte RMI
        /* Creazione di un'istanza dell'oggetto EUStatsService */
        RegisterInterfaceRemoteImpl remoteService = new RegisterInterfaceRemoteImpl(listUser,counterUser);
        /* Esportazione dell'Oggetto */
        RegisterInterfaceRemote stub = (RegisterInterfaceRemote)
                UnicastRemoteObject.exportObject(remoteService, 0);
        /* Creazione di un registry sulla porta args[0]*/
        LocateRegistry.createRegistry(RegPort);
        Registry r=LocateRegistry.getRegistry(RegPort);
        /* Pubblicazione dello stub nel registry */
        r.rebind("REMOTE-SERVER", stub);
        System.out.println("Server ready RMI");
        //Thread.sleep(5000);
        //System.out.println("Lista Utente "+ listUser.toString());

        //inizializzazione di un pool di thread
        ExecutorService service = Executors.newCachedThreadPool();
        // Thread threadSave = new Thread();
        //THREAD RMI -> LISTA UTENTI
        //Thread Trmi = new Thread();
        //leggo il file della configurazione
        int port;
        if (args.length == 0) {
            port = DEFAULT_PORT;
            //return;
        } else
            port = Integer.parseInt(args[0]);
        System.out.println("Using port " + port);

        //socket, selector e channel
        ServerSocketChannel serverChannel = null; //socket del server
        Selector selector = null;
        try { //configurazione server con creazione e bind di socket e selector
            serverChannel = ServerSocketChannel.open(); //apro socket
            ServerSocket ss = serverChannel.socket(); //la metto come socket
            InetSocketAddress address = new InetSocketAddress(port);
            ss.bind(address); //faccio la bind
            serverChannel.configureBlocking(false); //come rirchiesto dall'esercizio, uso il configureblocking
            selector = Selector.open(); //apro il selettore
            serverChannel.register(selector, SelectionKey.OP_ACCEPT); //lo registro nel selector
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        while (true) { //esecuzione del server
            //System.out.println("sto prendendo il selector");
            try {
                int nChiavi = selector.select();
                System.out.println("ho n chiavi: "+ nChiavi); //verifico quali richieste sono attive
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
            //System.out.println("ho passato la select");
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
            Set<SelectionKey> readyKeys = selector.selectedKeys(); //chiavi dei client con richieste pronte
            //System.out.println("queste sono le chiavi delle task" + readyKeys);
            Iterator<SelectionKey> iterator = readyKeys.iterator(); //iteratore

            while (iterator.hasNext()) { // processo le richieste dei client -> login,register,nuovo post, voto, commento, ecc.
                SelectionKey key = iterator.next();
                //SelectionKey key = iterator.next();
                iterator.remove();
                //System.out.println("stampo le key:  "+key);
                // rimuove la chiave dal Selected Set, ma non dal Registered Set
                //cosa deve fare il server a seconda della richiesta -> divide le task tra vari thread
                try { //leggo le task e le devo mettere in una lista per farle processare ad altri thread
                    if (key.isAcceptable()) {
                        registrazione(selector, key); //viene registrata
                    } else if (key.isReadable()) {
                        //legge il contenuto
                        //MEGA SWOTHC DELLA MORTE CON I FULMINI
                        String output = "";//stringa da modificare per rispondere al client
                        String a = leggiCanale(selector, key);
                        ByteBuffer buffer = (ByteBuffer)key.attachment();//buffer della chiave
                        switch (a)
                        {
                            case"login":
                            {
                                output = "ti sei loggato";
                                break;
                            }
                            case"register":
                            {
                                output="ti sei registrato";
                                break;
                            }
                            case"post":
                            {
                                output="hai postato una risorsa";
                                break;
                            }
                            case"help":
                            {
                                output = "il programma lo usi con le mani";
                                break;
                            }
                            default:
                            {
                                output = "Comando non riconosciuto";
                                System.out.println("[SERVER]:Comando non riconosciuto");
                            }
                        }
                        //meccanismo di risposta al client
                        buffer.put(output.getBytes(StandardCharsets.UTF_8));
                        buffer.flip();
                    }else if(key.isWritable()){
                        scritturaCanale(selector, key);//scrive il contenuto
                    }
                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        cex.printStackTrace();
                    }
                }
            }
        }
    }

    private static void registrazione(Selector sel, SelectionKey selectionKey) throws IOException{
        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel(); //prendo la key del client per recuperare il socket
        SocketChannel client = server.accept();                           //accetto la richiesta del server
        String clientId = client.socket().getInputStream().toString().substring(client.socket().getInputStream().toString().lastIndexOf("@") + 1 );
        System.out.println("[SERVER]:Connessione accettata dal client: " + clientId);
        listClientConnessi.put(clientId,null);//inizializzo l'elemento della lista dove segnerò l'utente loggato
        //client.socket().getInputStream().toString().substring(client.socket().getInputStream().toString().lastIndexOf("@") + 1 ) id del client che si connette al server
        ByteBuffer clientByteBuffer = ByteBuffer.wrap(new byte[1024]);
        client.configureBlocking(false);                                    // dico che non deve essere bloccante
        client.register(sel, SelectionKey.OP_READ, clientByteBuffer);
    }

    private static String leggiCanale(Selector sel, SelectionKey selectionKey) throws IOException {
        //System.out.println("sto leggendo dal canale");
        StringBuilder inputString = new StringBuilder(); //stringbuilder per richiesta/risposta (per fare l'append)
        SocketChannel client = (SocketChannel) selectionKey.channel();               //prendo il socket del client
        //System.out.println("ID client? --> "+ client.socket().getInputStream().toString());
        client.configureBlocking(false);
        //System.out.println("fino a qui");
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();                  //prendo l'attachment

        int nCaratteriLetti = client.read(buffer); //legge dal client
        System.out.println("[SERVER]:ho letto tot caratteri" + nCaratteriLetti);
        //quando faccio la lettura del comando che mi viene inviato da un client
        // avrò una lista delle task da suddividere nei vari thread che dispone il server
        //
        if(nCaratteriLetti < 0){
            client.close();
            throw new IOException("channel read failed.");
        }
        else
        {
            // la lettura porta tutto nel buffer locale e poi devo portarlo
            // carattere per carattere nello string builder
            buffer.flip();
            while (buffer.hasRemaining()) inputString.append((char) buffer.get()); //legge cose

            System.out.println("[SERVER]:HO RICEVUTO DAL CLIENT QUESTO: "+inputString);
            buffer.clear();
            //buffer.flip();//pronto per scrivere
            client.register(sel, SelectionKey.OP_WRITE, buffer);
            return inputString.toString();
        }
    }

    private static void scritturaCanale(Selector sel, SelectionKey selectionKey) throws IOException {
        SocketChannel client;
        StringBuilder inputString = new StringBuilder();
        client = (SocketChannel) selectionKey.channel();
        client.configureBlocking(false);
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
       /* buffer.flip();
        while (buffer.hasRemaining()) inputString.append((char) buffer.get());
        System.out.println("stringa che sto scrivendo: " + inputString);*/
        client.write(buffer);
        buffer.clear();
        //ready for new writes [pos = 0]
        client.register(sel, SelectionKey.OP_READ, buffer);
    }
}
