package com.uc.nplc.card;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.uc.nplc.R;
import com.uc.nplc.model.Post;
import java.util.ArrayList;

public class CardPost extends RecyclerView.Adapter<CardPost.CardViewViewHolder>{

    private Context context;
    private ArrayList<Post> listPost;

    private ArrayList<Post> getListPost() {
        return listPost;
    }
    public void setListPost(ArrayList<Post> listPost) {
        this.listPost = listPost;
    }
    public CardPost(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardPost.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false);
        return new CardPost.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CardPost.CardViewViewHolder holder, int position) {
        Post b = getListPost().get(position);
        if(Integer.parseInt(b.getId()) < 10){
            String post = "0"+b.getId();
            holder.id.setText(post);
        }else{
            holder.id.setText(b.getId());
        }
        holder.title.setText(b.getTitle());
        holder.location.setText(b.getLocation());
        holder.type.setText(b.getType());
    }

    @Override
    public int getItemCount() {
        return getListPost().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView id, title, location, type;

        CardViewViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.post_id);
            title = itemView.findViewById(R.id.txt_title_post);
            location = itemView.findViewById(R.id.txt_location);
            type = itemView.findViewById(R.id.type);

        }
    }

}
