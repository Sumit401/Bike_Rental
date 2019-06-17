package com.example.bike_rental;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences=getSharedPreferences("Login",MODE_PRIVATE);
                if(preferences.getString("ID",null)==null && preferences.getString("Name",null)==null
                        && preferences.getString("Email",null)==null && preferences.getString("Mobile",null)==null){

                    Intent intent=new Intent(Splash.this,MainActivity1.class);
                    startActivity(intent);
                    finish();
                }
                else {

                    Intent intent=new Intent(Splash.this,MainActivity2.class);
                    startActivity(intent);
                    finish();

                }

            }
        },1500);
    }
}
