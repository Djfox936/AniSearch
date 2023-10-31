package com.kiwitomatostudio.anisearch.api;

import com.kiwitomatostudio.anisearch.api.dto.SearchImageResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("search?anilistInfo")
    Call<SearchImageResponse> search(@Part MultipartBody.Part image);
}
