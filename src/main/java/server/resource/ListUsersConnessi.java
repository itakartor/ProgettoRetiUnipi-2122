package server.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ListUsersConnessi {
    private final Map<String, User> listClientConnessi;

    public ListUsersConnessi() {
        this.listClientConnessi = Collections.synchronizedMap(new HashMap<>());
    }

    public Map<String, User> getListClientConnessi() {
        return listClientConnessi;
    }

    public void putListClientConnessi(String key, User value) {
        this.listClientConnessi.put(key,value);
    }

}
