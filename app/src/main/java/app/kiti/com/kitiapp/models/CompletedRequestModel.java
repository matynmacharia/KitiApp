package app.kiti.com.kitiapp.models;

/**
 * Created by Ankit on 4/18/2018.
 */

public class CompletedRequestModel {

     public long amount;
     public String completedAt;
     public String requestId;
     public String transactionId;

    public CompletedRequestModel(long amount, String completedAt, String requestId, String transactionId) {
        this.amount = amount;
        this.completedAt = completedAt;
        this.requestId = requestId;
        this.transactionId = transactionId;
    }

}
