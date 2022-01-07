package server.task;

import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskListFollowers implements Callable<String> {
    private final Set<User> listUser;
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;

    public TaskListFollowers(Set<User> listUser, ListUsersConnessi listUsersConnessi, String idClient) {
        this.listUser = listUser;
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
    }

    @Override
    public String call(){
        StringBuilder result;
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<User> resultList = new HashSet<>();
        if(myUser != null) // se l'utente fosse loggato
        {
            for (User u: listUser) { // recupero tutti i following
                if(myUser.getFollowers().contains(u.getIdUser())) // verifico se il suo id è contenuto nella lista
                {
                    resultList.add(u);
                }
            }
            // ritorno gli utenti che myUser è seguito
            // formattazione output
            result = new StringBuilder("    List Followers         \n");
            result.append(" Utenti     |        Tags   \n---------------------------\n");
            if(resultList.isEmpty())
            {
                result.append("         Non hai follower        ");
            }
            else
            {
                for (User p: resultList) {
                    result.append(p.toString());
                }
            }
        }
        else
            result = new StringBuilder("[SERVER]: L'utente non e' loggato");
        return result.toString();
    }
}
