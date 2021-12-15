package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Server {

    public static int DEFAULT_PORT = 1919;
    public static void main(String[] args) {

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
            //inizializzazione della pool di thread
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }


        while (true) { //esecuzione del server
            try {
                selector.select(); //verifico quali richieste sono attive
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
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
            Iterator<SelectionKey> iterator = readyKeys.iterator(); //iteratore

            while (iterator.hasNext()) { // processo le richieste dei client -> login,register,nuovo post, voto, commento, ecc.
                SelectionKey key = iterator.next();
                iterator.remove();

                // rimuove la chiave dal Selected Set, ma non dal Registered Set
                //cosa deve fare il server a seconda della richiesta -> divide le task tra vari thread
                try { //leggo le task e le devo mettere in una lista per farle processare ad altri thread
                    if (key.isAcceptable()) {
                        registrazione(selector, key); //viene registrata
                    } else if (key.isReadable()) {
                        leggiCanale(selector, key); //legge il contenuto
                    } else if (key.isWritable()) {
                        scritturaCanale(selector,key); //scrive il contenuto
                    }
                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {}
                }
            }
        }
    }

    private static void registrazione(Selector sel, SelectionKey selectionKey) throws IOException{
        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel(); //prendo la key del client per recuperare il socket
        SocketChannel client = server.accept();                           //accetto la richiesta del server
        System.out.println("[SERVER]:Connessione accettata dal client: " + client);
        client.configureBlocking(false);                                    // dico che non deve essere bloccante
    }

    private static void leggiCanale(Selector sel, SelectionKey selectionKey) throws IOException {
        StringBuilder inputString = new StringBuilder(); //stringbuilder per richiesta/risposta (per fare l'append)
        SocketChannel client = (SocketChannel) selectionKey.channel();               //prendo il socket del client
        client.configureBlocking(false);
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();                  //prendo l'attachment
        int n = client.read(buffer); //legge dal client
        if(n < 0){
            client.close();
            throw new IOException("channel read failed.");
        }
        else if(n > 0)
        {
            System.out.println(inputString);

            //la lettura porta tutto nel buffer locale e poi devo portarlo
            // carattere per carattere nello string builder
            buffer.put("-echoed by server".getBytes(StandardCharsets.UTF_8)); //lo scrivo nel buffer
            buffer.flip();
            while (buffer.hasRemaining()) inputString.append((char) buffer.get()); //legge cose

            if(inputString.toString().substring(0,n).contains("!quit"))
            {
                client.close();
                System.out.println("[CONSOLE]:Termine del servizio comunicata dal client");
                System.exit(0);
            }

            //System.out.println("HO RICEVUTO DAL CLIENT QUESTO: "+inputString);

            buffer.flip();//pronto per scrivere
            client.register(sel, SelectionKey.OP_WRITE, buffer);
        }
    }
    private  static void scritturaCanale(Selector sel, SelectionKey selectionKey) throws IOException {
        SocketChannel client;
        client = (SocketChannel) selectionKey.channel();
        client.configureBlocking(false);
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
        client.write(buffer);
        buffer.clear();
        //ready for new writes [pos = 0]
        client.register(sel, SelectionKey.OP_READ, buffer);
    }
}
