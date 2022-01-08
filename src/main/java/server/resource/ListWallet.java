package server.resource;

import GestioneJson.CreatoreJson;
import GestioneJson.LeggiJson;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ListWallet {
    private String timeStamp;
    private Set<Wallet> listWallets;
    private boolean modified;

    public ListWallet(String pathFile,String nameFile) throws IOException {
        this.listWallets = Collections.synchronizedSet(new HashSet<>());
        this.modified = false;
        switch (CreatoreJson. creazioneFile(pathFile, nameFile)) // lettura o creazione del file post
        {
            case 0:
                System.out.println("[SERVER]: File json "+nameFile+" è stato creato con successo");
                break;
            case 1:
                System.out.println("[SERVER]: File json "+nameFile+" gia esistente lettura in atto");
                // devo leggere il file
                ListWallet supportList = LeggiJson.LetturaFileWallets(pathFile, nameFile);
                if(supportList != null)
                {
                    this.listWallets = supportList.getListWallets();
                    this.timeStamp = supportList.getTimeStamp();
                }
                else
                    System.out.println("[SERVER]: File " + nameFile +" vuoto");
                break;
            case -1:
                System.out.println("[ERROR]: Creazione File "+nameFile+" json fallita");
                break;
        }
    }

    public void addWallet(Wallet wallet)
    {
        this.listWallets.add(wallet);
    }
    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Set<Wallet> getListWallets() {
        return listWallets;
    }
}
