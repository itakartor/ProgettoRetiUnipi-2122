package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client implements Runnable {

    public static int DEFAULT_PORT = 1919;
    private static String ADDRESS = "localhost";
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        //configurazione iniziale del client: porta, addres e buffSize
        int port;
        if (args.length == 0) {
            port = DEFAULT_PORT;
        } else
            port = Integer.parseInt(args[0]);

        System.out.println("Using port " + port);

        BufferedReader input = null; //
        ByteBuffer buffer = null; // lettura/scrittura comunicazione con il server
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

                Thread.sleep(5000);
            }
        } catch(IOException | InterruptedException ex) { ex.printStackTrace(); }
    }

    @Override
    public void run() {
        //configurazione iniziale del client: porta, addres e buffSize
        int port = DEFAULT_PORT;
        System.out.println("Using port " + port);

        BufferedReader input = null; //
        ByteBuffer buffer = null; // lettura/scrittura comunicazione con il server
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

                Thread.sleep(5000);
            }
        } catch(IOException | InterruptedException ex) { ex.printStackTrace(); }
    }
}
