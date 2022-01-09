package client;

import config.ConfigField;
import server.registerRMI.ClientRemoteInterface;
import server.registerRMI.ClientRemoteInterfaceImpl;
import server.registerRMI.RegisterInterfaceRemote;
import util.UtilFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutionException;

public class ClientMain {
    private static final int BUFFER_SIZE = 5000;
    private static final Path pathFileConfig = Paths.get("src/main/java/config/serverConfig.txt");

    public static void main(String[] args) throws IOException {

        // configurazione iniziale del client: porta, addres e buffSize
        ConfigField configServer = UtilFile.readConfigurationServer(pathFileConfig.toString());
        int port = configServer.getTcpPort();
        String ip = configServer.getIpServer();
        int portRMI = configServer.getRegisterPort();
        String SERVER_NAME = configServer.getRegisterHost();
        String ipMulticast = configServer.getMulticastIp();
        Integer portMulticast = configServer.getMulticastPort();
        boolean quit = false;
        RegisterInterfaceRemote remoteService = null;

        // System.out.println("Using port " + port);

        BufferedReader input;
        ByteBuffer buffer; // lettura/scrittura comunicazione con il server
        // fine configurazione iniziale

        try {
            // inizio configurazione RMI
            Remote remoteObject;
            try {
                Registry r = LocateRegistry.getRegistry(portRMI);
                remoteService = (RegisterInterfaceRemote)r.lookup(SERVER_NAME);
                // ho recuperato l'interfaccia dal server
            }
            catch (Exception e) {
                System.out.println("Error in invoking object method " + e.getMessage());
                e.printStackTrace();
            }
            // fine configurazione RMI

            /* si registra per la callback */
            System.out.println("[CLIENT]:Registering for callback");
            ClientRemoteInterface callbackObj = new ClientRemoteInterfaceImpl();
            ClientRemoteInterface stub = (ClientRemoteInterface) UnicastRemoteObject.exportObject(callbackObj, 0);
            assert remoteService != null;
            remoteService.registerForCallback(stub);

            // configurazione per l'apertura/accettazione della connessione con il server
            SocketAddress address = new InetSocketAddress(ip, port); // socket per la connessione col server
            SocketChannel client = SocketChannel.open(address); // apertura connessione
            client.configureBlocking(true); // configurazione bloccante
            input = new BufferedReader(new InputStreamReader(System.in)); // per richiedere all'utente un comando da mandare al server
            buffer = ByteBuffer.wrap(new byte[BUFFER_SIZE]); // mette un byte all'interno del buffer
            String message; // messaggio che poi viene letto dall'stdin
            // fine configurazione per l'apertura/accettazione della connessione con il server

            boolean primo = true;

            Thread multiCastThread = new Thread(new TaskMultiCast(ipMulticast,portMulticast, BUFFER_SIZE));
            multiCastThread.setDaemon(true); // non mi interessa terminarlo se faccio !quit
            multiCastThread.start();

            while(!quit){
                // System.out.println("sono all'inizio del while");
                StringBuilder response = new StringBuilder();// uso la stringbuilder così da poterci fare l'append

                if(primo)
                {/*
                    *come funziona?
                    * all'inizio dell'accettazione della connessione devo recuperare l'id del client visto che il client
                    * stesso non lo conosce e mi servirà in seguito per la callback, allora all'inzio il client manderà
                    * il messaggio "primo" per comunicare al server che gli deve inviare il valore richiesto
                    * tutto questo l'utente non lo vedrà neanche
                    */
                    String messagePrimo = "primo";
                    buffer.put(messagePrimo.getBytes(StandardCharsets.UTF_8)); // metto il messaggio nel buffer
                    buffer.flip();
                    while(buffer.hasRemaining()) client.write(buffer); // fino a quando c'è qualcosa scrivi
                    buffer.clear(); // pulisco il buffer

                    // System.out.println("sono entrato nel primo");
                    primo = false;

                    client.read(buffer); // aspetto di leggere la risposta del server bloccante
                    buffer.flip();
                    while (buffer.hasRemaining()) response.append((char)buffer.get()); // riprendo eventuali cose che non ho letto
                    // System.out.println("prima risposta del server "+response); // scrivo la risposta
                    callbackObj.setIdClient(response.toString());
                    buffer.clear();
                    response = new StringBuilder(); // così ricarico la risposta per il prossimo passaggio

                }

                System.out.println("[CONSOLE]: Inserisci un comando");
                message = input.readLine(); // leggo qualcosa da tastiera
                // System.out.println(message.length());
                if(message.length() == 0) // controllo se il comando è vuoto in caso faccio rinserire il comando
                {
                    System.out.println("[CONSOLE]: Si prega di non inserire una linea senza caratteri");
                    continue;
                }
                if(message.contains("register"))// qui voglio controllare solo se è un comando register
                {                               // i controlli sui campi della registrazione li faccio nel RMI
                                                // perchè vorrei dare la possibilità a più persone di implementare
                                                // un client con delle API
                    if(remoteService.register(message)) // chiama il metodo dall'interfaccia remota
                    {                                   // e verifica se la registrazione è uandata a buon fine
                        System.out.println("[CLIENT]: registrazione avvenuta con successo");
                    }
                    else
                    {
                        System.out.println("[ERROR]: registrazione fallita");
                    }
                }
                else
                {
                    if(message.contains("!quit")){ // se il messaggio è !quit chiudo la comunicazione in caso per disconnessione del client
                        buffer.put("!quit".getBytes(StandardCharsets.UTF_8));
                        buffer.flip();
                        client.write(buffer);
                        UnicastRemoteObject.unexportObject(callbackObj,true); // devo forzare la chiusura dell'oggetto per la call back altrimenti non si chiude
                        remoteService.unregisterForCallback(stub);
                        client.close();
                        quit = true;
                        // System.out.println(Thread.currentThread());
                    }

                    if(!quit)
                    {
                        buffer.put(message.getBytes(StandardCharsets.UTF_8)); // metto il messaggio nel buffer
                        buffer.flip();
                        // System.out.println("valore buffer remaining: "+buffer.hasRemaining());
                        while(buffer.hasRemaining()) client.write(buffer); // fino a quando c'è qualcosa scrivi
                        /*buffer.flip();
                        StringBuilder inputString = new StringBuilder();
                        while (buffer.hasRemaining()) inputString.append((char) buffer.get());
                        System.out.println("buffer con messaggio da inviare: " + inputString);*/
                        buffer.clear(); // pulisco il buffer
                        // System.out.println("[SCRITTURA CLIENT]: "+ message );

                        client.read(buffer); // aspetto di leggere la risposta del server bloccante
                        // System.out.println("buffer lettura " +buffer.toString());
                        buffer.flip();
                        while (buffer.hasRemaining()) response.append((char)buffer.get()); // riprendo eventuali cose che non ho letto
                        System.out.println(response); // scrivo la risposta
                        buffer.clear();
                    }
                    // System.out.println("ho passato la quit");
                }

            }
        } catch(IOException | InterruptedException | ExecutionException ex) { ex.printStackTrace(); }
    }
}
