package server.task;

import server.resource.ListPost;
import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class TaskShowFeed implements Callable<Set<Post>> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final ListPost listPost;

    public TaskShowFeed(ListUsersConnessi listUsersConnessi, String idClient, ListPost listPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.listPost = listPost;
    }

    @Override
    public Set<Post> call() throws Exception {
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<Post> resultList = new HashSet<>();
        if(myUser != null) // se l'utente fosse loggato
        {
            for (Post p : listPost.getListPost()) { // recupero tutti i following
                if(myUser.getFollowings().contains(p.getIdAutore()))
                {
                    resultList.add(p);
                }
                else
                {
                    for (String idUser : myUser.getFollowings()) {
                        if(p.getRewinUser().contains(idUser))
                        {
                            resultList.add(p);
                            break;
                        }
                    }
                }
            }

        }
        else // errore l'utente non è connesso correttamente
            return null;

        return resultList;
    }
}
