package com.uc.nplc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.uc.nplc.model.Bantuan;

import java.util.ArrayList;

public class BantuanDetail extends AppCompatActivity {

    ActionBar bar;
    TextView txt_title, txt_des, txt_updated;
    ArrayList<Bantuan> lb = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantuan_detail);

        bar = getSupportActionBar();
        bar.setElevation(0);
        bar.setTitle("Penjelasan Bantuan");
        //bar.hide();

        txt_title = findViewById(R.id.txt_title_bantuan_detail);
        txt_des = findViewById(R.id.txt_des_bantuan_detail);
        txt_updated = findViewById(R.id.txt_updated_bantuan_detail);

        Intent i = getIntent();
        lb = (ArrayList<Bantuan>)getIntent().getSerializableExtra("lb");

        txt_title.setText(lb.get(0).getJudul());
        txt_des.setText(lb.get(0).getDes());
        txt_updated.setText(lb.get(0).getUpdated());


    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
