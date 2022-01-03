package server.resource;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class User {
    private final String username; // univoco
    private final String idUser;
    private final Set<String> tags;
    private final String hashPassword;
    private final String seed;
    private final Set<String> followings;
    private final Set<String> followers;

    public User(String username, Set<String> tags, Integer idUser, String hashPassword, String seed) {
        this.username = username;
        this.idUser = String.valueOf(idUser);
        this.tags = tags;
        this.followers = new HashSet<>();
        this.followings = new HashSet<>();
        this.seed = seed;
        this.hashPassword = hashPassword;
    }

    public void addFollowings(String idUser)
    {
        this.followings.add(idUser);
    }
    public String getUsername()
    {
        return this.username;
    }

    public String getIdUser() {
        return idUser;
    }

    public Set<String> getFollowings() { // ritorno gli utenti che seguo
        return followings;
    }

    public String getSeed() {
        return seed;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(this.username + "     |     ");
        Iterator<String> iterator = this.tags.iterator();
        while (iterator.hasNext()) {
            result.append(iterator.next());
            //Do stuff
            if (iterator.hasNext()) {
                result.append(", ");
            }
        }
        result.append("\n");
        return result.toString();
    }

    public Set<String> getTags() {
        return tags;
    }
}
