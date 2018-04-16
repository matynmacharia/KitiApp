package app.kiti.com.kitiapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.firebase.SyncManager;
import app.kiti.com.kitiapp.fragments.HomeFragment;
import app.kiti.com.kitiapp.preference.PreferenceManager;
import app.kiti.com.kitiapp.utils.FontManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.bottomBar_home)
    TextView bottomBarHome;
    @BindView(R.id.bottomBar_profile)
    TextView bottomBarProfile;
    @BindView(R.id.bottomBar_earning)
    TextView bottomBarEarning;
    @BindView(R.id.bottomBar_history)
    TextView bottomBarHistory;
    @BindView(R.id.bottombar_container)
    LinearLayout bottombarContainer;

    private final int BOTTOM_MENU_HOME = 7;
    private final int BOTTOM_MENU_PROFILE = 8;
    private final int BOTTOM_MENU_EARNINGS = 11;
    private final int BOTTOM_MENU_HISTORY = 10;

    private final int FRAGMENT_HOME = 12;
    private final int FRAGMENT_PROFILE = 13;
    private final int FRAGMENT_HISTORY = 15;
    private final int FRAGMENT_EARNINGS = 16;
    @BindView(R.id.contentPlaceHolder)
    FrameLayout contentPlaceHolder;
    @BindView(R.id.tabIndicatorHome)
    FrameLayout tabIndicatorHome;
    @BindView(R.id.tabIndicatorProfile)
    FrameLayout tabIndicatorProfile;
    @BindView(R.id.tabIndicatorEarning)
    FrameLayout tabIndicatorEarning;
    @BindView(R.id.tabIndicatorHistory)
    FrameLayout tabIndicatorHistory;
    @BindView(R.id.drawer_layout)
    RelativeLayout drawerLayout;


    private int lastMenuSelection = BOTTOM_MENU_HOME;
    private SyncManager syncManager;

    private Fragment homeFragment;
    private Fragment profileFragment;
    private Fragment earningFragment;
    private Fragment historyFragment;
    private FragmentManager fragmentManager;
    private Typeface typface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        syncManager = new SyncManager();
        initObjects();
        setupToolbar();
        attachListeners();

        transactFragment(FRAGMENT_HOME);

    }

    @Override
    public void onBackPressed() {
        showExistAlert();
    }

    private void initObjects() {

        fragmentManager = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        profileFragment = new Fragment();
        earningFragment = new Fragment();
        historyFragment = new Fragment();

    }

    private void setupToolbar() {

        typface = FontManager.getInstance().getTypeFace();

        bottomBarHome.setTypeface(typface);
        bottomBarProfile.setTypeface(typface);
        bottomBarEarning.setTypeface(typface);
        bottomBarHistory.setTypeface(typface);


    }

    private void attachListeners() {

        bottomBarHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // check for the current selection
                if (lastMenuSelection == BOTTOM_MENU_HOME)
                    return;
                swapSelection(BOTTOM_MENU_HOME);
                transactFragment(FRAGMENT_HOME);
            }
        });

        bottomBarProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // check for the current selection
                if (lastMenuSelection == BOTTOM_MENU_PROFILE)
                    return;
                swapSelection(BOTTOM_MENU_PROFILE);
                transactFragment(FRAGMENT_PROFILE);
            }
        });

        bottomBarEarning.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // check for the current selection
                if (lastMenuSelection == BOTTOM_MENU_EARNINGS)
                    return;
                swapSelection(BOTTOM_MENU_EARNINGS);
                transactFragment(FRAGMENT_EARNINGS);
            }
        });

        bottomBarHistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // check for the current selection
                if (lastMenuSelection == BOTTOM_MENU_HISTORY)
                    return;
                swapSelection(BOTTOM_MENU_HISTORY);
                transactFragment(FRAGMENT_HISTORY);
            }
        });

    }

    private void transactFragment(int fragment) {

        switch (fragment) {
            case FRAGMENT_HOME:

                // if (!homeFragment.isAdded()) {
                fragmentManager.beginTransaction()
                        .addToBackStack("home")
                        .replace(R.id.contentPlaceHolder, homeFragment)
                        .commit();
                break;
            case FRAGMENT_EARNINGS:

                // if (!homeFragment.isAdded()) {
                fragmentManager.beginTransaction()
                        .addToBackStack("earnings")
                        .replace(R.id.contentPlaceHolder, earningFragment)
                        .commit();
                break;
            case FRAGMENT_PROFILE:

                // if (!homeFragment.isAdded()) {
                fragmentManager.beginTransaction()
                        .addToBackStack("profile")
                        .replace(R.id.contentPlaceHolder, profileFragment)
                        .commit();
                break;
            case FRAGMENT_HISTORY:

                // if (!homeFragment.isAdded()) {
                fragmentManager.beginTransaction()
                        .addToBackStack("history")
                        .replace(R.id.contentPlaceHolder, historyFragment)
                        .commit();
                break;
        }
    }

    private void showExistAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to Exit ?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null);

        builder.show();

    }

    private void performLogout() {
        // clear pref
        PreferenceManager.getInstance().clearPreferences();
        //nav to login
        Intent intent = new Intent(this, ReceiveOtpActivity.class);
        startActivity(intent);
        finish();

    }
//
//    private void attachBalanceListener() {
//
//        DatabaseReference balanceRef = syncManager.getBalanceNodeRef();
//        if (balanceRef != null) {
//            balanceRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String balance = (String) dataSnapshot.getValue();
//                    //set value to UI
//                    balanceHeader.setText(balance);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } else {
//            //show error
//        }
//    }
//
//    private void attachLogoutListener() {
//
//        DatabaseReference tokenRef = syncManager.getUserTokenRef();
//        tokenRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                //check token
//                String token = (String) dataSnapshot.getValue();
//                PreferenceManager preferenceManager = PreferenceManager.getInstance();
//                //match with existing token
//                if (!preferenceManager.getUserToken().equals(token) && preferenceManager.isTokenUpdated()) {
//                    Log.d("LoginActivity","Performing logout :- isUpdated:"+preferenceManager.isTokenUpdated());
//
//                    performLogout();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    private void swapSelection(int newSelectedMenu) {
        if (newSelectedMenu == lastMenuSelection)
            return;
        resetLastSelection(lastMenuSelection);

        switch (newSelectedMenu) {
            case BOTTOM_MENU_HOME:
                bottomBarHome.setTextColor(getResources().getColor(R.color.colorPrimary));
                tabIndicatorHome.setVisibility(View.VISIBLE);
                lastMenuSelection = BOTTOM_MENU_HOME;

                break;
            case BOTTOM_MENU_PROFILE:
                bottomBarProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
                tabIndicatorProfile.setVisibility(View.VISIBLE);
                lastMenuSelection = BOTTOM_MENU_PROFILE;

                break;
            case BOTTOM_MENU_EARNINGS:
                bottomBarEarning.setTextColor(getResources().getColor(R.color.colorPrimary));
                tabIndicatorEarning.setVisibility(View.VISIBLE);
                lastMenuSelection = BOTTOM_MENU_EARNINGS;

                break;

            case BOTTOM_MENU_HISTORY:
                bottomBarHistory.setTextColor(getResources().getColor(R.color.colorPrimary));
                tabIndicatorHistory.setVisibility(View.VISIBLE);
                lastMenuSelection = BOTTOM_MENU_HISTORY;
                break;
            default:
                break;
        }
    }

    private void resetLastSelection(int lastMenuSelection) {

        switch (lastMenuSelection) {
            case BOTTOM_MENU_HOME:
                bottomBarHome.setTextColor(Color.parseColor("#818080"));
                tabIndicatorHome.setVisibility(View.GONE);
                break;
            case BOTTOM_MENU_PROFILE:
                bottomBarProfile.setTextColor(Color.parseColor("#818080"));
                tabIndicatorProfile.setVisibility(View.GONE);
                break;
            case BOTTOM_MENU_EARNINGS:
                bottomBarEarning.setTextColor(Color.parseColor("#818080"));
                tabIndicatorEarning.setVisibility(View.GONE);
                break;
            case BOTTOM_MENU_HISTORY:
                bottomBarHistory.setTextColor(Color.parseColor("#818080"));
                tabIndicatorHistory.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }


}
