package server.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ListPostLight {
    private Set<Post> listPost;// ho una lista sincronizzata dei post per evitare alcune problematiche di sincornizzazione
    private String timeStamp;

    public ListPostLight(Set<Post> listPost, String timeStamp) {
        this.listPost = listPost;
        this.timeStamp = timeStamp;
    }

    public Set<Post> getListPost() {
        return listPost;
    }

    public void setListPost(Set<Post> listPost) {
        this.listPost = listPost;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
