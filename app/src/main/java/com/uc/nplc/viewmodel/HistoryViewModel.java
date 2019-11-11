package com.uc.nplc.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uc.nplc.model.History;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class HistoryViewModel extends ViewModel {
    private MutableLiveData<ArrayList<History>> history = new MutableLiveData<>();

    public void loadHistory(String userId){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://7thnplc.wowrackcustomers.com/webservice/fetch_history.php";
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        final ArrayList<History> histories = new ArrayList<>();

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("history");
                    for (int i = 0; i < list.length(); i++){
                        JSONObject his = list.getJSONObject(i);
                        History h = new History();
                        h.setGame_id(his.getString("game_id"));
                        h.setStatus(his.getString("status"));
                        h.setPoint(his.getString("point"));
                        h.setTime_start(his.getString("time_start"));
                        histories.add(h);
                    }
                    history.postValue(histories);
                } catch (Exception e) {
                    Log.d("exceptionHistory", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("OnFailureHistory", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public LiveData<ArrayList<History>> getHistory(){
        return history;
    }
}
