package server.task;

import server.registerRMI.ClientRemoteInterface;
import server.resource.ListUsersConnessi;
import server.resource.User;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TaskNotifyFollow implements Runnable{
    private final String username;
    private final String message;
    private final Set<ClientRemoteInterface> clients;
    private final ListUsersConnessi listUsersConnessi;

    public TaskNotifyFollow(String username, String message, Set<ClientRemoteInterface> clients, ListUsersConnessi listUsersConnessi1) {
        this.username = username;
        this.message = message;
        this.clients = clients;
        this.listUsersConnessi = listUsersConnessi1;
    }

    @Override
    public void run() {
        String idClient = null;
        for (Map.Entry<String, User> entry : listUsersConnessi.getListClientConnessi().entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
            if(entry.getValue().getUsername().equals(username))
                idClient = entry.getKey();
        }
        if(idClient != null) // ho trovato che l'utente è loggato su un client allora posso notificarlo
        {
            for (ClientRemoteInterface client : clients) {
                try {
                    if (client.getIdClient().equals(idClient)) // ho trovato il client corretto allora lo notifico
                    {
                        try {
                            client.notifyEvent(this.message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
