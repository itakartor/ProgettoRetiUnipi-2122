package server.task;

import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskBlog implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;

    public TaskBlog(ListUsersConnessi listUsersConnessi, String idClient) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
    }

    @Override
    public String call(){
        StringBuilder result = new StringBuilder("[SERVER]:Richiesta blog fallita");
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<Post> resultList = new HashSet<>();
        if(myUser != null) // se l'utente fosse loggato
        {
            resultList = myUser.getMyPost();
            // formattazione output
            result = new StringBuilder("    My Blog                \n");
            result.append(" Id Post    |       Titolo  \n---------------------------\n");
            for (Post p: resultList) {
                result.append(p.toString());
            }
        }

        return result.toString();
    }
}
