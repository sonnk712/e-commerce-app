package com.LTTBDD.ecommerce_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.model.User;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                }catch (Exception e){

                }finally {
                    User currUser = DataLocalManager.getCurrUser();
                    if(currUser != null){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        };
        thread.start();
    }
}