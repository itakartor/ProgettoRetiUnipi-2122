package server.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class ListUser {
    private String timeStamp;
    private Set<User> listUsers;
    private final ReentrantLock lock;
    private boolean modified;

    public ListUser() {
        this.listUsers = Collections.synchronizedSet(new HashSet<>());
        this.modified = false;
        this.lock = new ReentrantLock();
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

    public void setModified(Boolean modified) {
        this.modified = modified;
    }


    public void addUser(User user)
    {
        this.listUsers.add(user);
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public boolean isModified() {
        return modified;
    }

    @Override
    public String toString() {
        return "ListUser{" +
                "timeStamp='" + timeStamp + '\'' +
                ", listUsers=" + listUsers +
                '}';
    }
}
