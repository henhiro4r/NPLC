package com.uc.nplc.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uc.nplc.model.Quiz;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class QuizVIewModel extends ViewModel {
    private MutableLiveData<ArrayList<Quiz>> quizList = new MutableLiveData<>();

    public void setQuizList(String userId){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://7thnplc.wowrackcustomers.com/webservice/quiz_list.php";
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        final ArrayList<Quiz> listQuiz = new ArrayList<>();

        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray quizzes = responseObject.getJSONArray("quiz");
                    for (int i = 0; i < quizzes.length(); i++) {
                        JSONObject quiz = quizzes.getJSONObject(i);
                        Quiz q = new Quiz();
                        q.setId(quiz.getString("id"));
                        q.setTitle(quiz.getString("title"));
                        q.setQuestion(quiz.getString("question"));
                        q.setPrice(quiz.getString("price"));
                        q.setStatus(quiz.getString("status"));
                        listQuiz.add(q);
                    }
                    quizList.postValue(listQuiz);
                } catch (Exception e) {
                    Log.d("exceptionQuiz", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("OnFailureQuiz", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public LiveData<ArrayList<Quiz>> getQuiz() {
        return quizList;
    }
}
