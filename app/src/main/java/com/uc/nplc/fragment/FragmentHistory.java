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
import com.uc.nplc.card.CardHistory;
import com.uc.nplc.model.History;
import com.uc.nplc.preference.Pref;
import com.uc.nplc.viewmodel.HistoryViewModel;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistory extends Fragment {


    public FragmentHistory() {

    }

    private ProgressBar pbHistory;
    private CardHistory historyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.fragment_history, container, false);
        Pref pref = new Pref(getActivity());
        String userId = pref.getIdKey();
        RecyclerView rv_history = v.findViewById(R.id.rv_fr_history);
        pbHistory = v.findViewById(R.id.pb_history);
        showLoading(true);
        historyAdapter = new CardHistory(getActivity());
        historyAdapter.notifyDataSetChanged();

        HistoryViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(HistoryViewModel.class);
        viewModel.loadHistory(userId);
        viewModel.getHistory().observe(getActivity(), getHistory);

        rv_history.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_history.setAdapter(historyAdapter);
        return v;
    }

    private Observer<ArrayList<History>> getHistory = new Observer<ArrayList<History>>() {
        @Override
        public void onChanged(ArrayList<History> histories) {
            if  (histories != null){
                historyAdapter.setListHistory(histories);
                showLoading(false);
            } else {
                showMessage();
            }
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            pbHistory.setVisibility(View.VISIBLE);
        } else {
            pbHistory.setVisibility(View.GONE);
        }
    }

    private void showMessage() {
        Toast.makeText(getActivity(), "No record found!", Toast.LENGTH_SHORT).show();
    }
}