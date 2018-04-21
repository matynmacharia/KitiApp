package app.kiti.com.kitiapp.models;

import com.google.gson.annotations.SerializedName;

import app.kiti.com.kitiapp.utils.FirebaseDataField;

/**
 * Created by Ankit on 4/18/2018.
 */

public class TransactionModel {

    @SerializedName(FirebaseDataField.ACCOUNT_NAME)
    public String accountName;
    @SerializedName(FirebaseDataField.AMOUNT)
    public long amount;
    @SerializedName(FirebaseDataField.TRANSACTION_TIME)
    public String transactionTime;
    @SerializedName("paytmNumber")
    public long paytmNumber;
    @SerializedName(FirebaseDataField.TRANSACTION_ID)
    public String transactionId;

    public TransactionModel(String accountName, long amount, String transactionTime, long paytmNumber, String transactionId) {
        this.accountName = accountName;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.paytmNumber = paytmNumber;
        this.transactionId = transactionId;
    }

}
