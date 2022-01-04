package server.task;

import server.resource.*;

import java.util.concurrent.Callable;

public class TaskNewPost implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final String title;
    private final String content;
    private final ListPost listPost;

    public TaskNewPost(ListUsersConnessi listUsersConnessi, String idClient, String title, String content, ListPost listPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.title = title;
        this.content = content;
        this.listPost = listPost;
    }

    @Override
    public String call(){
        String result = "[SERVER]:Richiesta new post fallita ";
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        if(myUser != null) // se l'utente fosse loggato
        {
            String userId = myUser.getIdUser();
            Post post = new Post(userId+listPost.getListPost().size(),title,content,userId, myUser.getUsername());
            listPost.getListPost().add(post);
            // System.out.println(post.toString());
            result = post.toString();
            listPost.setListPostModified(true);
        }
        // resultList.forEach(System.out::println);

        return result;
    }
}
