package server.resource;

import GestioneJson.CreatoreJson;
import GestioneJson.LeggiJson;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ListPost {
    private Set<Post> listPost;// ho una lista sincronizzata dei post per evitare alcune problematiche di sincornizzazione
    private Boolean listPostModified;
    private String timeStamp;

    public ListPost() throws IOException {
        String pathFile = "C:\\Users\\Kartor\\IdeaProjects\\ProgettoReti\\documentation";
        String nameFile = "posts";
        this.listPost = Collections.synchronizedSet(new HashSet<>());
        this.listPostModified = false;
        switch (CreatoreJson.creazioneFile(pathFile, nameFile)) // lettura o creazione del file post
        {
            case 0:
                System.out.println("[SERVER]: File json "+nameFile+" è stato creato con successo");
                break;
            case 1:
                System.out.println("[SERVER]: File json "+nameFile+" gia esistente lettura in atto");
                // devo leggere il file
                ListPostLight supportList = LeggiJson.LetturaFilePost(pathFile, nameFile);
                if(supportList != null)
                {
                    this.listPost = supportList.getListPost();
                    this.timeStamp = supportList.getTimeStamp();
                    // System.out.println(this.listUser.toString());
                }
                else
                    System.out.println("[SERVER]: File " + nameFile +" vuoto");
                break;
            case -1:
                System.out.println("[ERROR]: Creazione File "+nameFile+" json fallita");
                break;
        }
    }

    public Boolean isModified() {
        return listPostModified;
    }

    public Set<Post> getListPost() {
        return listPost;
    }

    public void setListPostModified(Boolean listPostModified) {
        this.listPostModified = listPostModified;
    }

    public void addPost(Post newPost)
    {
        this.listPost.add(newPost);
    }

    public void setListPost(Set<Post> listPost) {
        this.listPost = listPost;
    }
}
