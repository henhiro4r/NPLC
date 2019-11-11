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

public class ScanViewModel extends ViewModel {
    private MutableLiveData<String> message = new MutableLiveData<>();

    public void play(String code, String user_id){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://7thnplc.wowrackcustomers.com/webservice/scan.php";
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("play_id", code);

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    String msg = responseObject.getString("message");
                    if (msg.equals("Good luck, have fun!") || msg.equals("Success!")){
                        message.setValue(msg);
                    } else {
                        message.setValue(msg);
                    }
                } catch (Exception e) {
                    Log.d("exceptionScan", Objects.requireNonNull(e.getMessage()));
                    message.setValue(e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("OnFailureScan", Objects.requireNonNull(error.getMessage()));
                message.setValue(error.getMessage());
            }
        });
    }

    public LiveData<String> getMessage() {
        return message;
    }
}
