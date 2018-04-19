package app.kiti.com.kitiapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.models.CompletedRequestModel;
import app.kiti.com.kitiapp.models.RedeemRequestModel;
import app.kiti.com.kitiapp.utils.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/19/2018.
 */

public class PendingTransactionListAdapter extends ArrayAdapter<RedeemRequestModel> {

    private Context mContext;
    private ArrayList<RedeemRequestModel> redeemRequestModels;

    public PendingTransactionListAdapter(@NonNull Context context) {
        super(context, 0);
        this.mContext = context;
        redeemRequestModels = new ArrayList<>();
    }

    public void setRedeemRequestModels(ArrayList<RedeemRequestModel> requestModels) {
        this.redeemRequestModels = requestModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.pending_transaction_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bind(redeemRequestModels.get(position));
        return convertView;

    }

    @Override
    public int getCount() {
        return redeemRequestModels.size();
    }

    static class ViewHolder {
        @BindView(R.id.month_date)
        TextView monthDate;
        @BindView(R.id.via)
        TextView via;
        @BindView(R.id.phone_number)
        TextView phoneNumber;
        @BindView(R.id.request_id)
        TextView requestId;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(RedeemRequestModel redeemRequestModel) {
            //set date month
            long millis = TimeUtils.getMillisFrom(redeemRequestModel.requestedAt);
            String month = TimeUtils.getMonthFrom(millis);
            String date = TimeUtils.getDateFrom(millis);
            String year = TimeUtils.getYearFrom(millis);

            monthDate.setText(String.format("%s %s %s", date, month, year));
            via.setText(redeemRequestModel.requestedVia);
            phoneNumber.setText(redeemRequestModel.requestOnNumber);
            requestId.setText(redeemRequestModel.requestId);

        }
    }
}
