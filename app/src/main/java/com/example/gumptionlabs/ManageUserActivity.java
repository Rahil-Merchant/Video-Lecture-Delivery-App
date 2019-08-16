package com.example.gumptionlabs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class ManageUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);
        setTitle("Manage User");

        //get doc id from prev intent

        findViewById(R.id.viewPurchased_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        findViewById(R.id.assignCourse_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


}
