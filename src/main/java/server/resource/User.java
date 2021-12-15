package server.resource;

import java.util.Set;

public class User {
    private String username; //univoco
    private String idUser;
    private Set<String> tags;
    private Set<User> followings;
    private Set<Post> myPost;
    private Set<Post> rewinPost;
}
