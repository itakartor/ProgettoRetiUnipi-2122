package server.registerRMI;

import GestioneJson.CreatoreJson;
import server.resource.ListUser;
import server.resource.ListUserLight;
import server.resource.User;

import java.io.IOException;
import java.util.Set;

public class TaskSave implements Runnable {
    private final Integer msDelay;
    private final String pathFile;
    private final String fileName;
    private final ListUser listUsers;


    public TaskSave(Integer msDelay, String pathFile, String fileName, ListUser listUsers) {
        this.msDelay = msDelay;
        this.pathFile = pathFile;
        this.fileName = fileName;
        this.listUsers = listUsers;
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                Thread.sleep(this.msDelay);
                System.out.println(Thread.currentThread().getName() + " variabile modify " + listUsers.isModified());
                if(this.listUsers.isModified())
                {
                    CreatoreJson.AggiornametoFileUsers(this.pathFile,this.fileName,listUsers.getListUser());
                    this.listUsers.setModified(false);

                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
