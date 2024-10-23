package com.example.tfg_aplicaciongastos.ui.habitualPayments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HabitualPaymentsViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public HabitualPaymentsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment Pagos Habituales");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
