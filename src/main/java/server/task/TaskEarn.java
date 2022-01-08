package server.task;

import server.resource.*;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TaskEarn implements Runnable{
    private final String ipMulticast;
    private final Integer portMulticast;
    private final Integer percCuratore;
    private final Integer percAutore;
    private final Integer udpPort;
    private final Integer msTimeout;
    private final ListPost listPost;
    private final ListWallet listWallets;

    public TaskEarn(String ipMulticast, Integer portMulticast, Integer udpPort, Integer percCuratore, Integer percAutore, Integer msTimeout, ListPost listPost, ListWallet listWallets) {
        this.ipMulticast = ipMulticast;
        this.udpPort = udpPort;
        this.portMulticast = portMulticast;
        this.percCuratore = percCuratore;
        this.percAutore = percAutore;
        this.msTimeout = msTimeout;
        this.listPost = listPost;
        this.listWallets = listWallets;
    }

    @Override
    public void run() {
        DatagramSocket ms = null;
        InetAddress ia = null;
        try {
            ia = InetAddress.getByName(this.ipMulticast);
            ms = new DatagramSocket(this.udpPort);
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        while (true)
        {

            try {
                Thread.sleep(msTimeout);
                //Inizio pacchetto da mandare
                byte[] data;
                String msmStart = "Inizio della valutazione dei post";
                data = msmStart.getBytes(StandardCharsets.UTF_8);
                DatagramPacket dp = new DatagramPacket(data, data.length, ia, this.portMulticast); // devo stare attento a non ridichiarare il DatagramSocket
                //fine pacchetto da mandare                               // perchè se no lo vede come un'istanza differente e lancia un eccezione

                assert ms != null;
                ms.send(dp); // invio l'informazione una volta sola per iterazione
                System.out.println("[SERVER]: Ho inviato un messaggio multicast: " + msmStart); //debug
                // faccio valutare al thread tutti i post per poi incrementare i valori dei wallet
                double earn;
                double sumNewVote;
                double sumNewComment;
                for (Post p : this.listPost.getListPost()) { // per ogni post calcolo il guadagno
                    sumNewVote = 0;
                    sumNewComment = 0;
                    Set<String> idUserInteraction = new HashSet<>(); // lista dei curatori
                    p.incEvaluete();
                    for (Vote v: p.getRatesUsers()) { // sommatoria per i voti nuovi
                        if(v.isNewVote()) // se è un nuovo voto rispetto l'ultimo controllo
                        {
                            idUserInteraction.add(v.getIdUser());
                            v.setNewVote(false);
                            this.listPost.setListPostModified(true);
                            if(v.isVote()) // se è +1
                            {
                                sumNewVote++;
                            }
                            else // se è -1
                                sumNewVote--;
                        }
                    }
                    Set<String> idUsersComment = new HashSet<>();
                    for (Comment c: p.getComments()) { // sommatoria per i nuovi commenti
                        if(!idUsersComment.contains(c.getIdUser())) { // se ho gia contato i post di quell'utente non li devo ricontare
                            if (c.isNewComment()) // se è un nuovo commento
                            {
                                idUserInteraction.add(c.getIdUser());
                                idUsersComment.add(c.getIdUser()); // aggiungo l'id degli utenti gia calcolati
                                int numComment = 0;
                                c.setNewComment(false);
                                for (Comment d : p.getComments()) {
                                    if (d.getIdUser().equals(c.getIdUser())) // se è un commento che ha scritto lo stesso utente
                                    {
                                        numComment++; // così conto quanti post ha scritto una persona sotto quel determinato post
                                    }
                                }
                                sumNewComment += 2/(1+ Math.exp(-(numComment-1)));
                            }
                        }
                    }
                    if(idUserInteraction.size()>0)
                    {
                        sumNewVote = Math.max(sumNewVote, 0);
                        earn = (Math.log(sumNewVote +1) +Math.log(sumNewComment+1))/p.getCounterEvaluete(); // questo è il guadagno del post p
                        System.out.println("earn: "+earn +" sumNewVote: "+sumNewComment + " sumNewComment: "+sumNewComment);
                        // ora devo fare le transazioni per incrementare i wallet di tutti
                        double earnAut = percAutore*0.01*earn;
                        double earnCur = ((percCuratore*0.01)*earn)/idUserInteraction.size();
                        System.out.println("earnAut: " + earnAut + "earnCur: "+ earnCur);
                        for (Wallet w: listWallets.getListWallets()) { //idUserInteraction.contains(w.getIdUser())
                            if(w.getIdUser().equals(p.getIdAutore())) // faccio una transazione all'autore
                            {
                                w.addTransaction(earnAut, new Date().toString()); // aggiungo la transazione
                                w.modifyValue(earnAut); // incremento il valore dell wallet
                                this.listWallets.setModified(true);
                            }
                            else if(idUserInteraction.contains(w.getIdUser())) // faccio una transazione ad un curatore
                            {
                                w.addTransaction(earnCur, new Date().toString()); // aggiungo la transazione
                                w.modifyValue(earnCur); // incremento il valore dell wallet
                            }
                        }
                    }
                }

                // mando un altro messaggio multiCast per avvertire il cambiamento dei wallet
                String msmEnd = "Fine della valutazione dei post";
                data = msmEnd.getBytes(StandardCharsets.UTF_8);
                dp = new DatagramPacket(data, data.length, ia, this.portMulticast); // devo stare attento a non ridichiarare il DatagramSocket
                //fine pacchetto da mandare                               // perchè se no lo vede come un'istanza differente e lancia un eccezione
                ms.send(dp); // invio l'informazione una volta sola per iterazione
                System.out.println("[SERVER]: Ho inviato un messaggio multicast: " + msmStart); //debug
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
