package app.kiti.com.kitiapp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.firebase.SyncManager;
import app.kiti.com.kitiapp.preference.PreferenceManager;
import app.kiti.com.kitiapp.utils.FontManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Ankit on 4/19/2018.
 */

public class ProfileFragment extends Fragment {

    @BindView(R.id.call_symbol)
    TextView callSymbol;
    @BindView(R.id.phone_number)
    TextView phoneNumber;
    @BindView(R.id.linked_number_container)
    LinearLayout linkedNumberContainer;
    @BindView(R.id.total_earned_label)
    TextView totalEarnedLabel;
    @BindView(R.id.earned)
    TextView earned;
    @BindView(R.id.total_redeemed_label)
    TextView totalRedeemedLabel;
    @BindView(R.id.redeemed)
    TextView redeemed;
    @BindView(R.id.total_under_request_label)
    TextView totalUnderRequestLabel;
    @BindView(R.id.under_request)
    TextView underRequest;
    @BindView(R.id.total_balance_label)
    TextView totalBalanceLabel;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.earning_container)
    RelativeLayout earningContainer;
    @BindView(R.id.redeem_btn)
    TextView redeemBtn;
    Unbinder unbinder;

    SyncManager syncManager;
    @BindView(R.id.min_to_redeem_msg)
    TextView minToRedeemMsg;
    @BindView(R.id.redeemEt)
    EditText redeemEt;
    private String phone_number;
    private Context mContext;
    private long mBalance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncManager = new SyncManager();
        phone_number = PreferenceManager.getInstance().getUserPhone();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindData();
        fetchBalance();
        fetchMinToRedeem();

    }

    private void bindData() {
        phoneNumber.setText(phone_number);
        callSymbol.setTypeface(FontManager.getInstance().getTypeFace());
        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAmountAndRedeem();
            }
        });
    }

    private void checkAmountAndRedeem() {

        //this call will be made only when there is sufficient amount
        long amountRequestToRedeem = Long.valueOf(redeemEt.getText().toString());
        if(amountRequestToRedeem >0 ) {
            if (amountRequestToRedeem <= mBalance) {
                //confirm redeem amount
                confirmRedeemAmount(amountRequestToRedeem);
            }
        }

    }

    private void confirmRedeemAmount(final long redeemAmount){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("Redeem")
                .setMessage("Do you want to redeem : "+redeemAmount)
                .setPositiveButton("Yes, Redeem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        placeRedeemRequest(redeemAmount);
                    }
                })
                .setNegativeButton("No", null);

        builder.show();
    }

    private void placeRedeemRequest(long redeemAmount) {

        syncManager.putRedemptionRequest(redeemAmount);
        Toast.makeText(mContext,"Your redeem request placed! Amount will be credited to your linked number soon!",Toast.LENGTH_LONG).show();

    }

    private void fetchBalance() {
        //fetch balance -> fetch under process ->  fetch redeemed -> [set total earned]
        syncManager.getBalanceNodeRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    long balance = (long) dataSnapshot.getValue();
                    mBalance = balance;
                    //fetch under process
                    setBalance(balance);
                    fetchUnderProcessAmount(balance);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //balance for adding up
    private void fetchUnderProcessAmount(final long balance) {

        syncManager.getAmountUnderProcessNodeRed().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    long under_process_amount = (long) dataSnapshot.getValue();
                    setUnderProcess(under_process_amount);
                    // fetch redeemed
                    fetchRedeemed(balance, under_process_amount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void fetchRedeemed(final long balance, final long amount_under_process) {

        syncManager.getRedeemedNodeRed().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    long redeemed_amount = (long) dataSnapshot.getValue();
                    setRedeemed(redeemed_amount);
                    setTotal(balance + amount_under_process + redeemed_amount);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //fetch min to redeem
    private void fetchMinToRedeem() {

        syncManager.getMinToRedeemAmountNodeRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long minToRedeem = (long) dataSnapshot.getValue();
                setMinToRedeemMessage(minToRedeem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setMinToRedeemMessage(long minToRedeem) {
        if (minToRedeemMsg != null) {
            minToRedeemMsg.setText(String.format(mContext.getResources().getString(R.string.you_can_redeem_after_s), minToRedeem));
        }
    }

    private void setTotal(long total) {
        if (earned != null) {
            earned.setText(String.format(mContext.getResources().getString(R.string.earning_format), total));
        }
    }

    private void setRedeemed(long redeemed_amount) {
        if (redeemed != null) {
            redeemed.setText(String.format(mContext.getResources().getString(R.string.earning_format), redeemed_amount));
        }
    }

    private void setBalance(long balanceAmount) {
        if (balance != null) {
            balance.setText(String.format(mContext.getResources().getString(R.string.earning_format), balanceAmount));
        }
        invalidateRedeemButton(balanceAmount);
    }

    private void invalidateRedeemButton(long balanceAmount) {
        long minRequired = PreferenceManager.getInstance().getMinToRedeem();
        if (balanceAmount > minRequired) {
            enableRedeemButton();
            showRedeemBox();
        } else {
            hideRedeemBox();
            disableRedeemButton();
        }
    }

    private void showRedeemBox() {
        if(redeemEt!=null){
            redeemEt.setVisibility(View.VISIBLE);
        }
    }

    private void hideRedeemBox(){
        if(redeemEt!=null){
            redeemEt.setVisibility(View.GONE);
        }
    }

    private void disableRedeemButton() {
        if (redeemBtn != null) {
            redeemBtn.setEnabled(false);
        }
    }

    private void enableRedeemButton() {
        if (redeemBtn != null) {
            redeemBtn.setEnabled(true);
        }
    }

    private void setUnderProcess(long under_process_amount) {
        if (underRequest != null) {
            underRequest.setText(String.format(mContext.getResources().getString(R.string.earning_format), under_process_amount));
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
