package server.resource;

import java.util.Collections;
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
    private final String idWallet;

    public User(String username, Set<String> tags, Integer idUser, String hashPassword, String seed, String idWallet) {
        this.username = username;
        this.idUser = String.valueOf(idUser);
        this.tags = tags;
        this.idWallet = idWallet;
        this.followers = Collections.synchronizedSet(new HashSet<>());
        this.followings = Collections.synchronizedSet(new HashSet<>());
        this.seed = seed;
        this.hashPassword = hashPassword;
    }
    // nelle lista followings e followers metto gli username così da evitare una doppia ricerca in caso che l'utente li richiami
    public void addFollowings(String idUser)
    {
        this.followings.add(idUser);
    }

    public void addFollower(String idUser) {
        this.followers.add(idUser);
    }

    public void removeFollowing(String idUser) {
        this.followings.remove(idUser);
    }

    public void removeFollower(String idUser) {
        this.followers.remove(idUser);
    }

    public Set<String> getFollowers() {
        return followers;
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

    public String getIdWallet() {
        return idWallet;
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
