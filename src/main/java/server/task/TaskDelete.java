package server.task;

import server.resource.ListPost;
import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.Set;
import java.util.concurrent.Callable;


public class TaskDelete implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final String idPost;
    private final ListPost listPost;

    public TaskDelete(ListUsersConnessi listUsersConnessi, String idClient, String idPost, ListPost listPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.idPost = idPost;
        this.listPost = listPost;
    }

    @Override
    public String call() throws Exception {
        StringBuilder result;
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        if(myUser != null) // se l'utente fosse loggato
        {
            Set<Post> listPostOfPost = listPost.getListPost();
            Post postToDelete = null;
            for (Post p: listPostOfPost) {
                if(p.getIdPost().equals(this.idPost))
                {
                    postToDelete = p;
                }
            }
            if(postToDelete != null) // ho trovato il post
            {
                if(postToDelete.getIdAutore().equals(myUser.getIdUser())) // verifico se il post è mio
                {
                    listPostOfPost.remove(postToDelete);
                    listPost.setListPostModified(true);
                    result = new StringBuilder("[SERVER]:Post " +this.idPost+ " eliminato");
                }
                else
                    result = new StringBuilder("[SERVER]: Non sei autore del Post " +this.idPost);
            }
            else
                result = new StringBuilder("[SERVER]:Post " +this.idPost+ " non esiste");
        }
        else
            result = new StringBuilder("[SERVER]: L'utente non e' loggato");
        return result.toString();
    }
}
