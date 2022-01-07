package server.registerRMI;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

public interface RegisterInterfaceRemote extends Remote {
    boolean register(String comando) throws IOException, ExecutionException, InterruptedException;
    /* registrazione per la callback */

    public void registerForCallback (ClientRemoteInterface ClientInterface) throws RemoteException;

    /* cancella registrazione per la callback */

    public void unregisterForCallback (ClientRemoteInterface ClientInterface) throws RemoteException;

}
