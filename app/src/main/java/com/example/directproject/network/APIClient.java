package com.example.directproject.network;

import com.example.directproject.R;
import com.example.directproject.utils.SettingInfo;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.directproject.utils.SettingInfo.SERVER_IP;

/**
 *
 * @author YunJin Choi
 * @since 2019-12-27 18:53
 *
 */
public class APIClient {

    private static Retrofit retrofit = null;

    /**
     * @author YunJin Choi
     * @since 2019-12-27 18:53
     * @description 통신라이브러리인 Retrofit 사용 및 Gson 적용.
     * @return {Retrofit} retrofit
     */
    public static Retrofit getClient(){

        OkHttpClient client = new OkHttpClient.Builder().build();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();

        retrofit = new Retrofit.Builder()
                .baseUrl(SettingInfo.SERVER_IP)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .client(client)
                .build();

        return retrofit;
    }
}

