package server.task;

import server.resource.ListUsersConnessi;
import server.resource.ListWallet;
import server.resource.User;
import server.resource.Wallet;
import util.Generators;

import java.util.concurrent.Callable;

public class TaskWalletBTC implements Callable<String> {
    private final ListUsersConnessi listUsersConnessi;
    private final String idClient;
    private final ListWallet listWallet;

    public TaskWalletBTC(ListUsersConnessi listUsersConnessi, String idClient, ListWallet listWallet) {
        this.listUsersConnessi = listUsersConnessi;
        this.idClient = idClient;
        this.listWallet = listWallet;
    }

    @Override
    public String call() throws Exception {
        StringBuilder result;
        User myUser = this.listUsersConnessi.getListClientConnessi().get(idClient);
        if(myUser != null) // se l'utente fosse loggato
        {
            Wallet myWallet = null;
            for (Wallet w : listWallet.getListWallets()) {
                if(w.getIdUser().equals(myUser.getIdUser()))
                    myWallet = w;
            }
            if(myWallet != null) // ho trovato il wallet
            {
                float convertBTC = Generators.getRandomFromRandomDotOrg();
                double newValueWallet = (convertBTC/100)* myWallet.getValue();
                result = new StringBuilder("Il valore del wallet convertito in BTC e': " + newValueWallet);
            }
            else
                result = new StringBuilder("[SERVER]: Wallet non trovato");
        }
        else
            result =new StringBuilder("[SERVER]: L'utente non e' loggato");
        return result.toString();
    }
}
