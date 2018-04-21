package app.kiti.com.kitiapp.models;

import com.google.gson.annotations.SerializedName;

import app.kiti.com.kitiapp.utils.FirebaseDataField;

/**
 * Created by Ankit on 4/18/2018.
 */

public class CompletedRequestModel {

    @SerializedName(FirebaseDataField.AMOUNT)
    public long amount;
    @SerializedName(FirebaseDataField.COMPLETED_AT)
    public String completedAt;
    @SerializedName(FirebaseDataField.COMPLETED_VIA)
    public String completedVia;
    @SerializedName(FirebaseDataField.COMPLETED_ON_NUMBER)
    public String completeOnNumber;
    @SerializedName(FirebaseDataField.REQUEST_ID)
    public String requestId;
    @SerializedName(FirebaseDataField.TRANSACTION_ID)
    public String transactionId;

    public CompletedRequestModel(long amount, String completedAt, String completedVia, String completeOnNumber, String requestId, String transactionId) {
        this.amount = amount;
        this.completedAt = completedAt;
        this.completedVia = completedVia;
        this.completeOnNumber = completeOnNumber;
        this.requestId = requestId;
        this.transactionId = transactionId;
    }
}
