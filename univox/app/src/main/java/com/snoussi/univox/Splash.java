package com.snoussi.univox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
    private static int splash_screen = 5000;
    Animation topanim,botanime;
    ImageView logo;
    TextView text,text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        topanim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botanime = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        topanim.setDuration(1400);
        botanime.setDuration(1400);


        logo = findViewById(R.id.logo);
        text = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);

        logo.setAnimation(topanim);
        text.setAnimation(botanime);
        text2.setAnimation(botanime);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        },splash_screen);
    }
}
