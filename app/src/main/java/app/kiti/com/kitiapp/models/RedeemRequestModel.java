package app.kiti.com.kitiapp.models;

import com.google.gson.annotations.SerializedName;

import app.kiti.com.kitiapp.utils.FirebaseDataField;

/**
 * Created by Ankit on 4/18/2018.
 */

public class RedeemRequestModel {

    @SerializedName(FirebaseDataField.AMOUNT)
    public long amount;
    @SerializedName(FirebaseDataField.REQUEST_ID)
    public String requestId;
    @SerializedName(FirebaseDataField.REQUESTED_AT)
    public String requestedAt;
    @SerializedName(FirebaseDataField.REQUESTED_VIA)
    public String requestedVia;
    @SerializedName(FirebaseDataField.REQUESTED_ON_NUMBER)
    public String requestOnNumber;

    public RedeemRequestModel(long amount, String requestId, String requestedAt, String requestedVia, String requestOnNumber) {
        this.amount = amount;
        this.requestId = requestId;
        this.requestedAt = requestedAt;
        this.requestedVia = requestedVia;
        this.requestOnNumber = requestOnNumber;
    }
}
