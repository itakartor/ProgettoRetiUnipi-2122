package server.task;

import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class TaskNewPost implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final String title;
    private final String content;

    public TaskNewPost(ListUsersConnessi listUsersConnessi, String idClient, String title, String content) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.title = title;
        this.content = content;
    }

    @Override
    public String call(){
        StringBuilder result = new StringBuilder("[SERVER]:Richiesta new post fallita");
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        // Post post = new Post(title,content);
        if(myUser != null) // se l'utente fosse loggato
        {

           // myUser.addPost(post);
        }
        // resultList.forEach(System.out::println);

        return result.toString();
    }
}
