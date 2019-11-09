package com.uc.nplc.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uc.nplc.R;
import com.uc.nplc.model.Quiz;

import java.util.ArrayList;

public class CardQuiz extends RecyclerView.Adapter<CardQuiz.QuizViewHolder> {

    private Context context;
    private ArrayList<Quiz> quizData = new ArrayList<>();

    public CardQuiz(Context context) {
        this.context = context;
    }

    public void setQuizData(ArrayList<Quiz> data) {
        quizData.clear();
        quizData.addAll(data);
        notifyDataSetChanged();
    }

    public ArrayList<Quiz> getQuizData() {
        return quizData;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz quiz = quizData.get(position);
        holder.quiz_price.setText(quiz.getPrice());
        holder.quiz_title.setText(quiz.getTitle());
        holder.quiz_question.setText(quiz.getQuestion());
        if (quiz.getStatus().equals("1")) {
            holder.quiz_status.setText(R.string.status_1);
        } else if (quiz.getStatus().equals("2")) {
            holder.quiz_status.setText(R.string.status_2);
        } else if (quiz.getStatus().equals("3")){
            holder.quiz_status.setText(R.string.status_3);
        } else {
            holder.quiz_status.setText(R.string.status_0);
        }
    }

    @Override
    public int getItemCount() {
        return quizData.size();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {

        TextView quiz_title, quiz_question, quiz_status, quiz_price;

        QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quiz_title = itemView.findViewById(R.id.txt_title_quiz);
            quiz_question = itemView.findViewById(R.id.txt_desc_quiz);
            quiz_status = itemView.findViewById(R.id.txt_quiz_status);
            quiz_price = itemView.findViewById(R.id.txt_quiz_price);
        }
    }
}
