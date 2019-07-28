package com.example.gumptionlabs;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class homeActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleAuth;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleAuth = GoogleSignIn.getClient(this,gso);
        findViewById(R.id.homeLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                mGoogleAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        findViewById(R.id.store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homeActivity.this,StoreActivity.class));
            }
        });

        findViewById(R.id.home_purchased).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homeActivity.this,MyCoursesActivity.class));
            }
        });

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.getMenu().findItem(R.id.navigation_settings).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            for (int i = 0; i < navView.getMenu().size(); i++) {
                MenuItem menuItem = navView.getMenu().getItem(i);
                boolean isChecked = menuItem.getItemId() == item.getItemId();
                menuItem.setChecked(isChecked);
            }
            switch (item.getItemId()) {
                case R.id.navigation_my_courses:{
                    startActivity(new Intent(homeActivity.this,MyCoursesActivity.class));
                    return true;
                }
                case R.id.navigation_store: {
                    startActivity(new Intent(homeActivity.this,StoreActivity.class));
                    return true;
                }
                case R.id.navigation_info: {
                    String url = "http://www.gumptionlabs.com";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    return true;
                }
                case R.id.navigation_settings: {
                    return true;
                }
            }
            return false;
        }
    };
}
