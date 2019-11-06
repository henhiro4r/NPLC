package com.uc.nplc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class Spalsh extends AppCompatActivity {

    SharedPreferences userPref;
    SharedPreferences.Editor userEditor;

    ActionBar bar;
    ImageView img_logo, img_bg;
    Intent i = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        bar = getSupportActionBar();
        bar.hide();

        img_logo = findViewById(R.id.img_logo_spalsh);
        img_bg = findViewById(R.id.img_bg_spalsh);
        //i = new Intent(getApplicationContext(), Login.class);
        Animation anim = AnimationUtils.loadAnimation(Spalsh.this,R.anim.transition);
        img_logo.startAnimation(anim);
        img_bg.startAnimation(anim);

        userPref = getSharedPreferences("user", MODE_PRIVATE);
        userEditor = userPref.edit();

        if(userPref.getString("id", "-").equalsIgnoreCase("-")){
            i = new Intent(getApplicationContext(), Login.class);
        }else{
            i = new Intent(getApplicationContext(),MainActivity.class);
            Toast.makeText(this, "Selamat datang kembali Tim "+userPref.getString("name","-")+"!", Toast.LENGTH_SHORT).show();
        }

        Thread timer = new Thread(){
            public void run(){
                try {
                    sleep(2500);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }
}
