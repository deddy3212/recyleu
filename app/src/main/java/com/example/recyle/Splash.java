package com.example.recyle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    private TextView tv;
    private ImageView iv;

    private int SLEEP_TIMER = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        Logolauncher logolauncher = new Logolauncher();
        logolauncher.start();

    }
    private class Logolauncher extends Thread {
        public void run (){
            try {
                sleep(1000 * SLEEP_TIMER);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            Intent intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            Splash.this.finish();
        }
    }
}
