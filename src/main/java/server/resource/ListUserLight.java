package server.resource;

import java.util.Set;

public class ListUserLight {
    private String timeStamp;
    private Set<User> listUsers;

    public ListUserLight(String timeStamp, Set<User> listUsers) {
        this.timeStamp = timeStamp;
        this.listUsers = listUsers;
    }

    public void setListUsers(Set<User> listUsers) {
        this.listUsers = listUsers;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public Set<User> getListUsers() {
        return listUsers;
    }
}
