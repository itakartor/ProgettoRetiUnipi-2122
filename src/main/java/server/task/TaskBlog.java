package server.task;

import server.resource.ListPost;
import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskBlog implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final ListPost listPost;

    public TaskBlog(ListUsersConnessi listUsersConnessi, String idClient, ListPost listPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.listPost = listPost;
    }

    @Override
    public String call(){
        StringBuilder result = new StringBuilder("[SERVER]:Richiesta blog fallita");
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        Set<Post> resultList = new HashSet<>();
        if(myUser != null) // se l'utente fosse loggato
        {
            for (Post p: listPost.getListPost()) {
                if(p.getIdAutore().equals(myUser.getIdUser()) || p.getRewinUser().stream().anyMatch(idUser -> idUser.equals(myUser.getIdUser())))
                {
                    resultList.add(p);
                }
            }
            // formattazione output
            result = new StringBuilder("    My Blog                \n");
            result.append(" Id Post    |       Titolo  \n---------------------------\n");
            if(resultList.isEmpty())
            {
                result.append("         Non hai posts        ");
            }
            else
            {
                for (Post p: resultList) {
                    result.append(p.toString());
                }
            }

        }

        return result.toString();
    }
}
