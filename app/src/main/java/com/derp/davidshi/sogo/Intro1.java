package com.derp.davidshi.sogo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Intro1 extends AppCompatActivity {

    private String userID;
    private Button bFitness, bAcademics, bFriends, bOther;
    private TextView first_name, last_name;

    private void saveName(){
        // shared setting stuff
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("FirstName",first_name.getText().toString());
        editor.putString("LastName",last_name.getText().toString());

        editor.commit();

        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro1);



        // shared preferences
        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
        userID = settings.getString("UserID", null);

        // set name form
        first_name = (TextView) findViewById(R.id.first_name);
        last_name = (TextView) findViewById(R.id.last_name);

        // set buttons
        bFitness = (Button) findViewById(R.id.fitness);
        bAcademics = (Button) findViewById(R.id.academics);
        bFriends = (Button) findViewById(R.id.friends);
        bOther = (Button) findViewById(R.id.other);



        // add listeners
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

                // save name
                saveName();

                // to do: switch activities
                startActivity(new Intent(Intro1.this, listGoals.class));
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

                saveName();

                // to do: switch activities
                startActivity(new Intent(Intro1.this, listGoals.class));
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

                saveName();

                startActivity(new Intent(Intro1.this, listGoals.class));
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

                saveName();

                startActivity(new Intent(Intro1.this, listGoals.class));
                return;
            }
        };



        // set listeners to listen
        bFitness.setOnClickListener(fitnessList);
        bAcademics.setOnClickListener(academicsList);
        bFriends.setOnClickListener(friendsList);
        bOther.setOnClickListener(otherList);


    }
}