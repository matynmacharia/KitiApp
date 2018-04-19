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
import app.kiti.com.kitiapp.models.EarningModel;
import app.kiti.com.kitiapp.utils.FirebaseDataField;
import app.kiti.com.kitiapp.utils.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/19/2018.
 */

public class EarningHistoryAdapter extends ArrayAdapter<EarningModel> {

    private final Context mContext;
    ArrayList<EarningModel> earningModels;

    public EarningHistoryAdapter(@NonNull Context context) {
        super(context, 0);
        this.mContext = context;
        earningModels = new ArrayList<>();
    }

    public void setEarningModels(ArrayList<EarningModel> earningModels) {
        this.earningModels = earningModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.earning_item_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bindValues(earningModels.get(position));

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.month_year)
        TextView monthYear;
        @BindView(R.id.adType)
        TextView adType;
        @BindView(R.id.earnedAmount)
        TextView earnedAmount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindValues(EarningModel earningModel) {

            long millis = TimeUtils.getMillisFrom(earningModel.dataTime);
            //set date
            String __date = TimeUtils.getDateFrom(millis);
            String __month = TimeUtils.getMonthFrom(millis);
            String __year = TimeUtils.getYearFrom(millis);

            date.setText(__date);
            monthYear.setText(String.format("%s , %s", __month, __year));
            adType.setText(getFormattedTypeName(earningModel.adType));
            earnedAmount.setText(String.format("\u20B9 %d",earningModel.amountEarned));

        }
    }


    @Override
    public int getCount() {
        return earningModels.size();
    }

    private static String getFormattedTypeName(String type){
        if(type.equals(FirebaseDataField.VIDEO_AD)){
            return "Video AD";
        }
        return "";
    }

}
