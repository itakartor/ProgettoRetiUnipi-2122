package server.registerRMI;

import java.rmi.RemoteException;

public class ClientRemoteInterfaceImpl implements ClientRemoteInterface{
    private String idClient;
    public ClientRemoteInterfaceImpl() throws RemoteException {
        super( );
    }

    @Override
    public void notifyEvent(String value) throws RemoteException {
        System.out.println("[EVENT]: "+value);
    }

    public void setIdClient(String idClient) throws RemoteException{
        this.idClient = idClient;
    }

    public String getIdClient() throws RemoteException{
        return idClient;
    }
}
