package com.example.austi.feedme;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import static com.example.austi.feedme.DeliveryContainer.dContainerKey;

public class UserSettings extends AppCompatActivity {
    private EditText editTexts[] = new EditText[4];
    private static final String[] updateFields = {"Name", "phoneNum","email","password"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        Button update = (Button) findViewById(R.id.update);
        editTexts[0] = (EditText) findViewById(R.id.nameUpdateEditText);
        editTexts[1] = (EditText) findViewById(R.id.PhoneNumberSettings);
        editTexts[2] = (EditText) findViewById(R.id.EmailTextBoxSettings);
        editTexts[3] = (EditText) findViewById(R.id.PasswordUpdateEditText);


        update.setBackgroundColor(Color.parseColor("#CC001E"));
        update.setTextColor(Color.WHITE);

    }
    public void updateOnClick(View v){
        for(int i = 0; i < editTexts.length; i++){
            String text = editTexts[i].getText().toString();
            if(!text.equals("")){
                String request = "http://136.159.47.124/php/updateDynamicallyStudentHook.php/";
                ArrayList<String> bodies = new ArrayList<String>();
                bodies.add("field");
                bodies.add(updateFields[i]);
                bodies.add("value");
                bodies.add(text);
                bodies.add("studentid");
                bodies.add(MainActivity.userID + "");
                Thread mThread = new QueryThread(request,bodies);
                mThread.start();
                try {
                    mThread.join();
                    Log.e("theseResults",QueryThread.result);
                    if(!QueryThread.result.contains("false")){
                        String orderID = QueryThread.result;
                        Intent startDeliveryLock = new Intent(this,DeliveryLockPage.class);
                        startDeliveryLock.putExtra(dContainerKey,orderID);
                        startActivityForResult(startDeliveryLock,2);
                    }
                }catch (Exception e){
                    Log.e("error in thread" , e.toString());
                    e.printStackTrace();
                }
            }
        }
        Intent update = new Intent(this,LandingPage.class);
        startActivity(update);
    }
}
