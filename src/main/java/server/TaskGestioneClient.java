package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class TaskGestioneClient implements Runnable{

    @Override
    public void run() {

    }
    private static void leggiCanale2(Selector sel, SelectionKey selectionKey) throws IOException {
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
        else if(nCaratteriLetti > 0)
        {

            //la lettura porta tutto nel buffer locale e poi devo portarlo
            // carattere per carattere nello string builder
            buffer.put("-echoed by server".getBytes(StandardCharsets.UTF_8)); //lo scrivo nel buffer
            buffer.flip();
            while (buffer.hasRemaining()) inputString.append((char) buffer.get()); //legge cose
            //System.out.println(inputString);
            if(inputString.toString().substring(0,nCaratteriLetti).contains("!quit"))
            {
                client.close();
                System.out.println("[CONSOLE]:Termine del servizio comunicata dal client");
                System.exit(0);
            }

            System.out.println("[SERVER]:HO RICEVUTO DAL CLIENT QUESTO: "+inputString);

            buffer.flip();//pronto per scrivere
            client.register(sel, SelectionKey.OP_WRITE, buffer);
        }
    }
}
