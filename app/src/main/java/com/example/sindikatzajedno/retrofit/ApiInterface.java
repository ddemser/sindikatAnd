package com.example.sindikatzajedno.retrofit;

import com.example.sindikatzajedno.model.Sindikat_clanovi;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiInterface {


    @GET()
    Call<Sindikat_clanovi> getUsers(@Url String url);
    @Headers("Content-Type: application/json")
    @POST("/registerclanovi")
    Call<String> registerClan(@Body JsonObject jsonObject);

    @GET()
    Call<String> getLozinka(@Url String url);

}
