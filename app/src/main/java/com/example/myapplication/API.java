package com.example.myapplication;

import com.example.myapplication.dto.ImageResponse;
import com.example.myapplication.dto.OcrData;
import com.example.myapplication.dto.Summarize;
import com.example.myapplication.dto.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface API {
    // 로그인
    @POST("/insert")
    Call<String> getSignUpResponse(@Body User user);
    @POST("/login")
    Call<Integer> getLoginResponse(@Body User user);
    @POST("/search")
    Call<User> getSearchResponse(@Body User user);
    @Multipart
    @POST("/shot")
    Call<String> getCameraResponse(
            @Part MultipartBody.Part image,
            @Part("id") RequestBody id
            );
    @POST("/ocr")
    Call<OcrData> getOcrResponse(@Body Map<String, String> requestBody);

    @POST("/summarize")
    Call<Summarize> sendOcrData(@Body OcrData data);




}
