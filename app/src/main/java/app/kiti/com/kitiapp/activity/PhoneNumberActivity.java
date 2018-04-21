package app.kiti.com.kitiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PhoneNumberActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_PHONE = "key_phone";
    @BindView(R.id.kiti_logo)
    ImageView kitiLogo;
    @BindView(R.id.login_with_phone_caption)
    TextView loginWithPhoneCaption;
    @BindView(R.id.phone_numberEt)
    EditText phoneNumberEt;
    @BindView(R.id.get_otp_btn)
    TextView getOtpBtn;
    @BindView(R.id.login_field_container)
    RelativeLayout loginFieldContainer;

    String country_code_offset = "+91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);
        ButterKnife.bind(this);
        // last login check
        if (PreferenceManager.getInstance().isLoggedIn()) {
            navigateToHomePage();
            return;
        }

        attachListener();
    }

    private void attachListener() {

        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneNumberEt.getText().toString();
                if(isValidPhone(phone)) {
                    navigateToOtpReceivePage(phone);
                }else{
                    Toast.makeText(PhoneNumberActivity.this,"Invalid Phone Number",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void navigateToOtpReceivePage(String phone) {

        Intent intent = new Intent(this, ReceiveOtpActivity.class);
        intent.putExtra(EXTRA_KEY_PHONE, country_code_offset + phone);
        startActivity(intent);
        finish();

    }

    private boolean isValidPhone(String phone) {
        return (phone.length() == 10);
    }

    private void navigateToHomePage() {

        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
        finish();

    }


}
