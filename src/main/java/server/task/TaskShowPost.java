package server.task;

import server.resource.ListPost;
import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.concurrent.Callable;

public class TaskShowPost implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final String idPost;
    private final ListPost listPost;

    public TaskShowPost(ListUsersConnessi listUsersConnessi, String idClient, String idPost, ListPost listPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.idPost = idPost;
        this.listPost = listPost;
    }

    @Override
    public String call() throws Exception {
        StringBuilder result = new StringBuilder("[SERVER]:Richiesta show post fallita");
        // System.out.println("id client: "+ idClient);
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        if(myUser != null) // se l'utente fosse loggato
        {
            Post resultPost = null;
            for (Post p: this.listPost.getListPost()) {
                if(p.getIdPost().equals(idPost))
                {
                    resultPost = p;
                }
            }
            if(resultPost != null) // se ho trovato il post
            {
                System.out.println(resultPost.toStringShowPost());
                // formattazione output
                result = new StringBuilder("       Post                \n");
                result.append(resultPost.toStringShowPost());
            }
        }
        return result.toString();
    }
}
