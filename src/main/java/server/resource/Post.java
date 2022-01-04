package server.resource;

import java.util.*;

public class Post {
    private final String idPost; // è pensato come la concatenazione dell'id dell'utente + il numero della dimensione della lista dei post
    private final String title;
    private final String contenuto;
    private Integer positiveVote;
    private Integer negativeVote;
    private final String idAutore;
    private final String usernameAutore;
    private final Set<String> rewinUser; // id user di chi rewin il post
    private final Set<Comment> comments;
    private final Set<Vote> ratesUsers;

    public Post(String idPost, String title, String contenuto, String idAutore, String usernameAutore) {
        this.usernameAutore = usernameAutore;
        this.ratesUsers = Collections.synchronizedSet(new HashSet<>());
        this.idPost = idPost;
        this.title = title;
        this.contenuto = contenuto;
        this.positiveVote = 0;
        this.negativeVote = 0;
        this.idAutore = idAutore;
        this.rewinUser = new HashSet<>();
        this.comments = Collections.synchronizedSet(new HashSet<>());
    }

    public Set<String> getRewinUser() {
        return rewinUser;
    }

    public Set<Vote> getRatesUsers() {
        return ratesUsers;
    }
    public void addComment(String idUser, String comment,String username)
    {
        Comment newComment = new Comment(idUser,comment,username);
        this.comments.add(newComment);
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
        StringBuilder result = new StringBuilder(this.idPost + "          |     ");
        result.append(this.usernameAutore).append("     |     ");
        result.append(this.title);
        result.append("\n");
        return result.toString();
    }
    public String toStringShowPost() {
        StringBuilder result = new StringBuilder("Titolo: "+this.title +"\n");
        result.append("Contenuto: ").append(this.contenuto).append("\n");
        result.append("Autore: ").append(this.usernameAutore).append("\n");
        result.append("Voti: ").append(this.positiveVote).append(" positivi, ").append(this.negativeVote).append(" negativi \n");
        result.append("Commenti: ").append("\n");
        if(this.comments.isEmpty())
        {
            result.append(" - Non ci sono commenti");
        }
        else {
            for (Comment c : this.comments) {
                result.append("     -").append(c.getUsername()).append(": ").append("\"").append(c.getContent()).append("\"").append("\n");
            }
        }

        return result.toString();
    }

    /*Titolo: Post di prova
        < Contenuto: Questo è un post di prova
        < Voti: 0 positivi, 0 negativi
            < Commenti: 0*/

}
