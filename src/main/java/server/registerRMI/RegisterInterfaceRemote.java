package server.registerRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface RegisterInterfaceRemote extends Remote {
    public boolean register(String comando) throws RemoteException;
}
