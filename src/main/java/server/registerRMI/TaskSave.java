package server.registerRMI;

import GestioneJson.CreatoreJson;
import server.resource.User;

import java.io.IOException;
import java.util.Set;

public class TaskSave implements Runnable {
    private Integer msDelay;
    private String pathFile;
    private String fileName;
    private Set<User> listUsers;

    public TaskSave(Integer msDelay, String pathFile, String fileName, Set<User> listUsers) {
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
                CreatoreJson.AggiornametoFileUsers(this.pathFile,this.fileName,this.listUsers);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
