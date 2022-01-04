package server.resource;

public class Comment {
    private final String idUser;
    private final String content;
    private boolean newComment;
    private final String username;

    public Comment(String idUser, String content, String username) {
        this.idUser = idUser;
        this.content = content;
        this.newComment = true;
        this.username = username;
    }

    public void setNewComment(boolean newComment) {
        this.newComment = newComment;
    }

    public String getUsername() {
        return username;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getContent() {
        return content;
    }

    public boolean isNewComment() {
        return newComment;
    }
}
