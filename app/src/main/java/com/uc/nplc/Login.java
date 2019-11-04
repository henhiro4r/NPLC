package com.uc.nplc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {

    SharedPreferences phonePref, userPref;
    SharedPreferences.Editor phoneEditor, userEditor;
    TelephonyManager tm;
    private ProgressDialog pd;
    private AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    private ActionBar bar;
    String kode = "";

    EditText edit_kode;
    Button btn_mulai;

    RequestQueue requestQueue;

    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions= new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bar = getSupportActionBar();
        bar.hide();

        pd = new ProgressDialog(Login.this);
        tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        edit_kode = findViewById(R.id.edit_kode_login);
        btn_mulai = findViewById(R.id.btn_mulai_login);

        phonePref = getSharedPreferences("phone", MODE_PRIVATE);
        phoneEditor = phonePref.edit();

        userPref = getSharedPreferences("user", MODE_PRIVATE);
        userEditor = userPref.edit();

        btn_mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                kode = edit_kode.getText().toString().trim();
                if(TextUtils.isEmpty(kode)){
                    Toast.makeText(Login.this, "Masukkan kode tim terlebih dahulu!", Toast.LENGTH_SHORT).show();
                }else{
                    pd.setCancelable(false);
                    pd.setTitle("Dalam proses");
                    pd.setMessage("Sedang masuk ke apps, mohon tunggu...");
                    pd.show();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            login(kode);
                        }
                    };
                    Handler cancel = new Handler();
                    cancel.postDelayed(r, 1000);

                }

            }
        });

    }

    public void login(String code){
        requestQueue = Volley.newRequestQueue(Login.this);
        String url = "https://arjekindonesia.com/webservice_nplc/login.php";
        Map<String, String> params = new HashMap<>();
        params.put("kode", code);
        params.put("imei", phonePref.getString("imei","-"));
        JSONObject parameters = new JSONObject(params);

        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray hasil = null;
                            try {
                                hasil = response.getJSONArray("user");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < hasil.length(); i++) {
                                JSONObject jsonObject = hasil.getJSONObject(i);
                                String hsl = jsonObject.getString("msg");
                                String id = jsonObject.getString("id");
                                String nama = jsonObject.getString("nama");
                                String imei = jsonObject.getString("imei");
                                String coach = jsonObject.getString("coach");
                                String point = jsonObject.getString("point");

                                if(hsl.equalsIgnoreCase("notfound")){
                                    showMessage("Tim dengan kode tersebut tidak terdaftar!");
                                    edit_kode.setText("");
                                }else if(hsl.equalsIgnoreCase("notsame")){
                                    showMessage("Tim anda tidak bisa bermain dengan HP lain!");
                                    edit_kode.setText("");

                                }else if(hsl.equalsIgnoreCase("imeiisused")){
                                    showMessage("1 HP untuk 1 Tim saja!");
                                    edit_kode.setText("");

                                }else if(hsl.equalsIgnoreCase("welcome")){

                                    userEditor.putString("id",id);
                                    userEditor.putString("nama",nama);
                                    userEditor.putString("imei",imei);
                                    userEditor.putString("coach",coach);
                                    userEditor.putString("point",point);
                                    userEditor.commit();

                                    Intent in = new Intent(Login.this, MainActivity.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();
                                    showMessage("Selamat bermain & selamat berkompetisi Tim "+userPref.getString("nama","-")+"!");
                                }else if(hsl.equalsIgnoreCase("welcome2")){
                                    userEditor.putString("id",id);
                                    userEditor.putString("nama",nama);
                                    userEditor.putString("imei",imei);
                                    userEditor.putString("coach",coach);
                                    userEditor.putString("point",point);
                                    userEditor.commit();

                                    Intent in = new Intent(Login.this, MainActivity.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.enter, R.anim.exit);
                                    finish();
                                    showMessage("Selamat datang kembali Tim "+userPref.getString("nama","-")+"!");
                                }
                            }
                            pd.cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pd.cancel();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                        pd.cancel();
                    }
                }
        );
        requestQueue.add(jor);

    }

    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(checkPermissions()){}
    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissionsList, int[] grantResults){
        //permissionsDenied = "";
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    for (String per : permissionsList) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
                            return;
                        }else{
                            String imeiNumber = tm.getDeviceId();
                            phoneEditor.putString("imei", imeiNumber);
                            phoneEditor.commit();
                        }

                    }
                }

                return;
            }
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
                        return;
                    }else{
                        String imeiNumber = tm.getDeviceId();
                        phoneEditor.putString("imei", imeiNumber);
                        phoneEditor.commit();
                    }

                } else {
                    //Toast.makeText(MainActivity.this,"Without permission we check",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        /*if(permissionsDenied.equals("")){
            btn_mulai.setClickable(true);
            btn_mulai.setEnabled(true);
        }else{
            Toast.makeText(Login.this,"You must to allow all permissions to play! Exit the application & open again!", Toast.LENGTH_SHORT).show();
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
                finish();
        }*/
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
        Toast.makeText(Login.this, "Tekan lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
    }


}
