package com.example.test1.ui.commandes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommandesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CommandesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is commandes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}