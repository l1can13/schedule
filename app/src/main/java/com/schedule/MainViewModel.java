package com.schedule;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    MediatorLiveData<String> mutableLiveDara = new MediatorLiveData<>();

    public void setText(String s) {
        mutableLiveDara.setValue(s);
    }

    public MutableLiveData<String> getText() {
        return mutableLiveDara;
    }
}
