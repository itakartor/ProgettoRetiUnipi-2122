package server.registerRMI;

import GestioneJson.CreatoreJson;
import GestioneJson.LeggiJson;
import server.resource.ListUser;
import server.resource.User;
import server.task.TaskRegister;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RegisterInterfaceRemoteImpl implements RegisterInterfaceRemote{
    private List<ClientRemoteInterface> clients;

    ExecutorService service = Executors.newCachedThreadPool();
    //listaUtenti
    private final ListUser listUser = new ListUser();
    public RegisterInterfaceRemoteImpl(String pathFile,String nameFile) throws IOException {
        clients = new ArrayList<ClientRemoteInterface>( );
        switch (CreatoreJson.creazioneFile(pathFile,nameFile))
        {
            case 0:
                System.out.println("[SERVER]: File json "+nameFile+" è stato creato con successo");
                break;
            case 1:
                System.out.println("[SERVER]: File json "+nameFile+" gia esistente lettura in atto");
                // devo leggere il file
                ListUser supportList = LeggiJson.LetturaFileUsers(pathFile,nameFile);
                if(supportList != null)
                {
                    this.listUser.setListUser(supportList.getListUser());
                    this.listUser.setTimeStamp(supportList.getTimeStamp());
                    // System.out.println(this.listUser.toString());
                }
                else
                    System.out.println("[SERVER]: file " +nameFile +" vuoto");
                break;
            case -1:
                System.out.println("[ERROR]: Creazione File "+nameFile+" json fallita");
                break;
        }
    }

    // ricevo tutta la stringa del comando e poi controllo se è composta dai giusti parametri
    @Override
    public boolean register(String comando) throws ExecutionException, InterruptedException {
        Future<Boolean> future = service.submit(new TaskRegister(comando,this.listUser));
        // System.out.println(future.get());
        /*if(future.get())
        {
            CreatoreJson.AggiornametoFileUsers(pathFile,nameFile,this.listUser.getListUser());
        }*/
        return future.get();
    }



    public Set<User> getListUser() {
        return this.listUser.getListUser();
    }
    public ListUser getObjectListUser(){return this.listUser;}

    public Boolean getModifyList() {
        return this.listUser.isModified();
    }


    // gestione callback RMI
    public void registerForCallback(ClientRemoteInterface ClientInterface) throws RemoteException {
        if (!clients.contains(ClientInterface))
        {
            clients.add(ClientInterface);
            System.out.println("New client registered." );
        }
    }

    public void unregisterForCallback(ClientRemoteInterface ClientInterface) throws RemoteException {
        if (clients.remove(ClientInterface))
        {
            System.out.println("Client unregistered");
        }
        else {
            System.out.println("Unable to unregister client.");
        }
    }

    public void update(String value) throws RemoteException
    {
        doCallbacks(value);
    }
    private synchronized void doCallbacks(String value) throws RemoteException
    {
        //System.out.println("Starting callbacks.");
        Iterator<ClientRemoteInterface> i = clients.iterator( );
        //int numeroClienti = clients.size( );
        while (i.hasNext()) {
            ClientRemoteInterface client = (ClientRemoteInterface) i.next();
            //if(client.)
            System.out.println(client.toString()); //client.toString();
            client.notifyEvent(value);
        }
        //System.out.println("Callbacks complete.");
    }
}
