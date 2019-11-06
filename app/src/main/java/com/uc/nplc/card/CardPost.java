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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bantuan, parent, false);
        return new CardPost.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CardPost.CardViewViewHolder holder, int position) {
        Post b = getListPost().get(position);
        holder.txt_judul.setText(b.getJudul());
        holder.txt_des.setText(b.getDes());
    }

    @Override
    public int getItemCount() {
        return getListPost().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        TextView txt_judul;
        TextView txt_des;

        CardViewViewHolder(View itemView) {
            super(itemView);
            txt_judul = itemView.findViewById(R.id.txt_judul_card_bantuan);
            txt_des = itemView.findViewById(R.id.txt_des_card_bantuan);

        }
    }

}
