package app.kiti.com.kitiapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.firebase.SyncManager;
import app.kiti.com.kitiapp.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import static app.kiti.com.kitiapp.activity.PhoneNumberActivity.EXTRA_KEY_PHONE;

public class ReceiveOtpActivity extends AppCompatActivity {

    private static final String VERIFICATION_IN_PROCESS = "verification_process_key";
    private static final String PHONE_NUMBER = "phone_number";
    @BindView(R.id.kiti_logo)
    ImageView kitiLogo;
    @BindView(R.id.login_with_phone_caption)
    TextView loginWithPhoneCaption;
    @BindView(R.id.otpEt)
    EditText otpEt;
    @BindView(R.id.login_btn)
    TextView loginBtn;
    @BindView(R.id.login_field_container)
    RelativeLayout loginFieldContainer;
    @BindView(R.id.auto_detecting_otp_msg)
    TextView autoDetectingOtpMsg;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String TAG = ReceiveOtpActivity.class.getSimpleName();
    private SyncManager syncManager;
    private Handler mHandler;
    private Runnable autoDetectionModeDisableRunnable = new Runnable() {
        @Override
        public void run() {
            if(autoDetectingOtpMsg!=null){
                autoDetectingOtpMsg.setText("Enter your otp");
            }
        }
    };

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            verificationInProgress = false;
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            otpEt.setText(credential.getSmsCode());
            Log.d(TAG, "onVerificationCompleted:" + credential);
            mHandler.removeCallbacks(autoDetectionModeDisableRunnable);
            signInWithPhoneAuthCredential(credential);

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            verificationInProgress = false;
            Log.w(TAG, "onVerificationFailed", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
            } else if (e instanceof FirebaseTooManyRequestsException) {

            }

        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

            hideSendingOtpProgressDialog();

            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);
            autoDetectingOtpMsg.setVisibility(View.VISIBLE);
            mHandler.postDelayed(autoDetectionModeDisableRunnable,5000);
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            // ...
        }
    };
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private boolean verificationInProgress;
    private String phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_receive);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.getBoolean(VERIFICATION_IN_PROCESS, verificationInProgress);
        outState.putString(PHONE_NUMBER, phone_number);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        verificationInProgress = savedInstanceState.getBoolean(VERIFICATION_IN_PROCESS);
        phone_number = savedInstanceState.getString(PHONE_NUMBER);

    }

    private void init() {

        mHandler = new Handler();
        ButterKnife.bind(this);
        syncManager = new SyncManager();

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }

        mAuth = FirebaseAuth.getInstance();
        phone_number = getIntent().getStringExtra(EXTRA_KEY_PHONE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

    }

    private void attemptLogin() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otpEt.getText().toString());
        signInWithPhoneAuthCredential(credential);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!verificationInProgress) {
            verifyPhoneNumber(phone_number);
        }
    }

    private void showSendingOtpProgressDialog(String msg) {
        if (progressDialog != null) {
            progressDialog.setMessage(msg);
            progressDialog.show();
        }
    }

    private void hideSendingOtpProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void verifyPhoneNumber(String phone) {

        verificationInProgress = true;
        autoDetectingOtpMsg.setVisibility(View.GONE);

        showSendingOtpProgressDialog("Sending OTP to your number...");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {

        showSendingOtpProgressDialog("Logging You in...");

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideSendingOtpProgressDialog();
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            //save user phone to pref (keeping before important for any sync operation)
                            Log.d(TAG, "saving user phone " + user.getPhoneNumber());
                            PreferenceManager.getInstance().setCurrentUserPhone(user.getPhoneNumber());
                            PreferenceManager.getInstance().setLoggedIn(true);
                            syncManager.initUserOrUpdateUserLoginOnFirebase();
                            navigateToHomePage();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(ReceiveOtpActivity.this,"Invalid OTP",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void navigateToHomePage() {

        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
        finish();

    }

}
