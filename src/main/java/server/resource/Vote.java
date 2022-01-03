package server.resource;

public class Vote {
    private String idUser;
    private boolean vote;
    private boolean newVote;

    public Vote(String idUser, boolean vote) {
        this.idUser = idUser;
        this.vote = vote;
        this.newVote = true;
    }

    public String getIdUser() {
        return idUser;
    }

    public boolean isVote() {
        return vote;
    }

    public boolean isNewVote() {
        return newVote;
    }

    public void setNewVote(boolean newVote) {
        this.newVote = newVote;
    }
}
