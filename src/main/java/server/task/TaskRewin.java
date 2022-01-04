package server.task;

import server.resource.ListPost;
import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskRewin implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final String idPost;
    private final ListPost listPost;

    public TaskRewin(ListUsersConnessi listUsersConnessi, String idClient, String idPost, ListPost listPost) {
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
            Post myPost = null;
            for (Post p: listPost.getListPost()) {
                if(p.getIdPost().equals(idPost))
                    myPost = p;
            }
            if(myPost != null) // ho trovato il post
            {
                if(!myPost.getIdAutore().equals(myUser.getIdUser())) // il post non è mio
                {
                    if(myPost.getRewinUser().stream().noneMatch(idUser -> idUser.equals(myUser.getIdUser())))
                    {
                        myPost.addRewinUser(myUser.getIdUser());
                        result = new StringBuilder("[SERVER]:Rewin effettuato con successo");
                    }
                    else // ho gia rewinnato il post
                    {
                        result = new StringBuilder("[SERVER]:Il post è stato gia rewinnato dall'utente");
                    }
                }
                else
                    result = new StringBuilder("[SERVER]:Il post è tuo e non puoi effettuare il rewin");
            }
            else
                result = new StringBuilder("[SERVER]:Il post non esiste");
        }
        else
            result = new StringBuilder("[SERVER]: L'utente non e' loggato");

        return result.toString();
    }
}
