package server.task;

import server.resource.ListUser;
import server.resource.ListUsersConnessi;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskUnfollowUser implements Callable<String> {
    private final String usernameToUnfollow;
    private final String idClient;
    private final ListUser listUsers;
    private final ListUsersConnessi listUsersConnessi;

    public TaskUnfollowUser(String usernameToUnfollow, String idClient, ListUser listUsers, ListUsersConnessi listUsersConnessi) {
        this.usernameToUnfollow = usernameToUnfollow;
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
            User userToUnfollow = null; //cerco l'utente da smettere di seguire nella piattaforma
            for(User i : listUsers.getListUser()) {
                if(i.getUsername().equals(this.usernameToUnfollow))
                    userToUnfollow = i;
            }
            if(userToUnfollow != null) // se ho trovato l'utente che voglio smettere di seguire
            {
                if(!myUser.getFollowings().contains(userToUnfollow.getIdUser())) { // non seguo l'utente
                    result = new StringBuilder("[ERROR]: Utente "+ this.usernameToUnfollow + " non e' seguito");
                }
                else if (userToUnfollow.getIdUser().equals(myUser.getIdUser())) { // se sto provando a smettere di seguirmi
                    result = new StringBuilder("[ERROR]: Non puoi smettere di seguire te stesso");
                }
                else { // se tutto è andato a buon fine
                    myUser.removeFollowing(userToUnfollow.getIdUser());
                    userToUnfollow.removeFollower(myUser.getIdUser());
                    this.listUsers.setModified(true); // dico che la lista degli utenti è stata modificata
                    result = new StringBuilder("[SERVER]: L'utente "+this.usernameToUnfollow + " e' stato rimosso dai tuoi followings" );
                }
            }
            else
                result = new StringBuilder("[SERVER]: Utente " + usernameToUnfollow + " non esiste");
        }
        else
            result = new StringBuilder("[SERVER]: L'utente non e' loggato");

        return result.toString();
    }
}
