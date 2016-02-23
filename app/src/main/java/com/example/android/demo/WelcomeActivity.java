package com.example.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {
    Button userProfile, logout, profilePhoto, familyInfo;
    public static String authToken, userID;
    TextView dataview;
    public static final String INTENT_KEY = "INTENT_KEY";
    public static final String INTENT_KEY1 = "INTENT_KEY1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        authToken = LoginActivity.authenticationToken;
        userID = LoginActivity.userId;

        Log.d("userIdW",userID);

        dataview = (TextView) findViewById(R.id.data);
        String data = getIntent().getStringExtra(LoginActivity.INTENT_KEY1);
        dataview.setText("Welcome " + data);
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });

        userProfile = (Button) findViewById(R.id.profile);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEmailVerified checkEmailVerified = new CheckEmailVerified();
                checkEmailVerified.set(WelcomeActivity.this);
                checkEmailVerified.execute();
                Intent intent = new Intent(WelcomeActivity.this, UserInformationActivity.class);
                intent.putExtra(INTENT_KEY, authToken);
                intent.putExtra(INTENT_KEY1, userID);
                startActivity(intent);
            }
        });

        profilePhoto = (Button) findViewById(R.id.profilePhoto);
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ProfilePhoto.class);
                intent.putExtra(INTENT_KEY, authToken);
                startActivity(intent);
            }
        });

        familyInfo = (Button) findViewById(R.id.familyInfo);
        familyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, FamilyInformation.class);
                intent.putExtra(INTENT_KEY, authToken);
                startActivity(intent);
            }
        });
    }
}