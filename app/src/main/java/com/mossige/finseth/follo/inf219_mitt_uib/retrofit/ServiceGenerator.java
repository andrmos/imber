package com.mossige.finseth.follo.inf219_mitt_uib.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mossige.finseth.follo.inf219_mitt_uib.R;

import java.io.IOException;

import hirondelle.date4j.DateTime;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by andre on 29.12.16.
 */

public class ServiceGenerator {
    public static final String API_BASE_URL = "https://mitt.uib.no/api/v1/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(gsonConverterFactory());

    public static <S> S createService(Class<S> serviceClass, final Context context) {

        // Add access_token to every request
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                String accessToken = "Bearer " + getAccessToken(context);
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", accessToken)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, final String token) {

        // Add access_token to every request
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                String accessToken = "Bearer " + token;
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", accessToken)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    private static String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getString("access_token", "");
    }

    private static GsonConverterFactory gsonConverterFactory() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Add custom deserializer
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeDeserializer());
        Gson myGson = gsonBuilder.create();

        return GsonConverterFactory.create(myGson);
    }
}
