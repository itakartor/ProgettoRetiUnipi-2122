package server.task;

import server.resource.ListUsersConnessi;

import java.util.concurrent.Callable;

public class TaskDelete implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final String idPost;

    public TaskDelete(ListUsersConnessi listUsersConnessi, String idClient, String idPost) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.idPost = idPost;
    }

    @Override
    public String call() throws Exception {
        return null;
    }
}
