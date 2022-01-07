package server.registerRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemoteInterface extends Remote {
    public void notifyEvent(String value) throws RemoteException;
}
