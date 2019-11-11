package com.uc.nplc.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uc.nplc.R;
import com.uc.nplc.card.CardPost;
import com.uc.nplc.model.Post;
import com.uc.nplc.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPost extends Fragment {


    public FragmentPost() {

    }

    private ProgressBar pbPost;
    private CardPost cardPost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_post, container, false);
        pbPost = v.findViewById(R.id.pb_post);
        RecyclerView rv_post = v.findViewById(R.id.rv_fr_bantuan);
        showLoading(true);
        cardPost = new CardPost(getActivity());
        cardPost.notifyDataSetChanged();

        PostViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PostViewModel.class);
        viewModel.setListPost();
        viewModel.getPost().observe(getActivity(), loadPost);

        rv_post.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_post.setAdapter(cardPost);
        return v;
    }

    private Observer<ArrayList<Post>> loadPost = new Observer<ArrayList<Post>>() {
        @Override
        public void onChanged(ArrayList<Post> posts) {
            if  (posts != null) {
                cardPost.setListPost(posts);
                showLoading(false);
            } else {
                showMessage();
            }
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            pbPost.setVisibility(View.VISIBLE);
        } else {
            pbPost.setVisibility(View.GONE);
        }
    }

    private void showMessage() {
        Toast.makeText(getActivity(), "No record found!", Toast.LENGTH_SHORT).show();
    }
}
