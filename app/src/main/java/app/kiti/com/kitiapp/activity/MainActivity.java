package app.kiti.com.kitiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import app.kiti.com.kitiapp.R;
import app.kiti.com.kitiapp.firebase.SyncManager;
import app.kiti.com.kitiapp.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private SyncManager syncManager;
    private TextView usernameHeader;
    private TextView balanceHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        syncManager = new SyncManager();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View header = navView.getHeaderView(0);
        usernameHeader = header.findViewById(R.id.username_header);
        balanceHeader = header.findViewById(R.id.balance_header);
        navView.setNavigationItemSelectedListener(this);

        attachBalanceListener();
        attachLogoutListener();

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.earning) {
            // Handle the camera action
        } else if (id == R.id.pending) {

        } else if (id == R.id.history) {

        } else if (id == R.id.help) {

        } else if (id == R.id.about_us) {

        } else if (id == R.id.logout) {
            performLogout();
        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void performLogout() {
        // clear pref
        PreferenceManager.getInstance().clearPreferences();
        //nav to login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    private void attachBalanceListener() {

        DatabaseReference balanceRef = syncManager.getBalanceNodeRef();
        if (balanceRef != null) {
            balanceRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String balance = (String) dataSnapshot.getValue();
                    //set value to UI
                    balanceHeader.setText(balance);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            //show error
        }
    }

    private void attachLogoutListener() {

        DatabaseReference tokenRef = syncManager.getUserTokenRef();
        tokenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //check token
                String token = (String) dataSnapshot.getValue();
                PreferenceManager preferenceManager = PreferenceManager.getInstance();
                //match with existing token
                if (!preferenceManager.getUserToken().equals(token) && preferenceManager.isTokenUpdated()) {
                    performLogout();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
