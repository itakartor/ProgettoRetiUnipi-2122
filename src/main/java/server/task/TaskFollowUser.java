package server.task;

import server.resource.ListUsersConnessi;
import server.resource.User;

import java.util.Set;
import java.util.concurrent.Callable;

public class TaskFollowUser implements Callable<String> {
    private final String username;
    private final String idClient;
    private final Set<User> listUsers;
    private final ListUsersConnessi listUsersConnessi;

    public TaskFollowUser(String username, String idClient, Set<User> listUsers, ListUsersConnessi listUsersConnessi) {
        this.username = username;
        this.idClient = idClient;
        this.listUsers = listUsers;
        this.listUsersConnessi = listUsersConnessi;
    }

    @Override
    public String call(){

        return null;
    }
}
