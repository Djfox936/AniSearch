package com.kiwitomatostudio.anisearch.ui.result;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kiwitomatostudio.anisearch.api.ApiService;
import com.kiwitomatostudio.anisearch.api.dto.AnimeResult;
import com.kiwitomatostudio.anisearch.api.dto.SearchImageResponse;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ResultActivityViewModel extends ViewModel {
    private ApiService m_apiService;

    public ResultActivityViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.trace.moe/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        m_apiService = retrofit.create(ApiService.class);
    }

    private final MutableLiveData<Boolean> m_showLoading = new MutableLiveData<>();
    public LiveData<Boolean> showLoading = m_showLoading;

    public void onShowLoading(boolean yes) {
        m_showLoading.postValue(yes);
    }

    private MutableLiveData<ArrayList<AnimeResult>> m_result = new MutableLiveData<>();
    public LiveData<ArrayList<AnimeResult>> result = m_result;

    public void requestMatchResult(String path) {
        String imageFilePath = path;
        MediaType mediaType = MediaType.parse("image/jpeg");
        RequestBody requestBody = RequestBody.create(mediaType, new File(imageFilePath));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "demo.jpg", requestBody);
        Call<SearchImageResponse> call = m_apiService.search(imagePart);
        call.enqueue(new Callback<SearchImageResponse>() {
            @Override
            public void onResponse(Call<SearchImageResponse> call, Response<SearchImageResponse> response) {
                SearchImageResponse searchImageResponse = response.body();
                if (searchImageResponse == null) {
                    return;
                }
                ArrayList<AnimeResult> results = searchImageResponse.result;
                if (results == null) {
                    return;
                }
                m_result.postValue(results);
                m_showLoading.postValue(false);
                Log.d("ResultActivityViewModel", "onResponse: " + results);
            }

            @Override
            public void onFailure(Call<SearchImageResponse> call, Throwable t) {
                Log.d("ResultActivityViewModel", "onFailure: " + t.getMessage());
                m_showLoading.postValue(false);
            }
        });
    }
}

