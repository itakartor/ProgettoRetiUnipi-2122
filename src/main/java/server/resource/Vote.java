package server.resource;

public class Vote {
    private final String idUser;
    private final boolean vote;
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

    @Override
    public String toString() {
        return "Vote{" +
                "idUser='" + idUser + '\'' +
                ", vote=" + vote +
                ", newVote=" + newVote +
                '}';
    }
}
