package server.resource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class ListUser {
    private String timeStamp;
    private Set<User> listUsers;

    public ListUser(String timeStamp, Set<User> listUser) {
        this.timeStamp = timeStamp;
        this.listUsers = listUser;
    }

    public Set<User> getListUser() {
        return listUsers;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setListUser(Set<User> listUser) {
        this.listUsers = listUser;
    }

    public void addUser(User user)
    {
        this.listUsers.add(user);
    }
}
