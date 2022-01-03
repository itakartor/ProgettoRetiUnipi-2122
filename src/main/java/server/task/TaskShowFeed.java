package server.task;

import server.resource.ListPost;
import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class TaskShowFeed implements Callable<Set<Post>> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final ListPost listPost;
    private final Set<User> listUser;

    public TaskShowFeed(ListUsersConnessi listUsersConnessi, String idClient, ListPost listPost, Set<User> listUser) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.listPost = listPost;
        this.listUser = listUser;
    }

    @Override
    public Set<Post> call() throws Exception {
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<Post> resultList = new HashSet<>();
        if(myUser != null) // se l'utente fosse loggato
        {
            for (User u: listUser) { // recupero tutti i following
                if(myUser.getFollowings().contains(u.getIdUser())) // verifico se il suo id è contenuto nella lista
                {// aggiungo alla lista risultante tutti i post dei miei following (persone che seguo)
                    resultList.addAll(listPost.getListPost().stream().filter(p -> u.getIdUser().equals(p.getIdAutore())).collect(Collectors.toSet()));
                }
            }
        }
        else // errore l'utente non è connesso correttamente
            return null;

        return resultList;
    }
}
