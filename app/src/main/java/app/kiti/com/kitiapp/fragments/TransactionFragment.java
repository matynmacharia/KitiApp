package app.kiti.com.kitiapp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.adapters.CompletedTransactionListAdapter;
import app.kiti.com.kitiapp.adapters.PendingTransactionListAdapter;
import app.kiti.com.kitiapp.firebase.SyncManager;
import app.kiti.com.kitiapp.models.CompletedRequestModel;
import app.kiti.com.kitiapp.models.RedeemRequestModel;
import app.kiti.com.kitiapp.utils.FirebaseDataField;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Ankit on 4/19/2018.
 */

public class TransactionFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.completed_label)
    TextView completedLabel;
    @BindView(R.id.pending_label)
    TextView pendingLabel;
    @BindView(R.id.tab_container)
    LinearLayout tabContainer;
    @BindView(R.id.transactions_listView)
    ListView transactionsListView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.no_data_msg_panel)
    TextView noDataMsgPanel;

    private Context mContext;
    private SyncManager syncManager;
    private PendingTransactionListAdapter pendingTransactionListAdapter;
    private CompletedTransactionListAdapter completedTransactionListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        syncManager = new SyncManager();
        pendingTransactionListAdapter = new PendingTransactionListAdapter(mContext);
        completedTransactionListAdapter = new CompletedTransactionListAdapter(mContext);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchCompletedTransaction();
        attachListener();

        syncManager.putCompleteTrasactionTestRequest();

    }

    private void attachListener() {

        pendingLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchRequestedTransaction();
                setPendingButtonEnabled();
            }
        });

        completedLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCompletedTransaction();
                setCompletedButtonEnabled();
            }
        });

    }

    private void setPendingButtonEnabled() {

        if (pendingLabel != null) {
            pendingLabel.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        if (completedLabel != null) {
            completedLabel.setTextColor(Color.parseColor("#dc1d1d1d"));
        }

    }

    private void setCompletedButtonEnabled() {

        if (pendingLabel != null) {
            pendingLabel.setTextColor(Color.parseColor("#dc1d1d1d"));
        }
        if (completedLabel != null) {
            completedLabel.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

    }

    private void fetchCompletedTransaction() {

        //hide list view and show progress
        hideListView();
        showProgressBar();

        syncManager.getCompletedRequestNode().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    showNoCompletedData(false);
                    //collect earnings
                    collectCompletedTransaction((Map<String, Object>) dataSnapshot.getValue());
                } else {
                    //show no data available
                    hideProgressBar();
                    showNoCompletedData(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void fetchRequestedTransaction() {

        hideListView();
        showProgressBar();

        syncManager.getPendingRequestNode().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    showNoPendingData(false);
                    //collect earnings
                    collectPendingTransaction((Map<String, Object>) dataSnapshot.getValue());
                } else {
                    //show no data available
                    hideProgressBar();
                    showNoPendingData(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showNoPendingData(boolean show) {
        if (noDataMsgPanel != null) {
            if (show) {
                noDataMsgPanel.setVisibility(View.VISIBLE);
                noDataMsgPanel.setText("No Pending Request!");
            } else {
                noDataMsgPanel.setVisibility(View.GONE);
            }
        }
    }


    private void showNoCompletedData(boolean show) {
        if (noDataMsgPanel != null) {
            if (show) {
                noDataMsgPanel.setVisibility(View.VISIBLE);
                noDataMsgPanel.setText("No Completed Request!");
            } else {
                noDataMsgPanel.setVisibility(View.GONE);
            }
        }
    }


    private void hideListView() {
        if (transactionsListView != null) {
            transactionsListView.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void showListView() {
        if (transactionsListView != null) {
            transactionsListView.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }


    private void collectPendingTransaction(Map<String, Object> pendingTransaction) {

        ArrayList<RedeemRequestModel> redeemRequestModels = new ArrayList<>();
        for (Map.Entry<String, Object> entry : pendingTransaction.entrySet()) {
            //set map
            Map map = (Map) entry.getValue();

            RedeemRequestModel redeemRequestModel = new RedeemRequestModel(
                    (long) map.get(FirebaseDataField.AMOUNT),
                    (String) map.get(FirebaseDataField.REQUEST_ID),
                    (String) map.get(FirebaseDataField.REQUESTED_AT),
                    (String) map.get(FirebaseDataField.REQUESTED_VIA),
                    (String) map.get(FirebaseDataField.REQUESTED_ON_NUMBER)
            );

            redeemRequestModels.add(redeemRequestModel);

        }

        showListView();
        hideProgressBar();
        Log.d("Transaction", "pending items" + redeemRequestModels.size());
        pendingTransactionListAdapter.setRedeemRequestModels(redeemRequestModels);
        if (transactionsListView != null) {
            transactionsListView.setAdapter(pendingTransactionListAdapter);
        }

    }

    private void collectCompletedTransaction(Map<String, Object> completedTransaction) {

        ArrayList<CompletedRequestModel> completedRequestModels = new ArrayList<>();

        for (Map.Entry<String, Object> entry : completedTransaction.entrySet()) {
            //set map
            Map map = (Map) entry.getValue();

            CompletedRequestModel redeemRequestModel = new CompletedRequestModel(
                    (long) map.get(FirebaseDataField.AMOUNT),
                    (String) map.get(FirebaseDataField.COMPLETED_AT),
                    (String) map.get(FirebaseDataField.COMPLETED_VIA),
                    (String) map.get(FirebaseDataField.COMPLETED_ON_NUMBER),
                    (String) map.get(FirebaseDataField.REQUEST_ID),
                    (String) map.get(FirebaseDataField.TRANSACTION_ID)
            );

            completedRequestModels.add(redeemRequestModel);

        }

        showListView();
        hideProgressBar();
        completedTransactionListAdapter.setCompletedRequestModels(completedRequestModels);
        if (transactionsListView != null) {
            transactionsListView.setAdapter(completedTransactionListAdapter);
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
