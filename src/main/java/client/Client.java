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
import java.sql.SQLOutput;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

public class Client implements Runnable {

    public static int DEFAULT_PORT = 1919;
    private static String ADDRESS = "localhost";
    private static final int BUFFER_SIZE = 1024;
    private static boolean ok = true;
    public static void main(String[] args) {
        //configurazione iniziale del client: porta, addres e buffSize
        int port;
        RegisterInterfaceRemote remoteService = null;
        if (args.length == 0) {
            port = DEFAULT_PORT;
        } else
            port = Integer.parseInt(args[0]);

        System.out.println("Using port " + port);

        BufferedReader input = null; //
        ByteBuffer buffer = null; // lettura/scrittura comunicazione con il server
        //fine configurazione iniziale

        try {
            //inizio configurazione RMI
            Remote remoteObject;
            //int portRMI = 6666;
            try {
                Registry r = LocateRegistry.getRegistry(6666);
                remoteObject = r.lookup("REMOTE-SERVER");
                remoteService = (RegisterInterfaceRemote) remoteObject;
            }
            catch (Exception e) {
                System.out.println("Error in invoking object method " +
                        e.toString() + e.getMessage());
                e.printStackTrace();
            }
            //fine configurazione RMI

            //configurazione per l'apertura/accettazione della connessione con il server
            SocketAddress address = new InetSocketAddress(ADDRESS, port); //socket per la connessione col server
            SocketChannel client = SocketChannel.open(address); //apertura connessione
            client.configureBlocking(true); //configurazione bloccante (come richiesto dall'assignment)
            input = new BufferedReader(new InputStreamReader(System.in)); //per richiedere all'utente un messaggio da mandare al server
            buffer = ByteBuffer.wrap(new byte[BUFFER_SIZE]); //mette un byte all'interno del buffer
            String message = null; //messaggio che poi viene letto dall'stdin
            //fine configurazione per l'apertura/accettazione della connessione con il server

            while(true){

                StringBuilder response = new StringBuilder(); //uso la stringbuilder così da poterci fare l'append
                System.out.println("[CONSOLE]: Inserisci un messaggio");
                message = input.readLine(); //leggo qualcosa da tastiera a meno che sia vuoto
                ok = true;
                if(message.contains("register"))//message.contains("register")
                {
                    //System.out.println("sono nella registrazione");
                    StringTokenizer st = new StringTokenizer(message);
                    if(st.countTokens()>8)
                    {
                        System.out.println("[ERROR]: ha messo troppi argomenti");
                        System.out.println("[CONSOLE]: min 1 tag and max 5 tag");
                        ok = false;
                    }else if(st.countTokens()<4)
                    {
                        System.out.println("[ERROR]: ha messo pochi argomenti");
                        ok = false;
                    }else if(false)//controlli sulla password se è vuota o se non rispetta dei requisiti minimi
                    {               //tipo min 8 caratteri, almeno una lettera maiuscola, almeno una lettera minuscola e almeno un carattere speciale

                    }
                    if(ok)//in caso che uno dei controlli non vada a buon fine non faccio la registrazione
                    {
                        int i = 0;
                        String s = null;
                        String username = null;
                        String password = null;
                        Set<String> listTags = new HashSet<>();
                        s = st.nextToken();//dovrebbe avere la stringa "register"
                        while (st.hasMoreTokens()) {
                            s = st.nextToken();
                            i++;
                            if(i == 1)//prendo l'username e potrei fare il controllo su di esso se esiste gia
                            {
                                username = s;
                            }
                            if(i == 2)//devo fare i controlli del caso
                            {
                                password = s;
                            }
                            if(i>2)//prendo tutti i tags e li metto in minuscolo nella lista
                            {
                                listTags.add(s.toLowerCase(Locale.ROOT));
                            }
                        }
                        if(remoteService.register(username,password, listTags))
                        {
                            System.out.println("[Client]: registrazione avvenuta con successo");
                        }
                    }
                    else
                    {
                        System.out.println("[CONSOLE]: usege- register <USERNAME> <PASSWORD> <TAG1> <TAG2> <TAG3> <TAG4> <TAG5>");
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
                    System.out.println("valore buffer remaining: "+buffer.hasRemaining());
                    while(buffer.hasRemaining()) client.write(buffer); //fino a quando c'è qualcosa scrivi
                    /*buffer.flip();
                    StringBuilder inputString = new StringBuilder();
                    while (buffer.hasRemaining()) inputString.append((char) buffer.get());
                    System.out.println("buffer con messaggio da inviare: " + inputString);*/
                    buffer.clear();
                    System.out.println("[SCRITTURA CLIENT]: "+ message );

                    client.read(buffer); //aspetto di leggere la risposta del server

                    buffer.flip();
                    while (buffer.hasRemaining()) response.append((char)buffer.get()); //riprendo eventuali cose che non ho letto
                    System.out.println("[RISPOSTA SERVER]: " + response);
                    buffer.clear();

                }

                //Thread.sleep(5000);
            }
        } catch(IOException ex) { ex.printStackTrace(); }
    }

    @Override
    public void run() {
        main(null);
    }
}
