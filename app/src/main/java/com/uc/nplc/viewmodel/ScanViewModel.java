package com.uc.nplc.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScanViewModel extends ViewModel {
    private MutableLiveData<Boolean> isDone = new MutableLiveData<>();

    public void play(){

    }

    public LiveData<Boolean> getIsDone() {
        return isDone;
    }
}
