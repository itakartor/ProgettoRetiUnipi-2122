package server.resource;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Post {
    private static String idPost;
    private String title;
    private String contenuto;
    private Integer positiveVote;
    private Integer negativeVote;
    private String idAutore;
    private Set<User> rewinUser;

    public Post(String idPost, String title, String contenuto, String idAutore) {
        this.idPost = idPost;
        this.title = title;
        this.contenuto = contenuto;
        this.positiveVote = 0;
        this.negativeVote = 0;
        this.idAutore = idAutore;
        this.rewinUser = new HashSet<>();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(this.idPost + "     |     ");
        result.append(this.title);
        result.append("\n");
        return result.toString();
    }

}
