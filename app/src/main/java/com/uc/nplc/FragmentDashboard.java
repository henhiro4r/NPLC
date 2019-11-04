package com.uc.nplc;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDashboard extends Fragment {


    public FragmentDashboard() {
        // Required empty public constructor
    }

    SharedPreferences userPref, phonePref;
    SharedPreferences.Editor userEditor, phoneEditor;
    RequestQueue requestQueue;

    private ProgressDialog pd;
    private AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    ImageView logout, main, refresh;
    TextView txtImei, txt_tim;

    String idTim = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        phonePref = getActivity().getSharedPreferences("phone", getActivity().MODE_PRIVATE);
        phoneEditor = phonePref.edit();

        userPref = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
        userEditor = userPref.edit();

        pd = new ProgressDialog(getActivity());
        logout = v.findViewById(R.id.img_logout_fr_dashboard);
        main = v.findViewById(R.id.img_scan_fr_dashboard);
        refresh = v.findViewById(R.id.img_refresh_fr_dashboard);
        txt_tim = v.findViewById(R.id.txt_nama_tim_fr_dashboard);
        //txtImei = v.findViewById(R.id.txtimei);
        //txtImei.setText(phonePref.getString("imei","-"));
        idTim = userPref.getString("id","-");

        txt_tim.setText("Hi, Tim "+userPref.getString("nama","-")+"!");

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(klik);
                new AlertDialog.Builder(getActivity())
                        .setTitle("\uD83D\uDCCC Konfirmasi")
                        .setMessage("Apakah anda yakin mau keluar dari 7th NPLC apps?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pd.setCancelable(false);
                                pd.setTitle("Dalam proses");
                                pd.setMessage("Sedang keluar dari apps, mohon tunggu...");
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
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
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
                v.startAnimation(klik);
                Intent i = new Intent(getActivity(), PlayScanner.class);
                //i.putExtra("idtrans", idtrans);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                getActivity().finish();
            }
        });

        return v;
    }

    public void logout(String id_tim){
        requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://arjekindonesia.com/webservice_nplc/logout.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", id_tim);
        JSONObject parameters = new JSONObject(params);

        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray hasil = null;
                            try {
                                hasil = response.getJSONArray("out");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < hasil.length(); i++) {
                                JSONObject jsonObject = hasil.getJSONObject(i);
                                String hsl = jsonObject.getString("msg");

                                if(hsl.equalsIgnoreCase("out_ok")){
                                    showMessage("Terima kasih telah bermain.");
                                    userEditor.putString("id","-");
                                    userEditor.putString("nama","-");
                                    userEditor.putString("imei","-");
                                    userEditor.putString("coach","-");
                                    userEditor.putString("point","-");
                                    userEditor.commit();
                                    Intent in = new Intent(getActivity(), Login.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(in);
                                    getActivity().overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        getActivity().finishAffinity();
                                    }else{
                                        ActivityCompat.finishAffinity(getActivity());
                                    }

                                }else if(hsl.equalsIgnoreCase("out_no")){
                                    showMessage("Keluar dari apps gagal! Silahkan coba lagi!");

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
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


}
