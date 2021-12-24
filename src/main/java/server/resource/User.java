package server.resource;

import java.util.HashSet;
import java.util.Set;

public class User {
    private final String username; //univoco
    private final String idUser;
    private final Set<String> tags;
    private final Set<User> followings;
    private final Set<Post> myPost;
    private final Set<Post> rewinPost;

    public String getUsername()
    {
        return this.username;
    }

    public User(String username, Set<String> tags, Integer idUser) {
        this.username = username;
        this.idUser = String.valueOf(idUser);
        this.tags = tags;
        this.followings = new HashSet<>();
        this.myPost = new HashSet<>();
        this.rewinPost = new HashSet<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", idUser=" + idUser +
                ", tags=" + tags +
                '}';
    }
}
