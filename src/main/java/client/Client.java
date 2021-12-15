package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {

    public static int DEFAULT_PORT = 1919;
    private static String ADDRESS = "localhost";
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        //configurazione iniziale del client: porta, addres e buffSize
        int port;
        if (args.length == 0) {
            port = DEFAULT_PORT;
            //return;
        } else
            port = Integer.parseInt(args[0]);
        //int port = DEFAULT_PORT;
        try
        {
            port = Integer.parseInt(args[1]);
        } catch (RuntimeException ex) {
            port = DEFAULT_PORT;
        }
        System.out.println("Using port " + port);
        BufferedReader input = null;
        ByteBuffer buffer = null;
        //fine configurazione iniziale
        try {
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

                if(message.contains("!quit")){ // se il messaggio è !quit chiudo la comunicazione
                    buffer.put("!quit".getBytes());
                    buffer.flip();
                    client.write(buffer);
                    client.close();
                    break;
                }

                buffer.put(message.getBytes(StandardCharsets.UTF_8)); //metto il messaggio nel buffer
                buffer.flip();
                while(buffer.hasRemaining()) client.write(buffer); //fino a quando c'è qualcosa scrivi
                buffer.clear();
                System.out.println("[SCRITTURA CLIENT]: "+ message );

                client.read(buffer); //leggo subito dopo la risposta del server

                buffer.flip();
                while (buffer.hasRemaining()) response.append((char)buffer.get()); //riprendo eventuali cose che non ho letto
                System.out.println("[RISPOSTA SERVER]: " + response);
                buffer.clear();

                Thread.sleep(5000);
            }
        } catch(IOException | InterruptedException ex) { ex.printStackTrace(); }
    }
}
