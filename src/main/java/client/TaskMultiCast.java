package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class TaskMultiCast implements Runnable{
    private final String ipMulticast;
    private final Integer portMulticast;
    private final Integer BUFFER_SIZE;

    public TaskMultiCast(String ipMulticast, Integer portMulticast, Integer buffer_size) {
        this.ipMulticast = ipMulticast;
        this.portMulticast = portMulticast;
        BUFFER_SIZE = buffer_size;
    }

    @Override
    public void run() { // mettere while true per stare in ascolto perennemente

        // configurazione inziale multicast
        InetAddress ia;
        MulticastSocket ms = null;
        try {
            ia = InetAddress.getByName(ipMulticast);
            ms = new MulticastSocket (portMulticast);
            ms.joinGroup(ia);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // corpo per la ricezione dei messaggi
        while (true){
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                DatagramPacket dp = new DatagramPacket (buf,buf.length);
                assert ms != null;
                ms.receive(dp); // bloccante
                System.out.println("[GUADAGNO]:ho ricevuto " + new String(dp.getData(),0,dp.getLength()));//debug
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
