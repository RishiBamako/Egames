package com.rginfotech.egames.myretorfit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @Multipart
    @POST("my_profile_update")
    Call<UploadImageResponse> uploadImage(@Part("user_id") RequestBody userId, @Part MultipartBody.Part file);

}
