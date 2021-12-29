package server.task;

import server.resource.ListUser;
import server.resource.ListUsersConnessi;
import server.resource.Post;
import server.resource.User;

import java.util.concurrent.Callable;

public class TaskNewPost implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final String title;
    private final String content;
    private final ListUser listUser;

    public TaskNewPost(ListUsersConnessi listUsersConnessi, String idClient, String title, String content,ListUser listUser) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.title = title;
        this.content = content;
        this.listUser = listUser;
    }

    @Override
    public String call(){
        String result = "[SERVER]:Richiesta new post fallita " + idClient;
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        if(myUser != null) // se l'utente fosse loggato
        {
            String userId = myUser.getIdUser();
            Post post = new Post(userId+myUser.getMyPost().size(),title,content,userId);
            myUser.addPost(post);
            System.out.println(post.toString());
            result = post.toString();
            listUser.setModified(true);

        }
        // resultList.forEach(System.out::println);

        return result;
    }
}
