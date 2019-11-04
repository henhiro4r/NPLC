package com.uc.nplc.card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.uc.nplc.R;
import com.uc.nplc.model.Bantuan;
import java.util.ArrayList;

public class CardBantuan extends RecyclerView.Adapter<CardBantuan.CardViewViewHolder>{

    private Context context;
    private ArrayList<Bantuan> listBantuan;

    private ArrayList<Bantuan> getListBantuan() {
        return listBantuan;
    }
    public void setListBantuan(ArrayList<Bantuan> listBantuan) {
        this.listBantuan = listBantuan;
    }
    public CardBantuan(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardBantuan.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bantuan, parent, false);
        return new CardBantuan.CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CardBantuan.CardViewViewHolder holder, int position) {
        Bantuan b = getListBantuan().get(position);
        holder.txt_judul.setText(b.getJudul());
        holder.txt_des.setText(b.getDes());
    }

    @Override
    public int getItemCount() {
        return getListBantuan().size();
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
