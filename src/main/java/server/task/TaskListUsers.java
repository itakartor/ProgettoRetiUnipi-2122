package server.task;

import server.resource.ListUsersConnessi;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskListUsers implements Callable<String> {
    private final Set<User> listUser;
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;

    public TaskListUsers(Set<User> listUser, ListUsersConnessi listUsersConnessi, String idClient) {
        this.listUser = listUser;
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
    }

    @Override
    public String call() {
        StringBuilder result;
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<User> resultList = new HashSet<>();
        if(myUser != null) // se l'utente fosse loggato
        {
            this.listUser.forEach(user -> {
                if(!user.getUsername().equals(myUser.getUsername())) // solo se l'utente è diverso da me
                {                                                    // e ha almeno un tag in comune
                    if(user.getTags().stream().anyMatch(tag -> myUser.getTags().contains(tag)))
                    {
                        resultList.add(user);
                    }
                }
            });
            // formattazione output
            result = new StringBuilder("    List Users             \n");
            result.append(" Utenti     |        Tags   \n---------------------------\n");
            if(resultList.isEmpty())
            {
                result.append("         Non hai nuovi users con tag in comune        ");
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
