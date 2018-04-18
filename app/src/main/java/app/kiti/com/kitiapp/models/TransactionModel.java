package app.kiti.com.kitiapp.models;

/**
 * Created by Ankit on 4/18/2018.
 */

public class TransactionModel {

    public String accountName;
    public long amount;
    public String transactionTime;
    public long paytmNumber;
    public String transactionId;

    public TransactionModel(String accountName, long amount, String transactionTime, long paytmNumber, String transactionId) {
        this.accountName = accountName;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.paytmNumber = paytmNumber;
        this.transactionId = transactionId;
    }

}
