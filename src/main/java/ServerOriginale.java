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

public class ServerOriginale {
    public static int DEFAULT_PORT = 1919;
    public static void main(String[] args) {
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (RuntimeException ex) {
            port = DEFAULT_PORT;
        }
        System.out.println("[SERVER]:Listening for connections on port " + port);

        ServerSocketChannel serverChannel = null;
        Selector selector = null;
        try {//configurazione server con creazione e bind di socket e selector
            serverChannel = ServerSocketChannel.open();
            ServerSocket ss = serverChannel.socket();
            InetSocketAddress address = new InetSocketAddress(port);
            ss.bind(address);
            serverChannel.configureBlocking(false);
            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }


        while (true) {//esecuzione del server
            try {
                selector.select(); //verifico quali richieste sono attive
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys(); //insieme di chiavi dei client con richieste pronte
            Iterator<SelectionKey> iterator = readyKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                // rimuove la chiave dal Selected Set, ma non dal Registered Set
                try {
                    if (key.isAcceptable()) {registrazione(selector, key);}
                    else if (key.isReadable()) {leggiCanale(selector, key);}
                    else if (key.isWritable()) {scritturaCanale(selector,key);}
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
        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel(); //prendo la key del client per recupare il socket
        SocketChannel client = server.accept();                           //accetto la richiesta del server
        System.out.println("[SERVER]:Connessione accettata dal client:   " + client);
        client.configureBlocking(false);                                    // dico che non deve essere bloccante
        ByteBuffer buffer = ByteBuffer.wrap(new byte[1024]);                       //attach per le varie scritture
        SelectionKey key2 = client.register(sel, SelectionKey.OP_READ,buffer );//key del client indicando che devo scrive al client
    }
    private static void leggiCanale(Selector sel, SelectionKey selectionKey) throws IOException {
        StringBuilder inputString = new StringBuilder();
        SocketChannel client = (SocketChannel) selectionKey.channel();               //prendo il socket del client
        client.configureBlocking(false);
        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();                  //prendo l'attachment
        int n = client.read(buffer);
        if(n < 0){
            client.close();
            throw new IOException("channel read failed.");
        }
        else if(n > 0)
        {
            System.out.println(inputString);

            //la lettura porta tutto nel buffer locale e poi devo portarlo
            // carattere per carattere nello string builder
            //System.out.println("caratteri letti " + n);
            buffer.put("-echoed by server".getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            while (buffer.hasRemaining()) inputString.append((char) buffer.get());

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
