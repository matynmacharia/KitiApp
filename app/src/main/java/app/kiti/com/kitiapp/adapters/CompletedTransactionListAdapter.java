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
import app.kiti.com.kitiapp.utils.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/19/2018.
 */

public class CompletedTransactionListAdapter extends ArrayAdapter<CompletedRequestModel> {

    private Context mContext;
    private ArrayList<CompletedRequestModel> completedRequestModels;

    public CompletedTransactionListAdapter(@NonNull Context context) {
        super(context, 0);
        this.mContext = context;
        completedRequestModels = new ArrayList<>();
    }

    public void setCompletedRequestModels(ArrayList<CompletedRequestModel> completedRequestModels) {
        this.completedRequestModels = completedRequestModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.completed_transaction_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bind(completedRequestModels.get(position));
        return convertView;

    }

    @Override
    public int getCount() {
        return completedRequestModels.size();
    }

    static class ViewHolder {
        @BindView(R.id.month_date)
        TextView monthDate;
        @BindView(R.id.via)
        TextView via;
        @BindView(R.id.phone_number)
        TextView phoneNumber;
        @BindView(R.id.transaction_id)
        TextView transactionId;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(CompletedRequestModel completedRequestModel) {
            //set date month
            long millis = TimeUtils.getMillisFrom(completedRequestModel.completedAt);
            String month = TimeUtils.getMonthFrom(millis);
            String date = TimeUtils.getDateFrom(millis);
            String year = TimeUtils.getYearFrom(millis);

            monthDate.setText(String.format("%s %s %s", date, month, year));
            via.setText(completedRequestModel.completedVia);
            phoneNumber.setText(completedRequestModel.completeOnNumber);
            transactionId.setText(completedRequestModel.transactionId);

        }
    }
}
