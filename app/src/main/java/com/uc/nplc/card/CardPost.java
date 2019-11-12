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
    private ArrayList<Post> listPost = new ArrayList<>();

    public CardPost(Context context) {
        this.context = context;
    }

    public void setListPost(ArrayList<Post> list) {
        listPost.clear();
        listPost.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardPost.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_post, parent, false);
        return new CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CardPost.CardViewViewHolder holder, int position) {
        Post b = listPost.get(position);
        holder.title.setText(b.getTitle());
        holder.location.setText(b.getLocation());
        holder.type.setText(b.getType());
    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView title, location, type;

        CardViewViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title_post);
            location = itemView.findViewById(R.id.txt_location);
            type = itemView.findViewById(R.id.type);

        }
    }

}
