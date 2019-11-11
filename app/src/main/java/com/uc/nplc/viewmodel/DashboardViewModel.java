package com.uc.nplc.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class DashboardViewModel extends ViewModel {
    private MutableLiveData<Boolean> isDone = new MutableLiveData<>();

    public void init(){

    }

    public void check(String user_id){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://7thnplc.wowrackcustomers.com/webservice/checker.php";
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    String msg = responseObject.getString("message");
                    if (msg.equals("ok")) {
                        isDone.setValue(true);
                    } else {
                        isDone.setValue(false);
                    }
                } catch (Exception e) {
                    Log.d("exceptionStat", Objects.requireNonNull(e.getMessage()));
                    isDone.setValue(false);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("OnFailureStat", Objects.requireNonNull(error.getMessage()));
                isDone.setValue(false);
            }
        });
    }

    public void refresh(){

    }

    public LiveData<Boolean> getIsDone() {
        return isDone;
    }
}
