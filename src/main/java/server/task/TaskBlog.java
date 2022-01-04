package server.task;

import server.resource.ListPost;
import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskBlog implements Callable<Set<Post>> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final ListPost listPost;

    public TaskBlog(ListUsersConnessi listUsersConnessi, String idClient, ListPost listPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.listPost = listPost;
    }

    @Override
    public Set<Post> call(){
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<Post> resultList = new HashSet<>();
        if(myUser != null) // se l'utente fosse loggato
        {
            for (Post p: listPost.getListPost()) { // cerco tutti i post di cui sono autore oppure quelli che ho rewinnato
                if(p.getIdAutore().equals(myUser.getIdUser()) || p.getRewinUser().stream().anyMatch(idUser -> idUser.equals(myUser.getIdUser())))
                {
                    resultList.add(p);
                }
            }
        }
        else
            resultList = null; // l'utente non si è loggato

        return resultList;
    }
}
