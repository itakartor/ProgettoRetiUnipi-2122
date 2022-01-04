package server.task;

import server.resource.*;

import java.util.Set;
import java.util.concurrent.Callable;

public class TaskComment implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final ListPost listPost;
    private final String comment;
    private final Set<Post> feed;
    private final String idPost;

    public TaskComment(ListUsersConnessi listUsersConnessi, String idClient, ListPost listPost, String comment, Set<Post> feed, String idPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.listPost = listPost;
        this.comment = comment;
        this.feed = feed;
        this.idPost = idPost;
    }

    @Override
    public String call() throws Exception {
        StringBuilder result = new StringBuilder("[SERVER]:Richiesta comment fallita");
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        if(myUser != null) // se l'utente fosse loggato
        {
            Post myPost = null;
            for (Post p: feed) { // verifico se esiste il post nella lista dei feed
                if(p.getIdPost().equals(idPost))
                {
                    myPost = p;
                }
            }
            if(myPost != null) // ho trovato il post nei miei feed
            {
                if(!myPost.getIdAutore().equals(myUser.getIdUser())) // non sono io l'autore del post
                {
                    // se è andato tutto a buon fine metto il voto e dico al server di aggiornare la lista di post
                    myPost.addComment(myUser.getIdUser(),comment,myUser.getUsername());
                    this.listPost.setListPostModified(true);
                    result = new StringBuilder("[SERVER]: Commento accettato");
                }
                else
                    result = new StringBuilder("[SERVER]: Non puoi commentare un post di cui sei autore");
            }
            else
            {
                result = new StringBuilder("[SERVER]: Il post "+ idPost+" non appartiene ai tuoi feed");
            }
        }
        return result.toString();
    }

}
