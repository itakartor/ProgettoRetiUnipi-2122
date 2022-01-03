package server.resource;

import java.util.*;

public class Post {
    private final String idPost; // è pensato come la concatenazione dell'id dell'utente + il numero della dimensione della lista dei post
    private final String title;
    private final String contenuto;
    private Integer positiveVote;
    private Integer negativeVote;
    private final String idAutore;
    private final Set<String> rewinUser; // id user di chi rewin il post
    private final ArrayList<String> comments;
    private final Set<Vote> ratesUsers;

    public Post(String idPost, String title, String contenuto, String idAutore) {
        this.ratesUsers = Collections.synchronizedSet(new HashSet<>());
        this.idPost = idPost;
        this.title = title;
        this.contenuto = contenuto;
        this.positiveVote = 0;
        this.negativeVote = 0;
        this.idAutore = idAutore;
        this.rewinUser = new HashSet<>();
        this.comments = new ArrayList<>();
    }

    public Set<String> getRewinUser() {
        return rewinUser;
    }

    public Set<Vote> getRatesUsers() {
        return ratesUsers;
    }

    public void addVote(String idUser, String vote)
    {
        Vote newVote = null;
        switch (vote)
        {
            case"+1":
            {
                newVote = new Vote(idUser,true);
                this.positiveVote++;
                break;
            }
            case"-1":
            {
                newVote = new Vote(idUser,false);
                this.negativeVote++;
                break;
            }
        }
        if(newVote != null)
            this.ratesUsers.add(newVote);
    }
    public String getIdPost() {
        return idPost;
    }

    public String getIdAutore() {
        return idAutore;
    }

    public void addRewinUser(String idUser)
    {
        this.rewinUser.add(idUser);
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
