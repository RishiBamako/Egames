package com.rginfotech.egames.api;

import com.rginfotech.egames.model.UserLogin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {
    @FormUrlEncoded
    @POST("login")
    Call<UserLogin> loginUser(@Field("email") String email,@Field("password") String password);

    @FormUrlEncoded
    @POST("signup")
    Call<UserLogin> userSignUp(@Field("name") String name,@Field("email") String email,@Field("password") String password,@Field("country_code") String country_code,@Field("phone") String phone,@Field("gender") String gender);

}
