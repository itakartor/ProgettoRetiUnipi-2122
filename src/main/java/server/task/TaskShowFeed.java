package server.task;

import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskShowFeed implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;

    public TaskShowFeed(ListUsersConnessi listUsersConnessi, String idClient) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
    }

    @Override
    public String call() throws Exception {
        StringBuilder result = new StringBuilder("[SERVER]:Richiesta show feed fallita");
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<Post> resultList = new HashSet<>();
        if(myUser != null) // se l'utente fosse loggato
        {
            for (User u: myUser.getFollowings()) {
                resultList.addAll(u.getMyPost());
            }
            // formattazione output
            result = new StringBuilder("       Feed                \n");
            result.append("  ID Post   |       Title  \n---------------------------\n");
            for (Post p: resultList) {
                result.append(p.toString());
            }
        }
        return result.toString();
    }
}
