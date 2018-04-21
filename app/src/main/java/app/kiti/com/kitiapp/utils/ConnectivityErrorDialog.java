package app.kiti.com.kitiapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import app.kiti.com.kitiapp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ankit on 4/21/2018.
 */

public class ConnectivityErrorDialog extends Dialog {

    public Activity c;
    public Dialog d;
    @BindView(R.id.tryAgain)
    TextView tryAgain;
    private TextView tryButton;

    public ConnectivityErrorDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.connectivity_error_dialog);
        ButterKnife.bind(this);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tryAgainListener!=null){
                    tryAgainListener.onTryAgain();
                }
            }
        });
    }

    private TryAgainListener tryAgainListener;

    public void setTryAgainListener(TryAgainListener tryAgainListener) {
        this.tryAgainListener = tryAgainListener;
    }

    public interface TryAgainListener{
        void onTryAgain();
    }

}
