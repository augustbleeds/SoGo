package com.derp.davidshi.sogo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class listGoals extends AppCompatActivity {

    private TextView Title;
    private TextView IG_1, IG_2, IG_3;          // Interest_Goals
    private Button Send;

    private String getCategory(){
        // shared preferences
        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
        return settings.getString("Category", null);
    }

    private String getID(){
        // shared preferences
        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
        return settings.getString("UserID", null);
    }

    private String getFirstName(){
        // shared preferences
        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
        return settings.getString("FirstName", null);
    }

    private String getLastName(){
        // shared preferences
        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
        return settings.getString("LastName", null);
    }



    private void sendInput(){
        // send data to firebase server
        String Category = getCategory();
        String UserID = getID();

        Firebase ref = new Firebase("https://incandescent-fire-7723.firebaseIO.com/");
        Firebase userRef = ref.child("users/"+UserID);

        String[] goals = new String[3];

        goals[0] = IG_1.getText().toString();
        goals[1] = IG_2.getText().toString();
        goals[2] = IG_3.getText().toString();

        if(IG_1.getText().toString().length() == 0){
            goals[0] = null;
        }

        if(IG_1.getText().toString().length() == 0){
            goals[1] = null;
        }

        if(IG_1.getText().toString().length() == 0){
            goals[2] = null;
        }

        userRef.child("FirstName").setValue(getFirstName());
        userRef.child("LastName").setValue(getLastName());
        userRef.child(Category).setValue(goals);


        return;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_goals);

        Title = (TextView) findViewById(R.id.title);  // linking variable with the textView

        String result = "Tell us about your goals & interests in " + getCategory();
        Title.setText(result);

        // associate 3 line views
        IG_1 = (TextView) findViewById(R.id.line1);  // linking variable with the textView
        IG_2 = (TextView) findViewById(R.id.line2);  // linking variable with the textView
        IG_3 = (TextView) findViewById(R.id.line3);  // linking variable with the textView

        // associate the button
        Send = (Button) findViewById(R.id.send);

        // add listeners
        View.OnClickListener send_response = new View.OnClickListener() {
            @Override
            public void onClick(View v){
                sendInput();
                // change activity to main activity!
                return;
            }
        };

        // set to listen
        Send.setOnClickListener(send_response);


    }
}
