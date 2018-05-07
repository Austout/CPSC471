package com.example.austi.feedme;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText EmailEditText,passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b10 = (Button) findViewById(R.id.button);
        Button b11 = (Button) findViewById(R.id.button2);
        EmailEditText = (EditText) findViewById(R.id.editText2);
        passwordEditText = (EditText) findViewById(R.id.passwordLogin);
        b10.setBackgroundColor(Color.parseColor("#00000F"));
        b10.setTextColor(Color.WHITE);

        b11.setBackgroundColor(Color.parseColor("#00000F"));
        b11.setTextColor(Color.WHITE);
        requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
    }
    public void registrationOnClick(View v){
        Intent startRegistration = new Intent(this, Registration.class);
        startRegistration.putExtra(Registration.UserNameIntentKey,EmailEditText.getText());
        startActivity(startRegistration);
    }
    private static final int INITIAL_REQUEST=1337;
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    public static int userID = -1;
    public void loginInOnClick(View v){
        String request        = "http://136.159.47.124/php/userExistsHook.php/";
        String email = EmailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        ArrayList<String> bodies = new ArrayList<String>();
        bodies.add("email");
        bodies.add(email);
        bodies.add("password");
        bodies.add(password);
        Thread mThread = new QueryThread(request,bodies);
        mThread.start();
        try {
            mThread.join();
            Log.e("theseResults",QueryThread.result);
            if(!QueryThread.result.contains("false")){ //TODO
                userID = Integer.parseInt(QueryThread.result);
                Intent startLandingPage = new Intent(this,LandingPage.class);
                startActivity(startLandingPage);
            }else{
                Snackbar.make(findViewById(R.id.main_layout), "Invalid password",
                        Snackbar.LENGTH_SHORT)
                        .show();
//                Toast.makeText(getApplicationContext(),"Invalid Email/Password",Toast.LENGTH_LONG);
            }
        }catch (Exception e){
            Log.e("error in thread" , e.toString());
            e.printStackTrace();
        }

    }
}
