package com.derp.davidshi.sogo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginPage extends AppCompatActivity {

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
                    Toast.makeText(LoginPage.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                } else {
                    ref = new Firebase("https://incandescent-fire-7723.firebaseIO.com/");

                    ref.createUser(psnEmail.getText().toString(), psnPass.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            Toast.makeText(LoginPage.this, "Sign up success!", Toast.LENGTH_LONG).show();

                            ref.authWithPassword(psnEmail.getText().toString(), psnPass.getText().toString(), new Firebase.AuthResultHandler() {
                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    SharedPreferences settings;
                                    SharedPreferences.Editor editor;
                                    settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
                                    editor = settings.edit();

                                    editor.putString("UserID", authData.getUid());
                                    editor.commit();

                                    ref.child("userid").child("person" + 4).setValue(authData.getUid());

                                    startActivity(new Intent(LoginPage.this, Intro1.class));

                                    finish();
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {
                                    Toast.makeText(LoginPage.this, firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(LoginPage.this, firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((psnEmail.getText().length() <= 0) || (psnPass.getText().length() <= 0)) {
                    Toast.makeText(LoginPage.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                } else {
                    ref = new Firebase("https://incandescent-fire-7723.firebaseIO.com/");

                    ref.authWithPassword(psnEmail.getText().toString(), psnPass.getText().toString(), new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Toast.makeText(LoginPage.this, "Log in success!", Toast.LENGTH_LONG).show();
                            SharedPreferences settings;
                            SharedPreferences.Editor editor;
                            settings = getApplicationContext().getSharedPreferences("Accntprefs", Context.MODE_PRIVATE);
                            editor = settings.edit();

                            editor.putString("UserID", authData.getUid());
                            editor.commit();

                            startActivity(new Intent(LoginPage.this, Matches.class));

                            // Prevents user from going back to this LoginPage
                            //finish();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(LoginPage.this, firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }) ;
    }
}

