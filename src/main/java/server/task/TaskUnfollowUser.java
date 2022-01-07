package server.task;

import server.resource.ListUsersConnessi;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskUnfollowUser implements Callable<String> {
    private final String username;
    private final String idClient;
    private final Set<User> listUsers;
    private final ListUsersConnessi listUsersConnessi;

    public TaskUnfollowUser(String username, String idClient, Set<User> listUsers, ListUsersConnessi listUsersConnessi) {
        this.username = username;
        this.idClient = idClient;
        this.listUsers = listUsers;
        this.listUsersConnessi = listUsersConnessi;
    }

    @Override
    public String call(){
        StringBuilder result;
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<User> resultList = new HashSet<>();
        if(myUser != null) // se l'utente fosse loggato
        {
            User userToUnfollow = null; //cerco l'utente da smettere di seguire
            for(User i : listUsers) {
                if(i.getUsername().equals(this.username))
                    userToUnfollow = i;
            }
            if(myUser != null && myUser.getFollowings().contains(userToUnfollow.getIdUser())) { //l'utente segue già l'altro utente

                if(userToUnfollow == null) { //utente non trovato
                    result = new StringBuilder("[Server] errore: utente da smettere di seguire non trovato");
                } else if (userToUnfollow.getIdUser().equals(myUser.getIdUser())) {
                    result = new StringBuilder("[Server] errore: un utente non può smettere di seguire se stesso");
                } else {
                    myUser.removeFollowing(userToUnfollow.getIdUser());
                    userToUnfollow.removeFollower(myUser.getIdUser());
                    result = new StringBuilder("[Server] utente smesso di seguire con successo");
                }
            } else { //errore, l'utente non segue l'altro
                result = new StringBuilder("[Server]: errore: utente non seguito");
            }

        }
        else
            result = new StringBuilder("[SERVER]: L'utente non e' loggato");

        return result.toString();
        //return null;
    }
}
