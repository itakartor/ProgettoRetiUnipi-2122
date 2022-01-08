package server.registerRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemoteInterface extends Remote {
     void notifyEvent(String message) throws RemoteException;
     void setIdClient(String idClient) throws RemoteException;
     String getIdClient() throws RemoteException;
}
