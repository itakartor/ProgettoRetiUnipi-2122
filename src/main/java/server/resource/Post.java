package server.resource;

import java.util.Set;

public class Post {
    private String idPost;
    private String title;
    private String contenuto;
    private Integer positiveVote;
    private Integer negativeVote;
    private String idAutore;
    private Set<User> rewinUser;
}
