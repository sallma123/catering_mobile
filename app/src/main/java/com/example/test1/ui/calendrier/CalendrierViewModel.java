package com.example.test1.ui.calendrier;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalendrierViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public CalendrierViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is calendrier fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}