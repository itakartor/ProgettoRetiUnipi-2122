package server.resource;

public class Transaction {
    private final String idTransaction;
    private final Double increment;
    private final String timestamp;

    public Transaction(String idTransaction, Double increment, String timestamp) {
        this.idTransaction = idTransaction;
        this.increment = increment;
        this.timestamp = timestamp;
    }

    public Double getIncrement() {
        return increment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getIdTransaction() {
        return idTransaction;
    }
}
