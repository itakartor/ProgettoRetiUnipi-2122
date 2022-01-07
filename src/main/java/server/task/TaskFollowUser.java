package server.task;

import server.resource.ListUsersConnessi;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskFollowUser implements Callable<String> {
    private final String username;
    private final String idClient;
    private final Set<User> listUsers;
    private final ListUsersConnessi listUsersConnessi;

    public TaskFollowUser(String username, String idClient, Set<User> listUsers, ListUsersConnessi listUsersConnessi) {
        this.username = username;
        this.idClient = idClient;
        this.listUsers = listUsers;
        this.listUsersConnessi = listUsersConnessi;
    }

    @Override
    public String call(){
        StringBuilder result;
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        if(myUser != null) // se l'utente fosse loggato
        {
            User userToFollow = null; //cerco l'utente da seguire
            for(User i : listUsers) {
                System.out.println("Sto controllando utente "+ i.getUsername());
                if(i.getUsername().equals(this.username))
                    userToFollow = i;
            }
            if(myUser != null && myUser.getFollowings().contains(userToFollow.getIdUser())) { //l'utente segue già l'altro utente, errore
                result = new StringBuilder("[Server]: errore: utente già seguito");
            } else {

                if(userToFollow == null) { //utente non trovato
                    result = new StringBuilder("[Server] errore: utente da seguire " + username + " non trovato");
                } else if (userToFollow.getIdUser().equals(myUser.getIdUser())) {
                    result = new StringBuilder("[Server] errore: un utente non può seguire se stesso");
                } else {
                    myUser.addFollowings(userToFollow.getIdUser());
                    userToFollow.addFollower(myUser.getIdUser());
                    result = new StringBuilder("[Server] utente seguito con successo");
                }
            }

        }
        else
            result = new StringBuilder("[SERVER]: L'utente non e' loggato");

        return result.toString();
        //return null;
    }
}
