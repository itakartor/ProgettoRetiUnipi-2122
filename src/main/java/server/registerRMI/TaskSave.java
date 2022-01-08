package server.registerRMI;

import GestioneJson.CreatoreJson;
import server.resource.*;

import java.io.IOException;

public class TaskSave implements Runnable {
    private final Integer msDelay;
    private final String pathFile;
    private final String fileNameUsers;
    private final String fileNamePosts;
    private final String fileNameWallet;
    private final ListUser listUsers;
    private final ListPost listPost;
    private final ListWallet listWallet;


    public TaskSave(Integer msDelay, String pathFile, String fileNameUsers, String fileNamePosts, String fileNameWallet, ListUser listUsers, ListPost listPost, ListWallet listWallet) {
        this.msDelay = msDelay;
        this.pathFile = pathFile;
        this.fileNameUsers = fileNameUsers;
        this.fileNamePosts = fileNamePosts;
        this.fileNameWallet = fileNameWallet;
        this.listUsers = listUsers;
        this.listPost = listPost;
        this.listWallet = listWallet;
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

                    if(CreatoreJson.AggiornametoFileUsers(this.pathFile,this.fileNameUsers,listUsers))
                    {
                        System.out.println("[SERVER]:aggiornamento file users json terminato con successo");
                    }
                    this.listUsers.setModified(false);

                }
                if(this.listPost.isModified())
                {
                    if(CreatoreJson.AggiornametoFilePost(this.pathFile,this.fileNamePosts,listPost))
                    {
                        System.out.println("[SERVER]:aggiornamento file posts json terminato con successo");
                    }
                    this.listPost.setListPostModified(false);
                }
                if(this.listWallet.isModified())
                {
                    if(CreatoreJson.AggiornametoFileWallets(this.pathFile,this.fileNameWallet,listWallet))
                    {
                        System.out.println("[SERVER]:aggiornamento file wallets json terminato con successo");
                    }
                    this.listWallet.setModified(false);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
