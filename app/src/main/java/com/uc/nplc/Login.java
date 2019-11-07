package com.uc.nplc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

    SharedPreferences userPref;
    SharedPreferences.Editor userEditor;
    private ProgressDialog pd;
    private AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    private ActionBar bar;
    String username = "", password = "";
    EditText editUsername, editPassword;
    Button btnLogin;
    RequestQueue requestQueue;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bar = getSupportActionBar();
        bar.hide();
        pd = new ProgressDialog(Login.this);
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);

        userPref = getSharedPreferences("user", MODE_PRIVATE);
        userEditor = userPref.edit();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                username = editUsername.getText().toString().trim();
                password = editPassword.getText().toString().trim();
                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else{
                    pd.setCancelable(false);
                    pd.setTitle("Loading");
                    pd.setMessage("Please wait, logging in...");
                    pd.show();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            login(username, password);
                        }
                    };
                    Handler cancel = new Handler();
                    cancel.postDelayed(r, 1000);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showMessage("Camera Access granted");
            } else {
                showMessage("Camera Access denied");
            }
        }
    }

    public void login(String username, String password){
        requestQueue = Volley.newRequestQueue(Login.this);
        String url = "https://7thnplc.wowrackcustomers.com/webservice/login.php";
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);

        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject user;
                            String message = null;
                            try {
                                message = response.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (message.equals("welcome")){
                                user = response.getJSONObject("user");
                                userEditor.putString("id", user.getString("id"));
                                userEditor.putString("name", user.getString("name"));
                                userEditor.commit();
                                Intent in = new Intent(Login.this, MainActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(in);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                                finish();
                                showMessage("Good luck, Have fun! "+ userPref.getString("name","-") +"!");
                            } else {
                                showMessage(message);
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
        Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        Toast.makeText(Login.this, "Press again to exit!", Toast.LENGTH_SHORT).show();
    }
}
