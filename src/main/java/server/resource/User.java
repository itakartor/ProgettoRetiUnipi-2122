package server.resource;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String username; //univoco
    private Integer idUser;
    private Set<String> tags;
    private Set<User> followings;
    private Set<Post> myPost;
    private Set<Post> rewinPost;

    public String getUsername()
    {
        return this.username;
    }

    public User(String username, Set<String> tags, Integer idUser) {
        this.username = username;
        this.idUser = idUser;
        this.tags = tags;
        this.followings = new HashSet<User>();
        this.myPost = new HashSet<Post>();
        this.rewinPost = new HashSet<Post>();
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
