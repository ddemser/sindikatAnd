package com.example.sindikatzajedno.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public  class ServiceGenerator {




    /*private Gson gson = new GsonBuilder().setLenient()
            .rNNGzdBrXymtBL9WHZTXsLZrhihHoq9C9H(new NullStringToEmptyAdapterFactory())
            .registerTypeAdapter(Double.class, new DoubleAdapter())
            .create(); */


    Gson gson = new GsonBuilder()
            .setLenient()
            .create();



    private OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    public Retrofit retrofit = new Retrofit.Builder()
            //.baseUrl(getBaseUrl())

            //.baseUrl("https://sind-zajedno.times.hr/")
            .baseUrl("http://192.168.1.118:8443/")
            //.baseUrl("https://mh-erp.osmibit.hr/ords/ordshramina/mobile_radniNalozi/")
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

            Class<T> rawType = (Class<T>) type.getRawType();
            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringAdapter();
        }
    }

    public static class StringAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter out, String value) throws IOException {

        }

        public String read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

    }

    public static class DoubleAdapter extends TypeAdapter<Double> {

        @Override
        public void write(JsonWriter out, Double value) throws IOException {

        }

        @Override
        public Double read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return 0.00;
            }
            return reader.nextDouble();
        }
    }
}

