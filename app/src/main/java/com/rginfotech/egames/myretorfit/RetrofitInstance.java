package com.rginfotech.egames.myretorfit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rginfotech.egames.BuildConfig;
import com.rginfotech.egames.api.API;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;

    public static final HttpLoggingInterceptor.Level HTTPLogLevel = BuildConfig.DEBUG? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;

    /**
     * Create an instance of Retrofit object
     * */
    public static Retrofit getRetrofitInstance() {
        // .addInterceptor(new BasicAuthInterceptor(MyApplication.tinyDB.getString(Constants.SP_USER_TOKEN)))
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HTTPLogLevel);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
