package com.example.myapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public static API api;

    static {
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // 연결 시간 초과
                .readTimeout(30, TimeUnit.SECONDS) // 읽기 시간 초과
                .writeTimeout(30, TimeUnit.SECONDS) // 쓰기 시간 초과
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.29.209.36:8080/") // 요청 보내는 API 서버 URL (끝에 /가 있어야 함)
                .addConverterFactory(GsonConverterFactory.create(gson)) // Gson을 사용하여 역직렬화
                .client(okHttpClient) // OkHttpClient 설정을 사용
                .build();

        api = retrofit.create(API.class);
    }
}