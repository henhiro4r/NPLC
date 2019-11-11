package com.uc.nplc.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uc.nplc.QuizActivity;
import com.uc.nplc.R;
import com.uc.nplc.ResultActivity;
import com.uc.nplc.card.CardQuiz;
import com.uc.nplc.model.Quiz;
import com.uc.nplc.preference.Pref;
import com.uc.nplc.utils.ItemClickSupport;
import com.uc.nplc.viewmodel.QuizViewModel;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPortal extends Fragment {


    public FragmentPortal() {

    }

    private ProgressDialog pd;
    private AlphaAnimation click = new AlphaAnimation(1F, 0.6F);

    private RecyclerView rvQuiz;
    private ProgressBar pbQuiz;
    private ArrayList<Quiz> quizz = new ArrayList<>();
    private CardQuiz cardQuiz;
    private Pref pref;
    private QuizViewModel vIewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_portal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new Pref(getActivity());
        pbQuiz = view.findViewById(R.id.pb_quiz);
        rvQuiz = view.findViewById(R.id.rv_quiz);
        pd = new ProgressDialog(getActivity());
        showLoading(true);
        cardQuiz = new CardQuiz(getActivity());
        cardQuiz.notifyDataSetChanged();

        vIewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(QuizViewModel.class);
        loadData();

        rvQuiz.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvQuiz.setAdapter(cardQuiz);
        clickSupport();
    }

    private void loadData(){
        vIewModel.setQuizList(pref.getIdKey());
        vIewModel.getQuiz().observe(Objects.requireNonNull(getActivity()), loadQuiz);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private Observer<ArrayList<Quiz>> loadQuiz = new Observer<ArrayList<Quiz>>() {
        @Override
        public void onChanged(ArrayList<Quiz> quizzes) {
            if (quizzes != null){
                quizz.addAll(quizzes);
                cardQuiz.setQuizData(quizzes);
                showLoading(false);
            }
        }
    };

    private void clickSupport() {
        ItemClickSupport.addTo(rvQuiz).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, final int position, View v) {
                v.startAnimation(click);
                int point = Integer.parseInt(pref.getPointKey());
                int status = Integer.parseInt(quizz.get(position).getStatus());
                if  (point >= 500 && status == 0){
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("\uD83D\uDCCC Confirmation");
                    alert.setMessage("Are you sure want to buy this quiz? Your point will reduced by 500 pts");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            pd.setCancelable(false);
                            pd.setTitle("Loading");
                            pd.setMessage("Processing, please wait...");
                            pd.show();
                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    buy(quizz.get(position));
                                }
                            };
                            Handler cancel = new Handler();
                            cancel.postDelayed(r, 2000);
                            dialogInterface.dismiss();
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                } else if (status == 1){
                    Intent intent = new Intent(getActivity(), QuizActivity.class);
                    intent.putExtra(QuizActivity.EXTRA_QUIZ, quizz.get(position));
                    startActivity(intent);
                } else  if (status == 3){
                    Intent intent = new Intent(getActivity(), ResultActivity.class);
                    intent.putExtra(ResultActivity.EXTRA_QUIZ, quizz.get(position));
                    startActivity(intent);
                } else if (status == 2) {
                    showMessage(getString(R.string.wrong_quiz));
                } else {
                    showMessage(getString(R.string.ne_point));
                }
            }
        });
    }

    private void buy(Quiz q){
        vIewModel.quizBuy(pref.getIdKey(), q);
        vIewModel.getIsDone().observe(Objects.requireNonNull(getActivity()), checkDone);
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra(QuizActivity.EXTRA_QUIZ, q);
        startActivity(intent);
    }

    private Observer<Boolean> checkDone = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean aBoolean) {
            if (aBoolean){
                pd.dismiss();
                showMessage("Good Luck");
            } else {
                pd.dismiss();
                showMessage("Something wrong, please try again!");
            }
        }
    };

    private void showMessage(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void showLoading(Boolean state) {
        if (state) {
            pbQuiz.setVisibility(View.VISIBLE);
        } else {
            pbQuiz.setVisibility(View.GONE);
        }
    }
}
