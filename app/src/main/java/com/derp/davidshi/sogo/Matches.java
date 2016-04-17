package com.derp.davidshi.sogo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Random;

public class Matches extends AppCompatActivity {

    private String userID;
    private String Match;

    private String firstName;
    private String lastName;

    private Button btnMatch;

    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        startService(new Intent(this, MyService.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
        userID = settings.getString("UserID", null);
        Match = settings.getString("Match", null);
        editor = settings.edit();

        ref = new Firebase("https://incandescent-fire-7723.firebaseIO.com/");

        Random rand = new Random();

        ref.child("Changethis").setValue(rand.nextDouble());

        ref.addValueEventListener(new ValueEventListener() {
            // Retrieve new posts as they are added to the database
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                firstName = (String) snapshot.child("users").child(Match).child("FirstName").getValue();
                lastName = (String) snapshot.child("users").child(Match).child("LastName").getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        btnMatch = (Button) findViewById(R.id.btnMatch);
        btnMatch.setText("Someone" + btnMatch.getText());

        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Matches.this, MatchesPage.class));
            }
        });
    }
}
