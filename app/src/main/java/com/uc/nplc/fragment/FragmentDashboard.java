package com.uc.nplc.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uc.nplc.Login;
import com.uc.nplc.PlayScanner;
import com.uc.nplc.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashboard extends Fragment {


    public FragmentDashboard() {

    }

    private TextView pointNow, pointUsed;
    private SharedPreferences.Editor userEditor;
    private RequestQueue requestQueue;

    private ProgressDialog pd;
    private AlphaAnimation click = new AlphaAnimation(1F, 0.6F);

    private String idTim = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        SharedPreferences userPref = Objects.requireNonNull(getActivity()).getSharedPreferences("user", getActivity().MODE_PRIVATE);
        userEditor = userPref.edit();

        pd = new ProgressDialog(getActivity());
        ImageView logout = v.findViewById(R.id.img_logout_fr_dashboard);
        ImageView main = v.findViewById(R.id.img_scan_fr_dashboard);
        ImageView refresh = v.findViewById(R.id.img_refresh_fr_dashboard);
        TextView txt_tim = v.findViewById(R.id.txt_nama_tim_fr_dashboard);
        pointNow = v.findViewById(R.id.txt_pointN);
        pointUsed = v.findViewById(R.id.txt_pointU);
        idTim = userPref.getString("id","-");

        String greeting = "Hi, "+ userPref.getString("name","-")+"!";
        txt_tim.setText(greeting);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(click);
                pd.setCancelable(false);
                pd.setTitle("Loading");
                pd.setMessage("Updating, please wait...");
                pd.show();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        loadData(idTim);
                    }
                };
                Handler cancel = new Handler();
                cancel.postDelayed(r, 2000);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(click);
                new AlertDialog.Builder(getActivity())
                        .setTitle("\uD83D\uDCCC Confirmation")
                        .setMessage("Are you sure want to logout from 7th NPLC?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pd.setCancelable(false);
                                pd.setTitle("Loading");
                                pd.setMessage("Logging out, please wait...");
                                pd.show();
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        logout(idTim);
                                    }
                                };
                                Handler cancel = new Handler();
                                cancel.postDelayed(r, 2000);
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(click);
                Intent i = new Intent(getActivity(), PlayScanner.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                Objects.requireNonNull(getActivity()).finish();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData(idTim);
    }

    private void loadData(String idTim) {
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        String url = "https://7thnplc.wowrackcustomers.com/webservice/refresh.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", idTim);
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = null;
                    JSONObject updates;
                    try {
                        message = response.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (Objects.requireNonNull(message).equals("update")){
                        updates = response.getJSONObject("update");
                        String pnow = updates.getString("point")+ " pts";
                        String puse = updates.getString("used")+ " pts";
                        pointNow.setText(pnow);
                        pointUsed.setText(puse);
                        userEditor.commit();
                        pd.cancel();
                    } else {
                        pd.cancel();
                        showMessage(message);
                    }
                } catch (JSONException e) {
                    pd.cancel();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
            }
        });
        requestQueue.add(jor);
    }

    private void logout(String id_tim){
        requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        String url = "https://7thnplc.wowrackcustomers.com/webservice/logout.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", id_tim);
        JSONObject parameters = new JSONObject(params);

        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String message = null;
                        try {
                            message = response.getString("out");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(Objects.requireNonNull(message).equalsIgnoreCase("out_ok")){
                            showMessage("Thank you for playing!");
                            userEditor.putString("id","-");
                            userEditor.putString("name","-");
                            userEditor.commit();
                            Intent in = new Intent(getActivity(), Login.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(in);
                            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            getActivity().finishAffinity();
                        }else if(message.equalsIgnoreCase("out_no")){
                            showMessage("Log out failed, please try again!");
                        }
                        pd.cancel();
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
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}
