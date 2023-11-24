package com.kiwitomatostudio.anisearch.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<String> buttonText = new MutableLiveData<>();
    private MutableLiveData<String> selectedImageUri = new MutableLiveData<>();

    public MainActivityViewModel() {
        buttonText.setValue("選擇截圖");
    }

    public LiveData<String> getButtonText() {
        return buttonText;
    }

    public LiveData<String> getSelectedImageUri() {
        return selectedImageUri;
    }

    public void setImageSelected(String imageUri) {
        selectedImageUri.setValue(imageUri);
        buttonText.setValue("下一步");
    }

    public void resetImageSelection() {
        selectedImageUri.setValue(null);
        buttonText.setValue("選擇截圖");
    }
}
