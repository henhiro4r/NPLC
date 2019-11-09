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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uc.nplc.R;
import com.uc.nplc.card.CardHistory;
import com.uc.nplc.model.History;
import com.uc.nplc.preference.Pref;

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

    private String status = "", game_id = "", point = "", time_start = "", userid = "", message = "";

    private ArrayList<History> listHistory = new ArrayList<>();
    private RecyclerView rv_history;
    private ProgressBar pbHistory;
    private Pref pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.fragment_history, container, false);
        pref = new Pref(getActivity());
        userid = pref.getIdKey();
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
                            JSONArray result;
                            try {
                                message = response.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(message.equals("null")){
                                showMessage();
                            }else{
                                result = response.getJSONArray("history");
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject jsonObject = result.getJSONObject(i);
                                    game_id = jsonObject.getString("game_id");
                                    status = jsonObject.getString("status");
                                    point = jsonObject.getString("point");
                                    time_start = jsonObject.getString("time_start");
                                    History h = new History(game_id, status, point, time_start);
                                    listHistory.add(h);
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

    private void showMessage() {
        Toast.makeText(getActivity(), "No record found!", Toast.LENGTH_SHORT).show();
    }
}
