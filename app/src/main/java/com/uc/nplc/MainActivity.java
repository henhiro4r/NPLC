package com.uc.nplc;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uc.nplc.fragment.FragmentDashboard;
import com.uc.nplc.fragment.FragmentHistory;
import com.uc.nplc.fragment.FragmentPortal;
import com.uc.nplc.fragment.FragmentPost;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActionBar bar;
    public static final String FRAGMENT_TO_LOAD = "fragLoad";
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = getSupportActionBar();
        navigation = findViewById(R.id.nav_view_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null){
            navigation.setSelectedItemId(R.id.navigation_dashboard);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Objects.equals(getIntent().getStringExtra(FRAGMENT_TO_LOAD), "Quiz")) {
            navigation.setSelectedItemId(R.id.navigation_portal);
        } else {
            navigation.setSelectedItemId(R.id.navigation_dashboard);
        }
    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    bar.setTitle("Dashboard");
                    fragment = new FragmentDashboard();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_portal:
                    bar.setTitle("Portal Master Game");
                    fragment = new FragmentPortal();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_riwayat:
                    bar.setTitle("History Play");
                    fragment = new FragmentHistory();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_bantuan:
                    bar.setTitle(R.string.bantuan);
                    fragment = new FragmentPost();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    public boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(a);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(MainActivity.this, "Press again to exit!", Toast.LENGTH_SHORT).show();
    }

}
