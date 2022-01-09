package server.resource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Wallet {
    private final String idWallet;
    private double value;
    private final String idUser;
    private final Set<Transaction> listTransaction;

    public Wallet(String idWallet, int idUser) {
        this.idWallet = idWallet;
        this.value = 0;
        this.idUser = String.valueOf(idUser);
        this.listTransaction = Collections.synchronizedSet(new HashSet<>());
    }

    public void addTransaction(Double inc,String timestamp)
    {
        int id = this.listTransaction.size()+1;
        this.listTransaction.add(new Transaction("W"+this.idUser+"-"+id,inc,timestamp));
    }
    public String getIdWallet() {
        return idWallet;
    }

    public double getValue() {
        return value;
    }

    public String getIdUser() {
        return idUser;
    }

    public Set<Transaction> getListTransaction() {
        return listTransaction;
    }

    public void modifyValue(Double inc)
    {
        this.value+= inc;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("     My Wallet      ").append("\n");
        result.append("valore: ").append(this.value).append(" Wincoin").append("\n");
        result.append("Transaction: ").append("\n");
        if(this.listTransaction.isEmpty())
        {
            result.append(" - Non ci sono transazioni");
        }
        else {
            for (Transaction t : this.listTransaction) {
                result.append("     -").append("variazione").append(": ").append(t.getIncrement()).append(" marca temporale: ").append(t.getTimestamp()).append("\n");
            }
        }

        return result.toString();
    }
}
