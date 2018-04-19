package app.kiti.com.kitiapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.adapters.EarningHistoryAdapter;
import app.kiti.com.kitiapp.firebase.SyncManager;
import app.kiti.com.kitiapp.models.EarningModel;
import app.kiti.com.kitiapp.utils.FirebaseDataField;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Ankit on 4/19/2018.
 */

public class EarningFragment extends Fragment {

    @BindView(R.id.earning_history_label)
    TextView earningHistoryLabel;
    @BindView(R.id.earning_history)
    ListView earningHistory;
    Unbinder unbinder;
    private Context mContext;
    private SyncManager syncManager;
    private EarningHistoryAdapter earningHistoryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        syncManager = new SyncManager();
        earningHistoryAdapter = new EarningHistoryAdapter(mContext);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.earning_fragment, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchEarnings();
        earningHistory.setAdapter(earningHistoryAdapter);
    }

    private void fetchEarnings() {

        syncManager.getVideoEarningListNodeRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("EarningTest", "existence:" + dataSnapshot.exists());
                Log.d("EarningTest", "node" + dataSnapshot);
                if (dataSnapshot.exists()) {
                    //collect earnings
                    collectVideoEarning((Map<String, Object>) dataSnapshot.getValue());
                } else {
                    //show no data available
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void collectVideoEarning(Map<String, Object> earningMap) {

        ArrayList<EarningModel> earningModels = new ArrayList<>();
        for (Map.Entry<String, Object> entry : earningMap.entrySet()) {
            //get map
            Map singleEarning = (Map) entry.getValue();
            //extract data fields
            earningModels.add(new EarningModel(
                    (String) singleEarning.get(FirebaseDataField.ACTION_TIME),
                    FirebaseDataField.VIDEO_AD,
                    (Long) singleEarning.get(FirebaseDataField.AD_EARN_RATE)
            ));
        }

        //update to earning list
        earningHistoryAdapter.setEarningModels(earningModels);

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
