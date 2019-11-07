package com.uc.nplc.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uc.nplc.R;
import com.uc.nplc.card.CardHistory;
import com.uc.nplc.model.History;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistory extends Fragment {


    public FragmentHistory() {

    }

    private String hsl = "", idHis = "", idPos = "", hMy = "", idVs = "", hVs = "", created = "", userid = "";

    private ArrayList<History> listHistory = new ArrayList<>();
    private RecyclerView rv_history;
    private ProgressBar pbHistory;
    private TextView txtTidakAda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.fragment_history, container, false);
        SharedPreferences userPref = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE);
        userid = userPref.getString("id","-");
        rv_history = v.findViewById(R.id.rv_fr_history);
        pbHistory = v.findViewById(R.id.pb_history);
        showLoading(true);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadHistory(userid);
    }

    private void loadHistory(String id){
        listHistory.clear();
        rv_history.setAdapter(null);
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        String url = "https://7thnplc.wowrackcustomers.com/webservice/fetch_history.php";
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        JSONObject parameters = new JSONObject(params);
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray hasil = null;
                            try {
                                hasil = response.getJSONArray("history");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(Objects.requireNonNull(hasil).length() == 0){
                                txtTidakAda.setVisibility(View.VISIBLE);
                            }else{
                                for (int i = 0; i < hasil.length(); i++) {
                                    JSONObject jsonObject = hasil.getJSONObject(i);
                                    hsl = jsonObject.getString("msg");
                                    if(hsl.equalsIgnoreCase("null")){
                                        //
                                    }else{
                                        idHis = jsonObject.getString("id");
                                        idPos = jsonObject.getString("game_id");
                                        hMy = jsonObject.getString("status");
                                        idVs = jsonObject.getString("point");
                                        hVs = jsonObject.getString("htim2");
                                        created = jsonObject.getString("time_start");
                                        History h = new History(idHis,idPos,hMy,idVs,hVs,created);
                                        listHistory.add(h);
                                    }
                                }
                            }
                            if (getActivity()!=null) {
                                showLoading(false);
                                showHistory(listHistory);
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

    private void showHistory(ArrayList<History> list){
        rv_history.setLayoutManager(new LinearLayoutManager(getActivity()));
        CardHistory historyAdapter = new CardHistory(getActivity());
        historyAdapter.setListHistory(list);
        rv_history.setAdapter(historyAdapter);
    }

    private void showLoading(Boolean state) {
        if (state) {
            pbHistory.setVisibility(View.VISIBLE);
        } else {
            pbHistory.setVisibility(View.GONE);
        }
    }
}
