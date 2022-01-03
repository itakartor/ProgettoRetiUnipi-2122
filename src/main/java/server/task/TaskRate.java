package server.task;

import server.resource.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskRate implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final ListPost listPost;
    private final String vote;
    private final Set<Post> feed;
    private final String idPost;

    public TaskRate(ListUsersConnessi listUsersConnessi, String idClient, ListPost listPost, String vote, Set<Post> feed, String idPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.listPost = listPost;
        this.vote = vote;
        this.feed = feed;
        this.idPost = idPost;
    }

    @Override
    public String call() throws Exception {
        StringBuilder result = new StringBuilder("[SERVER]:Richiesta rate fallita");
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        if(myUser != null) // se l'utente fosse loggato
        {
            Post myPost = null;
            for (Post p: this.listPost.getListPost()) {
                if(p.getIdPost().equals(idPost))
                {
                    myPost = p;
                    break;
                }
            }
            if(myPost != null) // ho trovato il post nei miei feed
            {
                if(!myPost.getIdAutore().equals(myUser.getIdUser())) // non sono io l'autore dei post
                {
                    for (Vote v: myPost.getRatesUsers()) {
                        if(v.getIdUser().equals(myUser.getIdUser())) // controllo se ho gia messo un voto
                        {
                            result = new StringBuilder("[SERVER]: Non puoi votare un post piu' di una volta");
                            break;
                        }
                    }
                    myPost.addVote(myUser.getIdUser(),vote);
                }
                else
                    result = new StringBuilder("[SERVER]: Non puoi votare un post di cui sei autore");
            }
            else
            {
                result = new StringBuilder("[SERVER]: Il post "+ idPost+" non è nei tuoi feed");
            }
        }
        return result.toString();
    }
}
