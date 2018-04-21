package app.kiti.com.kitiapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 4/19/2018.
 */

public class EarningModel {

    @SerializedName("dataTime")
    public String dataTime;
    @SerializedName("adType")
    public String adType;
    @SerializedName("amountEarned")
    public long amountEarned;

    public EarningModel(String dataTime, String adType, long amountEarned) {
        this.dataTime = dataTime;
        this.adType = adType;
        this.amountEarned = amountEarned;
    }

}
