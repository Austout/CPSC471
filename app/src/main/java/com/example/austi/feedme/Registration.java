package com.example.austi.feedme;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Registration extends AppCompatActivity {
    public final static String UserNameIntentKey = "Registration.Name:";
    private EditText usernameEditText,passwordEditText,DOBEditText,PhoneNumberEditText,
    EmailEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Button b11 = (Button) findViewById(R.id.button5);
        DOBEditText = (EditText) findViewById(R.id.editText3);
        PhoneNumberEditText = (EditText) findViewById(R.id.editText4);
        EmailEditText = (EditText) findViewById(R.id.editText5);
        usernameEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.passwordRegistration);
        Intent currIntent = getIntent();
        EmailEditText.setText(currIntent.getStringExtra(UserNameIntentKey));
        b11.setBackgroundColor(Color.parseColor("#CC001E"));
        b11.setTextColor(Color.WHITE);
    }
    public void signUpOnClick(View v){
        Log.e("running thread", "starting http client");
        String dob = DOBEditText.getText().toString();
        String phonenum = PhoneNumberEditText.getText().toString();
        String email = EmailEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String request        = "http://136.159.47.124/php/hookTest.php/";
        ArrayList<String> bodies = new ArrayList<String>();
        bodies.add("dob");
        bodies.add(dob);
        bodies.add("email");
        bodies.add(email);
        bodies.add("phonenum");
        bodies.add(phonenum);
        bodies.add("username");
        bodies.add(username);
        bodies.add("password");
        bodies.add(password);
        Thread mThread = new QueryThread(request,bodies);
        mThread.start();
        try {
            mThread.join();
            Log.e("theseResults",QueryThread.result);
        }catch (Exception e){
            Log.e("error in thread" , e.toString());
            e.printStackTrace();
        }
        finish();
    }
}
