package com.derp.davidshi.sogo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {

    private EditText psnName;
    private EditText psnEmail;
    private EditText psnPass;
    private Button btnSignup;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        Firebase myFirebaseRef = new Firebase("https://incandescent-fire-7723.firebaseIO.com/");

        psnName = (EditText) findViewById(R.id.psnName);
        psnEmail = (EditText) findViewById(R.id.psnEmail);
        psnPass = (EditText) findViewById(R.id.psnPass);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((psnName.getText().length() <= 0) && (psnEmail.getText().length() <= 0) && (psnPass.getText().length() <= 0)) {
                    Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                } else {
                    
                }
            }
        )};
    }
}

