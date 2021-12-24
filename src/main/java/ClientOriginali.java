import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ClientOriginali {
    public static int DEFAULT_PORT = 1919;
    private static String ADDRESS = "localhost";
    private static final int BUFFER_SIZE =1024;

    public static void main(String[] args) {
        int port;
        if (args.length == 0) {
            port = DEFAULT_PORT;
        } else
            port = Integer.parseInt(args[0]);
        BufferedReader input = null;
        ByteBuffer buffer = null;
        try {
            SocketAddress address = new InetSocketAddress(ADDRESS, port);
            SocketChannel client = SocketChannel.open(address);
            client.configureBlocking(false);
            input = new BufferedReader(new InputStreamReader(System.in));
            buffer = ByteBuffer.wrap(new byte[BUFFER_SIZE]);
            String message = null;

            while(true){

                StringBuilder response = new StringBuilder();
                System.out.println("[CONSOLE]:inserisci un messaggio ");
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

                client.read(buffer); //leggo subito dopo

                buffer.flip();
                while (buffer.hasRemaining()) response.append((char)buffer.get()); // se mi sono perso qualcosa lo recupero
                System.out.println("[RISPOSTA SERVER]: " + response);
                buffer.clear();

                Thread.sleep(5000);//distanzio tutte le richieste
            }
        } catch(IOException | InterruptedException ex) { ex.printStackTrace(); }
    }
}
