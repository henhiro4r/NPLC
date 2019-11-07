package com.uc.nplc.card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

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
        String post, point;
        if(Integer.parseInt(h.getGame_id()) < 10){
            post = "POST 0"+h.getGame_id();
            holder.txt_pos.setText(post);
        }else{
            post = "POST "+h.getGame_id();
            holder.txt_pos.setText(post);
        }

        if(h.getStatus().equalsIgnoreCase("w")){
            holder.txt_wdl.setText(R.string.win);
            holder.txt_wdl.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
            holder.cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
            point = "+"+ h.getPoint() +"pts";
            holder.txt_pts.setText(point);
        }else if(h.getStatus().equalsIgnoreCase("d")){
            holder.txt_wdl.setText(R.string.draw);
            holder.txt_wdl.setTextColor(ContextCompat.getColor(context, R.color.colorTosca));
            holder.cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorTosca));
            point = "+"+ h.getPoint() +"pts";
            holder.txt_pts.setText(point);
        }else if(h.getStatus().equalsIgnoreCase("l")){
            holder.txt_wdl.setText(R.string.lose);
            holder.txt_wdl.setTextColor(ContextCompat.getColor(context, R.color.colorMerah));
            holder.cv.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorMerah));
            point = "+"+ h.getPoint() +"pts";
            holder.txt_pts.setText(point);
        }

        holder.txt_time.setText(h.getTime_start());

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
