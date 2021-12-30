package server.resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Post {
    private final String idPost; // è pensato come la concatenazione dell'id dell'utente + il numero della dimensione della lista dei post
    private final String title;
    private final String contenuto;
    private final Integer positiveVote;
    private final Integer negativeVote;
    private final String idAutore;
    private final Set<String> rewinUser; // id user di chi rewin il post
    private final ArrayList<String> comments;

    public Post(String idPost, String title, String contenuto, String idAutore) {
        this.idPost = idPost;
        this.title = title;
        this.contenuto = contenuto;
        this.positiveVote = 0;
        this.negativeVote = 0;
        this.idAutore = idAutore;
        this.rewinUser = new HashSet<>();
        this.comments = new ArrayList<>();
    }

    public String getIdPost() {
        return idPost;
    }

    public String getIdAutore() {
        return idAutore;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(this.idPost + "     |     ");
        result.append(this.title);
        result.append("\n");
        return result.toString();
    }
    public String toStringShowPost() {
        StringBuilder result = new StringBuilder("Titolo: "+this.title +"\n");
        result.append("Contenuto: ").append(this.contenuto).append("\n");
        result.append("Voti: ").append(this.positiveVote).append(" positivi, ").append(this.negativeVote).append(" negativi \n");
        result.append("Commenti: ").append(this.comments.size()).append("\n");
        return result.toString();
    }

    /*Titolo: Post di prova
        < Contenuto: Questo è un post di prova
        < Voti: 0 positivi, 0 negativi
            < Commenti: 0*/

}
