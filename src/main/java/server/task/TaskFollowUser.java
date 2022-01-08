package server.task;

import server.resource.ListUser;
import server.resource.ListUsersConnessi;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskFollowUser implements Callable<String> {
    private final String usernameToFollow;
    private final String idClient;
    private final ListUser listUsers;
    private final ListUsersConnessi listUsersConnessi;

    public TaskFollowUser(String usernameToFollow, String idClient, ListUser listUsers, ListUsersConnessi listUsersConnessi) {
        this.usernameToFollow = usernameToFollow;
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
            for(User i : listUsers.getListUser()) {
                if(i.getUsername().equals(this.usernameToFollow))
                    userToFollow = i;
            }
            if(userToFollow != null) // se ho trovato l'utente che voglio seguire
            {
                if(myUser.getFollowings().contains(userToFollow.getIdUser())) { // seguo di gia l'utente
                    result = new StringBuilder("[ERROR]: Utente "+ this.usernameToFollow + " non lo puoi seguire due volte");
                }
                else if (userToFollow.getIdUser().equals(myUser.getIdUser())) { // se l'utente prova a seguire se stesso
                    result = new StringBuilder("[ERROR]: Non puoi seguire te stesso");
                }
                else { // se tutto è andato a buon fine
                    myUser.addFollowings(userToFollow.getIdUser());
                    userToFollow.addFollower(myUser.getIdUser());
                    this.listUsers.setModified(true); // dico che la lista degli utenti è stata modificata
                    result = new StringBuilder("[SERVER]: L'utente "+this.usernameToFollow + " e' stato aggiunto ai tuoi followings" );
                }
            }
            else
                result = new StringBuilder("[SERVER]: Utente " + usernameToFollow + " non esiste");
        }
        else
            result = new StringBuilder("[SERVER]: L'utente non e' loggato");

        return result.toString();
    }
}
