package com.uc.nplc.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.uc.nplc.model.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class PostViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Post>> listPost = new MutableLiveData<>();

    public void setListPost(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://7thnplc.wowrackcustomers.com/webservice/post_list.php";
        final ArrayList<Post> posts = new ArrayList<>();

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("list");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject post = list.getJSONObject(i);
                        Post p = new Post();
                        p.setPost_id(post.getString("id"));
                        p.setTitle(post.getString("title"));
                        p.setLocation(post.getString("location"));
                        p.setType(post.getString("type"));
                        posts.add(p);
                    }
                    listPost.postValue(posts);
                } catch (Exception e){
                    Log.d("exceptionPost", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("OnFailurePost", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public LiveData<ArrayList<Post>> getPost(){
        return listPost;
    }
}
