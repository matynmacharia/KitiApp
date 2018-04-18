package app.kiti.com.kitiapp.models;

/**
 * Created by Ankit on 4/18/2018.
 */

public class RedeemRequestModel {

    public long amount;
    public String requestId;
    public String requestedAt;

    public RedeemRequestModel(long amount, String reqId, String requestedAt) {
        this.amount = amount;
        this.requestId = reqId;
        this.requestedAt = requestedAt;
    }
}
