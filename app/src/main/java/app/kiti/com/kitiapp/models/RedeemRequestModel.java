package app.kiti.com.kitiapp.models;

/**
 * Created by Ankit on 4/18/2018.
 */

public class RedeemRequestModel {

    public long amount;
    public String requestId;
    public String requestedAt;
    public String requestedVia;
    public String requestOnNumber;

    public RedeemRequestModel(long amount, String requestId, String requestedAt, String requestedVia, String requestOnNumber) {
        this.amount = amount;
        this.requestId = requestId;
        this.requestedAt = requestedAt;
        this.requestedVia = requestedVia;
        this.requestOnNumber = requestOnNumber;
    }
}
