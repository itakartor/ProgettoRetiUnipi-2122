package server.registerRMI;

import GestioneJson.CreatoreJson;
import GestioneJson.LeggiJson;
import com.github.dockerjava.api.model.GenericResource;
import server.resource.ListUser;
import server.resource.ListUserLight;
import server.resource.User;
import server.task.TaskRegister;
import util.Generators;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class RegisterInterfaceRemoteImpl implements RegisterInterfaceRemote{
    ExecutorService service = Executors.newCachedThreadPool();
    //listaUtenti
    private final ListUser listUser = new ListUser();
    public RegisterInterfaceRemoteImpl(String pathFile,String nameFile) throws IOException {
        switch (CreatoreJson.creazioneFile(pathFile,nameFile))
        {
            case 0:
                System.out.println("[SERVER]: File json "+nameFile+" è stato creato con successo");
                break;
            case 1:
                System.out.println("[SERVER]: File json "+nameFile+" gia esistente lettura in atto");
                // devo leggere il file
                ListUserLight supportList = LeggiJson.LetturaFileUsers(pathFile,nameFile);
                if(supportList != null)
                {
                    this.listUser.setListUser(supportList.getListUsers());
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
}
