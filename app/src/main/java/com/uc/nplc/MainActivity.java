package com.uc.nplc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    ActionBar bar;
    private AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);

    SharedPreferences userPref, phonePref;
    SharedPreferences.Editor userEditor, phoneEditor;

    String tim = "", coach = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phonePref = getSharedPreferences("phone", MODE_PRIVATE);
        userPref = getSharedPreferences("user", MODE_PRIVATE);
        tim = userPref.getString("nama","-");
        coach = userPref.getString("coach","-");

        bar = getSupportActionBar();
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
                    //bar.setTitle("Hi, Tim "+tim+"!");
                    //bar.setSubtitle("Coach: "+coach);
                    bar.setElevation(0);
                    bar.hide();
                    //bar.setIcon(R.drawable.logo_putih_landscape);
                    FragmentDashboard dashboard = new FragmentDashboard();
                    FragmentTransaction ftDashboard = getSupportFragmentManager().beginTransaction();
                    ftDashboard.replace(R.id.frame_main, dashboard, "Dashboard");
                    ftDashboard.commit();
                    return true;
                case R.id.navigation_portal:
                    bar.show();
                    bar.setTitle("Portal Master Game");
                    //bar.setSubtitle(null);
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
                    bar.setTitle("Riwayat Permainan");
                    //bar.setSubtitle(null);
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
                    //bar.setSubtitle(null);
                    bar.setElevation(0);
                    bar.setDisplayShowHomeEnabled(false);
                    bar.setLogo(R.mipmap.ic_launcher_round);
                    bar.setDisplayUseLogoEnabled(false);
                    FragmentBantuan bantuan = new FragmentBantuan();
                    FragmentTransaction ftBantuan = getSupportFragmentManager().beginTransaction();
                    ftBantuan.replace(R.id.frame_main, bantuan, "Bantuan");
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
                //noinspection RestrictedApi
                item.setShifting(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {

        } catch (IllegalAccessException e) {

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
        Toast.makeText(MainActivity.this, "Tekan lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
    }

}
