package com.derp.davidshi.sogo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class profilePage extends AppCompatActivity {

    private String userID, matchID;
    private Button btnMatchPage, bFitness, bAcademics, bFriends, bOther;
    private TextView match_results;

    private String getMatchID(){
        // shared preferences
        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
        return settings.getString("Match", null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);


        // shared preferences
        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
        userID = settings.getString("UserID", null);


        // set buttons
        btnMatchPage = (Button) findViewById(R.id.btnMatchPage);
        bFitness = (Button) findViewById(R.id.fitness);
        bAcademics = (Button) findViewById(R.id.academics);
        bFriends = (Button) findViewById(R.id.friends);
        bOther = (Button) findViewById(R.id.other);



        // add listeners
        View.OnClickListener btnMatchPage = new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(Matches.this, MatchesPage.class));
            }

        View.OnClickListener fitnessList = new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // shared setting stuff
                SharedPreferences settings;
                SharedPreferences.Editor editor;
                settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
                editor = settings.edit();

                editor.putString("Category","Fitness");
                editor.commit();


                // to do: switch activities
                startActivity(new Intent(profilePage.this, listGoals.class));
                return;
            }
        };

        View.OnClickListener academicsList = new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // shared setting stuff
                SharedPreferences settings;
                SharedPreferences.Editor editor;
                settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
                editor = settings.edit();

                editor.putString("Category","Academics");
                editor.commit();


                // to do: switch activities
                startActivity(new Intent(profilePage.this, listGoals.class));
                return;
            }
        };

        View.OnClickListener friendsList = new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // shared setting stuff
                SharedPreferences settings;
                SharedPreferences.Editor editor;
                settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
                editor = settings.edit();

                editor.putString("Category","Friends");
                editor.commit();

                startActivity(new Intent(profilePage.this, listGoals.class));
                return;
            }
        };

        View.OnClickListener otherList = new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // shared setting stuff
                SharedPreferences settings;
                SharedPreferences.Editor editor;
                settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
                editor = settings.edit();

                editor.putString("Category","Other");
                editor.commit();

                startActivity(new Intent(profilePage.this, listGoals.class));
                return;
            }
        };


        // set listeners to listen
        bFitness.setOnClickListener(fitnessList);
        bAcademics.setOnClickListener(academicsList);
        bFriends.setOnClickListener(friendsList);
        bOther.setOnClickListener(otherList);

        // now we need to display the matched results!
        matchID = getMatchID();

        Firebase ref = new Firebase("https://incandescent-fire-7723.firebaseIO.com/");



        // Attach an listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                snapshot.child("users").child(matchID);
                return;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
                return;
            }

        });

        // change the value to force a Data Change

        ref.child("Changethis").setValue(Math.random());




    }


}
