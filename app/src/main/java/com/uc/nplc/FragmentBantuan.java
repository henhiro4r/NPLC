package com.uc.nplc;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uc.nplc.card.CardBantuan;
import com.uc.nplc.card.CardHistory;
import com.uc.nplc.model.Bantuan;
import com.uc.nplc.model.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBantuan extends Fragment {


    public FragmentBantuan() {
        // Required empty public constructor
    }

    private AlphaAnimation klik = new AlphaAnimation(1F, 0.6F);
    RequestQueue requestQueue;
    String hsl = "", id = "", judul = "", des = "", updated = "", userid = "";

    ArrayList<Bantuan> listBantuan = new ArrayList<>();
    ArrayList<Bantuan> lb = new ArrayList<>();
    RecyclerView rv_bantuan;
    SharedPreferences userPref;
    ProgressBar progBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_bantuan, container, false);

        userPref = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        userid = userPref.getString("id","-");
        rv_bantuan = v.findViewById(R.id.rv_fr_bantuan);


        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        loadBantuan();
    }

    public void loadBantuan(){
        listBantuan.clear();
        rv_bantuan.setAdapter(null);
        requestQueue = Volley.newRequestQueue(getActivity());
        String url = "https://arjekindonesia.com/webservice_nplc/fetch_bantuan.php";
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //progBar.setVisibility(View.INVISIBLE);
                        try {
                            JSONArray hasil = null;
                            try {
                                hasil = response.getJSONArray("bantuan");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(hasil.length() == 0){

                            }else{
                                for (int i = 0; i < hasil.length(); i++) {
                                    JSONObject jsonObject = hasil.getJSONObject(i);
                                    id = jsonObject.getString("id");
                                    judul = jsonObject.getString("judul");
                                    des = jsonObject.getString("des");
                                    updated = jsonObject.getString("updated_at");

                                    Bantuan b = new Bantuan(id,judul,des,updated);
                                    listBantuan.add(b);




                                }
                            }

                            if (getActivity()!=null) {
                                showBantuan(listBantuan);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        requestQueue.add(jor);
    }


    public void showBantuan(ArrayList<Bantuan> list){
        rv_bantuan.setLayoutManager(new LinearLayoutManager(getActivity()));
        CardBantuan bantuanAdapter = new CardBantuan(getActivity());
        bantuanAdapter.setListBantuan(list);
        rv_bantuan.setAdapter(bantuanAdapter);

        ItemClickSupport.addTo(rv_bantuan).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                v.startAnimation(klik);
                lb.clear();
                Intent i = new Intent(getActivity(), BantuanDetail.class);
                Bantuan b = listBantuan.get(position);
                lb.add(b);
                i.putExtra("lb", lb);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

    }


}
