package server.task;

import server.resource.ListUsersConnessi;
import server.resource.User;
import util.Generators;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskLogin implements Callable<String> {
    private final String username;
    private String password;
    private final Set<User> listUsers;
    private final String clientId;
    private final ListUsersConnessi listClientConnessi;

    public TaskLogin(String username, String password, Set<User> listUsers, String clientId, ListUsersConnessi listClientConnessi) {
        this.username = username;
        this.password = password;
        this.listUsers = listUsers;
        this.clientId = clientId;
        this.listClientConnessi = listClientConnessi;
    }

    @Override
    public String call() {
        // calcolare la password con la funzione hash
        boolean ok = true;
        String seed = null;
        String output = null;
        User user = null;
        Map<String,User> map = listClientConnessi.getListClientConnessi();
        for (User u: listUsers) {
            if(u.getUsername().equals(username))
            {
                seed = u.getSeed();
                user = u;
                break;
            }
        }
        listClientConnessi.getLock().lock();
        for (Map.Entry<String, User> entry : map.entrySet()) { // controllo se trovo lo stesso utente loggato su un altro client
            if(entry.getValue() == user && !entry.getKey().equals(clientId))
            {
                output ="[SERVER]:Login fallito loggato su un altro client";
                ok = false;
                break;
            }
        }
        if(ok)
        {
            if(seed == null) // l'utente non è registrato
            {
                output ="[SERVER]:Login fallito User non registrato";
            }else if(map.get(clientId) != null) // l'utente è gia loggato
            {
                output = "[SERVER]:Login fallito User gia loggato";
            }
            else // controllo se la password fosse corretta
            {
                password = Generators.sha256(password + seed);
                if(!password.equals(user.getHashPassword())) // se la password è sbagliata
                {
                    output = "[SERVER]:Login fallito Password errata";
                }
                else // la password è corretta
                {
                    output = "[SERVER]:Login avvenuto con successo";
                    listClientConnessi.putListClientConnessi(clientId,user); // rimpiazza il vecchio valore null con l'oggetto user
                }
            }
        }
        listClientConnessi.getLock().unlock();
        System.out.println("sono nella task "+output);
        return output;
    }
}
