package com.derp.davidshi.sogo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText psnEmail;
    private EditText psnPass;
    private Button btnSignup;
    private Button btnLogin;

    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        psnEmail = (EditText) findViewById(R.id.psnEmail);
        psnPass = (EditText) findViewById(R.id.psnPass);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        ref = new Firebase("https://incandescent-fire-7723.firebaseIO.com/");

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((psnEmail.getText().length() <= 0) || (psnPass.getText().length() <= 0)) {
                    Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                } else {
                    ref = new Firebase("https://incandescent-fire-7723.firebaseIO.com/");

                    ref.createUser(psnEmail.getText().toString(), psnPass.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this, intro1.class));
                        }
                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(MainActivity.this, firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((psnEmail.getText().length() <= 0) || (psnPass.getText().length() <= 0)) {
                    Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                } else {
                    ref = new Firebase("https://incandescent-fire-7723.firebaseIO.com/");

                    ref.authWithPassword(psnEmail.getText().toString(), psnPass.getText().toString(), new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(MainActivity.this, firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }) ;
    }
}

