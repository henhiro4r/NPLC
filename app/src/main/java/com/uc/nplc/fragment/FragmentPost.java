package com.uc.nplc.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uc.nplc.R;
import com.uc.nplc.card.CardPost;
import com.uc.nplc.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPost extends Fragment {


    public FragmentPost() {
        // Required empty public constructor
    }

    private String id = "", title = "", location = "", type = "";

    private ArrayList<Post> listPost = new ArrayList<>();
    private RecyclerView rv_post;
    private ProgressBar pbPost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_post, container, false);
        pbPost = v.findViewById(R.id.pb_post);
        rv_post = v.findViewById(R.id.rv_fr_bantuan);
        showLoading(true);
        loadPost();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void loadPost(){
        listPost.clear();
        rv_post.setAdapter(null);
        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        String url = "https://7thnplc.wowrackcustomers.com/webservice/post_list.php";
        final JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray result = null;
                            try {
                                result = response.getJSONArray("list");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(Objects.requireNonNull(result).length() == 0){
                                Toast.makeText(getActivity(), "No post found", Toast.LENGTH_SHORT).show();
                            }else{
                                for (int i = 0; i < result.length(); i++) {
                                    JSONObject jsonObject = result.getJSONObject(i);
                                    id = jsonObject.getString("id");
                                    title = jsonObject.getString("title");
                                    type = jsonObject.getString("type");
                                    location = jsonObject.getString("location");
                                    Post b = new Post(id,title,location,type);
                                    listPost.add(b);
                                }
                            }
                            if (getActivity()!=null) {
                                showLoading(false);
                                showPost(listPost);
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
                        showLoading(false);
                    }
                }
        );
        requestQueue.add(jor);
    }

    private void showPost(ArrayList<Post> list){
        rv_post.setLayoutManager(new LinearLayoutManager(getActivity()));
        CardPost cardPost = new CardPost(getActivity());
        cardPost.setListPost(list);
        rv_post.setAdapter(cardPost);
    }

    private void showLoading(Boolean state) {
        if (state) {
            pbPost.setVisibility(View.VISIBLE);
        } else {
            pbPost.setVisibility(View.GONE);
        }
    }
}
