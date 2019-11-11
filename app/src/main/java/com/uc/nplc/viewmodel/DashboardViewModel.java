package com.uc.nplc.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {
    private MutableLiveData<Boolean> isDone = new MutableLiveData<>();

    public void init(){

    }

    public void refresh(){

    }

    public LiveData<Boolean> getIsDone() {
        return isDone;
    }
}
