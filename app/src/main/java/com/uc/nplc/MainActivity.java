package com.uc.nplc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uc.nplc.fragment.FragmentDashboard;
import com.uc.nplc.fragment.FragmentHistory;
import com.uc.nplc.fragment.FragmentPortal;
import com.uc.nplc.fragment.FragmentPost;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bar = getSupportActionBar();
        assert bar != null;
        bar.setElevation(0);
        bar.hide();

        FragmentDashboard dashboard = new FragmentDashboard();
        FragmentTransaction ftDashboard = getSupportFragmentManager().beginTransaction();
        ftDashboard.replace(R.id.frame_main, dashboard, "Dashboard");
        ftDashboard.commit();

        BottomNavigationView navigation = findViewById(R.id.nav_view_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(navigation);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    bar.setElevation(0);
                    bar.hide();
                    FragmentDashboard dashboard = new FragmentDashboard();
                    FragmentTransaction ftDashboard = getSupportFragmentManager().beginTransaction();
                    ftDashboard.replace(R.id.frame_main, dashboard, "Dashboard");
                    ftDashboard.commit();
                    return true;
                case R.id.navigation_portal:
                    bar.show();
                    bar.setTitle("Portal Master Game");
                    bar.setElevation(0);
                    bar.setDisplayShowHomeEnabled(false);
                    bar.setLogo(R.mipmap.ic_launcher_round);
                    bar.setDisplayUseLogoEnabled(false);
                    FragmentPortal portal = new FragmentPortal();
                    FragmentTransaction ftPortal = getSupportFragmentManager().beginTransaction();
                    ftPortal.replace(R.id.frame_main, portal, "Portal");
                    ftPortal.commit();
                    return true;
                case R.id.navigation_riwayat:
                    bar.show();
                    bar.setTitle("History play");
                    bar.setElevation(0);
                    bar.setDisplayShowHomeEnabled(false);
                    bar.setLogo(R.mipmap.ic_launcher_round);
                    bar.setDisplayUseLogoEnabled(false);
                    FragmentHistory history = new FragmentHistory();
                    FragmentTransaction ftHistory = getSupportFragmentManager().beginTransaction();
                    ftHistory.replace(R.id.frame_main, history, "History");
                    ftHistory.commit();
                    return true;
                case R.id.navigation_bantuan:
                    bar.show();
                    bar.setTitle(R.string.bantuan);
                    bar.setElevation(0);
                    bar.setDisplayShowHomeEnabled(false);
                    bar.setLogo(R.mipmap.ic_launcher_round);
                    bar.setDisplayUseLogoEnabled(false);
                    FragmentPost bantuan = new FragmentPost();
                    FragmentTransaction ftBantuan = getSupportFragmentManager().beginTransaction();
                    ftBantuan.replace(R.id.frame_main, bantuan, "Post");
                    ftBantuan.commit();
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("RestrictedAPI")
    public  void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {

        }
    }

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
