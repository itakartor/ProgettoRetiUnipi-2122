package server.task;

import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.concurrent.Callable;

public class TaskShowPost implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final String idPost;

    public TaskShowPost(ListUsersConnessi listUsersConnessi, String idClient, String idPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.idPost = idPost;
    }

    @Override
    public String call() throws Exception {
        StringBuilder result = new StringBuilder("[SERVER]:Richiesta show post fallita");
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);

        if(myUser != null) // se l'utente fosse loggato
        {
            Post resultPost = null;
            for (Post p: myUser.getMyPost()) { // prima controllo nei post di cui sono autore
                if(p.getIdPost().equals(idPost))
                {
                    resultPost = p;
                }
                break;
            }
            if(resultPost == null)// il post non si trova nei post di cui sono autore allora li cerco nei feed
            {
                for (Post p: myUser.getPostsFeed()) {
                    if(p.getIdPost().equals(idPost))
                    {
                        resultPost = p;
                    }
                    break;
                }
            }
            if(resultPost != null)
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
