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
        StringBuilder result = new StringBuilder("[SERVER]:Richiesta list user fallita");
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<User> resultList = new HashSet<>();
        if(myUser != null)
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
            result = new StringBuilder(" Utenti     |        Tags   \n---------------------------\n");
            for (User u: resultList) {
                result.append(u.toString());
            }
        }
        resultList.forEach(System.out::println);

        return result.toString();
    }
}
