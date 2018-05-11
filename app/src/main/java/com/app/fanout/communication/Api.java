package com.app.fanout.communication;

import com.app.fanout.model.ChatModel;
import com.app.fanout.model.MessagesResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static com.app.fanout.communication.Api.EndPoints.API_BASE_URL;

public class Api {

    private static Api instance;
    private final Retrofit retrofit;
    private EndPoints SERVICE;

    public static Api getInstance() {
        if (instance == null)
            instance = new Api();
        return instance;
    }

    private Api() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpClient())
                .build();

        SERVICE = retrofit.create(EndPoints.class);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public EndPoints getEndPoint() {
        return SERVICE;
    }

    private OkHttpClient getHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.readTimeout(30, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        clientBuilder.addInterceptor(logging);

        return clientBuilder.build();
    }

    public interface EndPoints {

        String API_BASE_URL = "http://chat.fanoutapp.com/";

        @FormUrlEncoded
        @POST("/rooms/{room-id}/messages/")
        Call<ChatModel> sendMessage(@Path("room-id") String roomId, @Field("from") String from, @Field("text") String text);

        @GET("/rooms/{room-id}/messages/")
        Call<MessagesResponse> getMessages(@Path("room-id") String roomId);
    }
}