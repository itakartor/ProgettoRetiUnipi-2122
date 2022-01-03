package server.registerRMI;

import GestioneJson.CreatoreJson;
import server.resource.*;

import java.io.IOException;

public class TaskSave implements Runnable {
    private final Integer msDelay;
    private final String pathFile;
    private final String fileNameUsers;
    private final String fileNamePosts;
    private final ListUser listUsers;
    private final ListPost listPost;


    public TaskSave(Integer msDelay, String pathFile, String fileNameUsers, String fileNamePosts, ListUser listUsers, ListPost listPost) {
        this.msDelay = msDelay;
        this.pathFile = pathFile;
        this.fileNameUsers = fileNameUsers;
        this.fileNamePosts = fileNamePosts;
        this.listUsers = listUsers;
        this.listPost = listPost;
    }

    @Override
    public void run() {
        while(true)
        {
            try {
                Thread.sleep(this.msDelay);
                // System.out.println(Thread.currentThread().getName() + " variabile modify " + listUsers.isModified());
                if(this.listUsers.isModified())
                {

                    if(CreatoreJson.AggiornametoFileUsers(this.pathFile,this.fileNameUsers,listUsers.getListUser()))
                    {
                        System.out.println("[SERVER]:aggiornamento file users json terminato con successo");
                    }
                    this.listUsers.setModified(false);

                }
                if(this.listPost.isModified())
                {
                    if(CreatoreJson.AggiornametoFilePost(this.pathFile,this.fileNamePosts,listPost.getListPost()))
                    {
                        System.out.println("[SERVER]:aggiornamento file posts json terminato con successo");
                    }
                    this.listPost.setListPostModified(false);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
