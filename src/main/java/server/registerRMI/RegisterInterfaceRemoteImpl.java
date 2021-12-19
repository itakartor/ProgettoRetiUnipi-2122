package server.registerRMI;

import server.resource.User;

import java.nio.channels.SelectionKey;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Set;

public class RegisterInterfaceRemoteImpl implements RegisterInterfaceRemote{

    //listaUtenti
    private Set<User> listUser;
    private Integer counterUsers;
    public RegisterInterfaceRemoteImpl(Set<User> listUser, Integer counterUsers)  {
        this.listUser = listUser;
        this.counterUsers =counterUsers;//counter degli utenti registrati per generare l'id incrementale degli utenti
    }

    @Override
    public boolean register(String username, String password, Set<String> listTag) throws RemoteException {
        Iterator<User> iterator =this.listUser.iterator();
        while(iterator.hasNext())
        {
            User u = iterator.next();
            if(u.getUsername().equals(username))
            {
                System.out.println("[ERROR]: username gia utilizzato");
                return false;
            }
        }
        this.counterUsers++;
        User newUser = new User(username,listTag, this.counterUsers);
        this.listUser.add(newUser);
        //va aggiunto l'utente al json
        System.out.println("[SERVER]: registrazione avvenuta con successo: "+ newUser);
        return true;//registrazione a buon fine
    }

    public Set<User> getListUser() {
        return listUser;
    }
}
