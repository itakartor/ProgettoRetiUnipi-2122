package server.task;

import server.resource.ListUsersConnessi;

import java.util.concurrent.Callable;

public class TaskLogout implements Callable<String> {
    private final String clientId;
    private final ListUsersConnessi listClientConnessi;

    public TaskLogout(String clientId, ListUsersConnessi listClientConnessi) {
        this.clientId = clientId;
        this.listClientConnessi = listClientConnessi;
    }

    @Override
    public String call(){
        String result = "[SERVER]: Logout avvenuto con successo";
        if(listClientConnessi.getListClientConnessi().get(clientId) != null)
        {
            listClientConnessi.putListClientConnessi(clientId,null); // aggiorno la mappa mettendo a null l'utente
        }
        else
            result = "[SERVER]: Logout fallito";
        return result;
    }
}
