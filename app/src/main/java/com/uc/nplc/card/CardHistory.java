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
import com.uc.nplc.model.History;

import java.util.ArrayList;

public class CardHistory extends RecyclerView.Adapter<CardHistory.CardViewViewHolder>{

    private Context context;
    private ArrayList<History> listHistory;

    private ArrayList<History> getListHistory() {
        return listHistory;
    }
    public void setListHistory(ArrayList<History> listHistory) {
        this.listHistory = listHistory;
    }
    public CardHistory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history, parent, false);
        return new CardViewViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CardHistory.CardViewViewHolder holder, int position) {
        History h = getListHistory().get(position);
        if(Integer.parseInt(h.getIdPos()) < 10){
            holder.txt_pos.setText("POS 0"+h.getIdPos());
        }else{
            holder.txt_pos.setText("POS "+h.getIdPos());
        }

        if(h.gethMy().equalsIgnoreCase("w")){
            holder.txt_wdl.setText("WIN");
            holder.txt_wdl.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.txt_pts.setText("+100pts");
        }else if(h.gethMy().equalsIgnoreCase("d")){
            holder.txt_wdl.setText("DRAW");
            holder.txt_wdl.setTextColor(ContextCompat.getColor(context, R.color.colorTosca));
            holder.cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorTosca));
            holder.txt_pts.setText("+50pts");
        }else if(h.gethMy().equalsIgnoreCase("l")){
            holder.txt_wdl.setText("LOSE");
            holder.txt_wdl.setTextColor(ContextCompat.getColor(context, R.color.colorMerah));
            holder.cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorMerah));
            holder.txt_pts.setText("+25pts");
        }

        holder.txt_time.setText(h.getCreated());

    }

    @Override
    public int getItemCount() {
        return getListHistory().size();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        //CardView cvImgHis;
        TextView txt_pos;
        TextView txt_wdl;
        TextView txt_pts;
        TextView txt_time;
        CardView cv;

        CardViewViewHolder(View itemView) {
            super(itemView);
            //cvImgHis = itemView.findViewById(R.id.cv_img_history);
            txt_pos = itemView.findViewById(R.id.txt_pos_card_history);
            txt_wdl = itemView.findViewById(R.id.txt_wdl_card_history);
            txt_pts = itemView.findViewById(R.id.txt_pts_card_history);
            txt_time = itemView.findViewById(R.id.txt_time_card_history);
            cv = itemView.findViewById(R.id.cv_card_history);

        }
    }

}
