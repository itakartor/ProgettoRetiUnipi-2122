package server.task;

import server.resource.ListPost;
import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class TaskShowFeed implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final ListPost listPost;

    public TaskShowFeed(ListUsersConnessi listUsersConnessi, String idClient, ListPost listPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.listPost = listPost;
    }

    @Override
    public String call() throws Exception {
        StringBuilder result = new StringBuilder("[SERVER]:Richiesta show feed fallita");
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<Post> resultList = new HashSet<>();
        if(myUser != null) // se l'utente fosse loggato
        {
            for (User u: myUser.getFollowings()) { // aggiungo alla lista risultante tutti i post dei miei following (persone che seguo)
                resultList.addAll(listPost.getListPost().stream().filter(p -> u.getIdUser().equals(p.getIdAutore())).collect(Collectors.toSet()));
            }
            // formattazione output
            result = new StringBuilder("       Feed                \n");
            result.append("  ID Post   |       Title  \n---------------------------\n");
            if(resultList.isEmpty())
            {
                result.append("         Non hai feed        ");
            }
            else
            {
                for (Post p: resultList) {
                    result.append(p.toString());
                }
            }
        }
        return result.toString();
    }
}
