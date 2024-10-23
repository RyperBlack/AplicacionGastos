package com.example.tfg_aplicaciongastos.ui.graph;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GraphViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GraphViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment Graficos");
    }

    public LiveData<String> getText() {
        return mText;
    }
}