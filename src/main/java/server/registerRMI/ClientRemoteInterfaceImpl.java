package server.registerRMI;

import java.rmi.RemoteException;

public class ClientRemoteInterfaceImpl implements ClientRemoteInterface{

    public ClientRemoteInterfaceImpl() throws RemoteException {
        super( );
    }

    @Override
    public void notifyEvent(String value) throws RemoteException {
        String returnMessage = "Update event received: " + value;
        System.out.println(returnMessage);
    }
}
