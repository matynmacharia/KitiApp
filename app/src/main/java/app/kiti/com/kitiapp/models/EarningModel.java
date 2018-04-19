package app.kiti.com.kitiapp.models;

/**
 * Created by Ankit on 4/19/2018.
 */

public class EarningModel {

    public String dataTime;
    public String adType;
    public long amountEarned;

    public EarningModel(String dataTime, String adType, long amountEarned) {
        this.dataTime = dataTime;
        this.adType = adType;
        this.amountEarned = amountEarned;
    }

}
