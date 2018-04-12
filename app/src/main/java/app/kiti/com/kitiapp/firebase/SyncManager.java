package app.kiti.com.kitiapp.firebase;

import android.gesture.Prediction;
import android.os.SystemClock;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

import app.kiti.com.kitiapp.preference.PreferenceManager;
import app.kiti.com.kitiapp.utils.FirebaseDataField;

/**
 * Created by Ankit on 4/12/2018.
 */

public class SyncManager {

    private final FirebaseDatabase database;

    public SyncManager() {
        database = FirebaseDatabase.getInstance();
    }

    public DatabaseReference getBalanceNodeRef(){

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return null;

        return database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.BALANCE);

    }

    public void setAdClick(String type) {

        // Users->phone->Clicks->type->time
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return;
        database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.CLICKS)
                .child(type)
                .child(currentDateTimeString)
                .setValue("done");

    }

    public void putRedemptionRequest(String amount) {

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return;
        DatabaseReference redRef = database.getReference()
                .child(userPhone);

        //set amount
        redRef.child(FirebaseDataField.AMOUNT).setValue(amount);
        redRef.child(FirebaseDataField.REQUEST_ID).setValue(getRequestId(currentDateTimeString));
        redRef.child(FirebaseDataField.REQUESTED_AT).setValue(currentDateTimeString);

    }

    public void addBalance(final String amountToAdd) {
        //users -> phone -> balance
        //first read the balance

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return;

        database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.AMOUNT)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String existing_balance = (String) dataSnapshot.getValue();
                        Integer newBalance = Integer.parseInt(existing_balance) + Integer.parseInt(amountToAdd);
                        updateBalance(String.valueOf(newBalance));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void updateBalance(String updateBalance) {

        String userPhone = PreferenceManager.getInstance().getUserPhone();
        if (userPhone.length() == 0)
            return;

        database.getReference()
                .child(FirebaseDataField.USERS)
                .child(userPhone)
                .child(FirebaseDataField.AMOUNT)
                .setValue(updateBalance);

    }

    public void syncRates() {

        // Rates -> type
        database.getReference()
                .child(FirebaseDataField.RATES)
                .child(FirebaseDataField.BANNER_RATE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int rate = (int) dataSnapshot.getValue();
                        PreferenceManager.getInstance().setBannerRate(rate);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        database.getReference()
                .child(FirebaseDataField.RATES)
                .child(FirebaseDataField.INTERSTITAL_RATE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int rate = (int) dataSnapshot.getValue();
                        PreferenceManager.getInstance().setInterstitalRate(rate);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        database.getReference()
                .child(FirebaseDataField.RATES)
                .child(FirebaseDataField.VIDEO_RATE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int rate = (int) dataSnapshot.getValue();
                        PreferenceManager.getInstance().setVideoRate(rate);
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

}
