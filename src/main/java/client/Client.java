package client;

import jdk.swing.interop.SwingInterOpUtils;
import server.registerRMI.RegisterInterfaceRemote;
import server.registerRMI.RegisterInterfaceRemoteImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutionException;

public class Client implements Runnable {

    public static int DEFAULT_PORT = 1919;
    public static int DEFAULT_PORT_RMI = 6666;
    private static final String ADDRESS = "localhost";
    private static final String SERVER_NAME = "REMOTE-SERVER";
    private static final int BUFFER_SIZE = 1024;
    public static void main(String[] args) {
        //configurazione iniziale del client: porta, addres e buffSize
        int port;
        String ip;
        int portRMI = DEFAULT_PORT_RMI;
        RegisterInterfaceRemote remoteService = null;
        if (args.length < 2) {
            port = DEFAULT_PORT;
            ip = ADDRESS;
        } else {
            port = Integer.parseInt(args[1]);
            ip = args[2];
        }

        System.out.println("Using port " + port);

        BufferedReader input; //
        ByteBuffer buffer; // lettura/scrittura comunicazione con il server
        //fine configurazione iniziale

        try {
            //inizio configurazione RMI
            Remote remoteObject;
            try {
                Registry r = LocateRegistry.getRegistry(portRMI);
                remoteObject = r.lookup(SERVER_NAME);
                remoteService = (RegisterInterfaceRemote) remoteObject; // ho recuperato l'interfaccia dal server
            }
            catch (Exception e) {
                System.out.println("Error in invoking object method " + e.getMessage());
                e.printStackTrace();
            }
            // fine configurazione RMI

            // configurazione per l'apertura/accettazione della connessione con il server
            SocketAddress address = new InetSocketAddress(ip, port); // socket per la connessione col server
            SocketChannel client = SocketChannel.open(address); // apertura connessione
            client.configureBlocking(true); // configurazione bloccante
            input = new BufferedReader(new InputStreamReader(System.in)); // per richiedere all'utente un comando da mandare al server
            buffer = ByteBuffer.wrap(new byte[BUFFER_SIZE]); // mette un byte all'interno del buffer
            String message; // messaggio che poi viene letto dall'stdin
            // fine configurazione per l'apertura/accettazione della connessione con il server

            while(true){

                StringBuilder response = new StringBuilder();// uso la stringbuilder così da poterci fare l'append
                System.out.println("[CONSOLE]: Inserisci un comando");
                message = input.readLine(); // leggo qualcosa da tastiera
                if(message.contains("register"))// qui voglio controllare solo se è un comando register
                {                               // i controlli sui campi della registrazione li faccio nel RMI
                                                // perchè vorrei dare la possibilità a più persone di implementare
                                                // un client con delle API
                    assert remoteService != null;
                    if(remoteService.register(message))
                    {
                        System.out.println("[Client]: registrazione avvenuta con successo");
                    }
                    else
                    {
                        System.out.println("[ERROR]: registrazione fallita");
                    }
                }
                else
                {
                    if(message.contains("!quit")){ // se il messaggio è !quit chiudo la comunicazione in caso per disconnessione
                        buffer.put("!quit".getBytes());
                        buffer.flip();
                        client.write(buffer);
                        client.close();
                        break;
                    }

                    buffer.put(message.getBytes(StandardCharsets.UTF_8)); //metto il messaggio nel buffer
                    buffer.flip();
                    // System.out.println("valore buffer remaining: "+buffer.hasRemaining());
                    while(buffer.hasRemaining()) client.write(buffer); //fino a quando c'è qualcosa scrivi
                    /*buffer.flip();
                    StringBuilder inputString = new StringBuilder();
                    while (buffer.hasRemaining()) inputString.append((char) buffer.get());
                    System.out.println("buffer con messaggio da inviare: " + inputString);*/
                    buffer.clear();
                    // System.out.println("[SCRITTURA CLIENT]: "+ message );

                    client.read(buffer); //aspetto di leggere la risposta del server

                    buffer.flip();
                    while (buffer.hasRemaining()) response.append((char)buffer.get()); //riprendo eventuali cose che non ho letto
                    System.out.println(response);
                    buffer.clear();

                }

                //Thread.sleep(5000);
            }
        } catch(IOException | InterruptedException | ExecutionException ex) { ex.printStackTrace(); }
    }

    @Override
    public void run() {

    }
}
