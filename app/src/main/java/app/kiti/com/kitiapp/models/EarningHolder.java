package app.kiti.com.kitiapp.models;

import com.google.gson.annotations.SerializedName;

import app.kiti.com.kitiapp.utils.FirebaseDataField;

/**
 * Created by Ankit on 4/19/2018.
 */

public class EarningHolder {

    @SerializedName(FirebaseDataField.ACTION_TIME)
    public String action_at;
    @SerializedName(FirebaseDataField.AD_EARN_RATE)
    public long adEarnRate;

    public EarningHolder(String action_at, long adEarnRate) {
        this.action_at = action_at;
        this.adEarnRate = adEarnRate;
    }

}
