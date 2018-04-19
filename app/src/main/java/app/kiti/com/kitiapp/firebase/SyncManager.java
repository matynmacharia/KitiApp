package app.kiti.com.kitiapp.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import app.kiti.com.kitiapp.models.EarningHolder;
import app.kiti.com.kitiapp.preference.PreferenceManager;
import app.kiti.com.kitiapp.utils.FirebaseDataField;
import app.kiti.com.kitiapp.utils.TimeUtils;

/**
 * Created by Ankit on 4/12/2018.
 */

public class SyncManager {

    private final FirebaseDatabase database;

    public SyncManager() {
        database = FirebaseDatabase.getInstance();
    }

    public DatabaseReference getBalanceNodeRef() {

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return null;

        return database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.BALANCE);

    }

    public DatabaseReference getAmountUnderProcessNodeRed() {

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return null;

        return database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.AMOUNT_UNDER_REQUEST);

    }

    public DatabaseReference getUserTokenRef() {

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return null;

        return database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.USER_TOKEN);

    }

    public DatabaseReference getUserNodeRef() {

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return null;

        return database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone);
    }

    public DatabaseReference getNextVideoSeenTimeNodeRef() {

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return null;

        return database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.NEXT_VIDEO_VIEW_EARLIEST_BY_TIME);

    }

    public DatabaseReference getMinToRedeemAmountNodeRef() {

        return database.getReference()
                .child(FirebaseDataField.CONFIG)
                .child(FirebaseDataField.MIN_TO_REDEEM);

    }

    public DatabaseReference getRedeemedNodeRed() {

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return null;

        return database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.REDEEMED_AMOUNT);

    }

    public DatabaseReference getVideoEarningListNodeRef(){

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return null;

        return database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.EARNINGS)
                .child(FirebaseDataField.VIDEO_AD);

    }

    public void initUserOrUpdateUserLoginOnFirebase() {

        final String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return;

        //Users -> phone
        database.getReference()
                .child(FirebaseDataField.USERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(userPhone).exists()) {
                            //update
                            updateUserLoginToken(userPhone, generateUserLoginToken());

                        } else {
                            createNewUser(userPhone, generateUserLoginToken());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        l("error [user init or upate]" + databaseError.toString());
                    }
                });

    }

    private void updateUserLoginToken(String userPhone, String token) {

        l("updating user token");

        //update pref
        PreferenceManager.getInstance().saveUserToken(token);

        database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.USER_TOKEN)
                .setValue(token);

        // set pref
        PreferenceManager.getInstance().tokenUpdatedToFirebase(true);

    }

    private void createNewUser(String userPhone, String token) {

        l("Creating new user");

        //update pref
        PreferenceManager.getInstance().saveUserToken(token);

        DatabaseReference userRef = database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone);

        userRef.child(FirebaseDataField.USER_TOKEN)
                .setValue(token);

        userRef.child(FirebaseDataField.NEXT_VIDEO_VIEW_EARLIEST_BY_TIME)
                .setValue("");

        userRef.child(FirebaseDataField.BALANCE)
                .setValue(0);

        userRef.child(FirebaseDataField.AMOUNT_UNDER_REQUEST)
                .setValue(0);

        userRef.child(FirebaseDataField.REDEEMED_AMOUNT)
                .setValue(0);

        userRef.child(FirebaseDataField.EARNINGS);

        userRef.child(FirebaseDataField.NAME)
                .setValue(PreferenceManager.getInstance().getUsername());

        userRef.child(FirebaseDataField.USER_PHONE)
                .setValue(userPhone);

        userRef.child(FirebaseDataField.TRANSACTIONS);

        // set pref
        PreferenceManager.getInstance().tokenUpdatedToFirebase(true);

    }

    public void setAdClick(String type) {

        // Users->phone->Clicks->type->time
        String currentDateTimeString = TimeUtils.getTime();
        String userPhone = PreferenceManager.getInstance().getUserPhone();
        long currentRate = PreferenceManager.getInstance().getVideoRate();

        if (userPhone.length() == 0)
            return;

        EarningHolder earningHolder = new EarningHolder(currentDateTimeString , currentRate);

        //update view time
        database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.EARNINGS)
                .child(type)
                .child(String.valueOf(TimeUtils.getMillisFrom(currentDateTimeString)))
                .setValue(earningHolder);

        //update view time
        database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.LAST_VIDEO_SEEN_AT)
                .setValue(currentDateTimeString);

        //update next view time
        database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.NEXT_VIDEO_VIEW_EARLIEST_BY_TIME)
                .setValue(TimeUtils.getAdvancedTimeFromCurrentTime(currentDateTimeString));

    }

    public void putRedemptionRequest(long amount) {

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return;

        DatabaseReference redRef =
                database.getReference()
                        .child(userPhone)
                        .child(generateRedeemRequestId());

        //set amount
        redRef.child(FirebaseDataField.AMOUNT).setValue(amount);
        redRef.child(FirebaseDataField.REQUEST_ID).setValue(getRequestId(currentDateTimeString));
        redRef.child(FirebaseDataField.REQUESTED_AT).setValue(currentDateTimeString);

        //deduct balance
        addBalance(amount * (-1));
        //add to under request field
        updateRedemptionAmountInUserNode(amount);

    }

    private void updateRedemptionAmountInUserNode(final long amount) {

        //get Redemption amount under process and add it
        getUserNodeRef()
                .child(FirebaseDataField.AMOUNT_UNDER_REQUEST)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long amountAlready = (long) dataSnapshot.getValue();
                        //add it
                        setRedemptionAmountInUserNode(amountAlready + amount);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void setRedemptionAmountInUserNode(long amount) {

        getUserNodeRef()
                .child(FirebaseDataField.AMOUNT_UNDER_REQUEST)
                .setValue(amount);

    }

    private String generateRedeemRequestId() {
        return "requestMoney_" + TimeUtils.getMillisFrom(TimeUtils.getTime());
    }

    public void addBalance(final long amountToAdd) {
        //users -> phone -> balance
        //first read the balance

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return;

        database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.BALANCE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        long existing_balance = (long) dataSnapshot.getValue();
                        long newBalance = existing_balance + amountToAdd;
                        updateBalance(newBalance);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void updateBalance(long updateBalance) {

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return;

        database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.BALANCE)
                .setValue(updateBalance);

    }

    public void syncConfig() {

        database.getReference()
                .child(FirebaseDataField.RATES)
                .child(FirebaseDataField.VIDEO_RATE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long rate = (long) dataSnapshot.getValue();
                        PreferenceManager.getInstance().setVideoRate(rate);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        database.getReference()
                .child(FirebaseDataField.CONFIG)
                .child(FirebaseDataField.TIME_DIFF_IN_MIN)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String diff = String.valueOf(dataSnapshot.getValue());
                        PreferenceManager.getInstance().saveTimeDifference(diff);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        database.getReference()
                .child(FirebaseDataField.CONFIG)
                .child(FirebaseDataField.MIN_TO_REDEEM)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long min = (long) dataSnapshot.getValue();
                        PreferenceManager.getInstance().saveMinAmountToRedeem(min);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    public void logJob() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        database.getReference().child("job_test_for_0_to_10_seconds").child(currentDateTimeString).setValue("job Started");
    }

    private String getRequestId(String time) {

        StringBuilder builder = new StringBuilder();
        return builder.append(PreferenceManager.getInstance().getUserPhone()).append("_").append(time).toString();

    }

    private String generateUserLoginToken() {

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        String millis = String.valueOf(System.currentTimeMillis());
        l("generated tokne:" + userPhone + "_" + millis);
        return userPhone + "_" + millis;

    }

    private void l(String msg) {
        Log.d("SyncManager", msg);
    }

}
