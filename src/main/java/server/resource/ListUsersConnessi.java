package server.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ListUsersConnessi {
    private final Map<String, User> listClientConnessi;
    private final ReentrantLock lock;

    public ListUsersConnessi() {
        this.listClientConnessi = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public Map<String, User> getListClientConnessi() {
        return listClientConnessi;
    }

    public void putListClientConnessi(String key, User value) {
        this.listClientConnessi.put(key,value);
    }
}
