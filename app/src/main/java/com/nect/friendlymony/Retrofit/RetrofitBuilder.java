package com.nect.friendlymony.Retrofit;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nect.friendlymony.Utils.App;
import com.nect.friendlymony.Utils.AppUtils;
import com.nect.friendlymony.Utils.Constants;
import com.nect.friendlymony.Utils.HawkAppUtils;
import com.nect.friendlymony.Utils.Pref;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by nectabits on 1/16/2017.
 * Retrofit REST Client
 * <p>
 * you can use the builder to set some general options for all requests,
 * i.e. the base URL or the converter.
 */
@SuppressWarnings("unused")
public class RetrofitBuilder {

    private static RetrofitBuilder retrofitBuilder = new RetrofitBuilder();
    /* public ApiEndpointInterface getDummyData() {
         HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
         logging.setLevel(HttpLoggingInterceptor.Level.BODY);
         OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
         httpClient.connectTimeout(10, TimeUnit.MINUTES);
         httpClient.readTimeout(10, TimeUnit.MINUTES);
         httpClient.writeTimeout(10, TimeUnit.MINUTES);
         httpClient.addInterceptor(logging);
         httpClient.interceptors().add(headerAuthorizationInterceptor);

         *//*Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();*//*

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppUtils.DUMMY_PROFILE)
                .client(httpClient.build())
                //.addConverterFactory(JacksonConverterFactory.create())

                .build();
        return retrofit.create(ApiEndpointInterface.class);
    }*/
    private Interceptor headerAuthorizationInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            String accessToken = "";
            try {
                if (HawkAppUtils.getInstance().getUSERDATA() != null) {
                    accessToken = HawkAppUtils.getInstance().getUSERDATA().getToken();
                }

                if (Pref.getStringValue(App.getInstance(), Constants.CURRENT_BASE_URL, "1").equalsIgnoreCase("1")) {
                    Headers headers = request.headers().newBuilder()
                            .add("Content-Type", "application/x-www-form-urlencoded")
                            //.add("Content-Type", "application/x-www-form-urlencoded")
                            .build();
                    request = request.newBuilder().headers(headers).build();
                } else {
                    Headers headers = request.headers().newBuilder()
                            .add("Content-Type", "application/x-www-form-urlencoded")
                            //.add("Content-Type", "application/x-www-form-urlencoded")
                            .add("Authorization", TextUtils.isEmpty(accessToken) ? "" : accessToken)
                            .build();
                    request = request.newBuilder().headers(headers).build();
                }

                Log.e("FRIENDLYMONY APP LOG", "Authorization==" + accessToken);
            } catch (Exception e) {
                Headers headers = request.headers().newBuilder()
                        .add("Content-Type", "application/x-www-form-urlencoded")
                        //.add("Content-Type", "application/x-www-form-urlencoded")
                        .add("Authorization", TextUtils.isEmpty(accessToken) ? "" : accessToken)
                        .build();
                request = request.newBuilder().headers(headers).build();
            }

            return chain.proceed(request);
        }
    };

    private RetrofitBuilder() {

    }

    /**
     * Get instance id RetrofitBuilder
     *
     * @return retrofitBuilder
     */
    public static RetrofitBuilder getInstance() {
        return retrofitBuilder;
    }

    /**
     * Retrofit client with custom request setting.
     * i.e. request time out, json converter factory.
     *
     * @return retrofit client.
     */

    public ApiEndpointInterface getRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(20, TimeUnit.MINUTES);
        httpClient.readTimeout(20, TimeUnit.MINUTES);
        httpClient.writeTimeout(20, TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);
        httpClient.interceptors().add(headerAuthorizationInterceptor);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        String baseUrl = "";

        /*if (Pref.getStringValue(App.getInstance(), Constants.CURRENT_BASE_URL, "2").equalsIgnoreCase("1")) {
            baseUrl = AppUtils.APPLE_LOGIN_BASE_URL;
        } else {
            baseUrl = AppUtils.NEW_BASE_URL;
        }*/

        Log.e("FRIENDLYMONY APP LOG", "baseUrl==" + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppUtils.NEW_BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create(gson))

                //.addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(ApiEndpointInterface.class);
    }

    public ApiEndpointInterface getRetrofit2() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(20, TimeUnit.MINUTES);
        httpClient.readTimeout(20, TimeUnit.MINUTES);
        httpClient.writeTimeout(20, TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);
        httpClient.interceptors().add(headerAuthorizationInterceptor);

        String baseUrl = "";

        if (Pref.getStringValue(App.getInstance(), Constants.CURRENT_BASE_URL, "1").equalsIgnoreCase("1")) {
            baseUrl = AppUtils.OLD_BASE_URL;
        } else {
            baseUrl = AppUtils.NEW_BASE_URL;
        }
        Log.e("FRIENDLYMONY APP LOG", "baseUrl==" + baseUrl);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppUtils.NEW_BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(ApiEndpointInterface.class);
    }

  /*  public ApiEndpointInterface getRetrofitXMPP() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(10, TimeUnit.MINUTES);
        httpClient.readTimeout(10, TimeUnit.MINUTES);
        httpClient.writeTimeout(10, TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);
        httpClient.interceptors().add(headerAuthorizationInterceptor);

        *//*Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();*//*

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppUtils.BASE_URL_2)
                .client(httpClient.build())
                //.addConverterFactory(JacksonConverterFactory.create())

                .build();
        return retrofit.create(ApiEndpointInterface.class);
    }*/

    public ApiEndpointInterface getRetroFCM() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(20, TimeUnit.MINUTES);
        httpClient.readTimeout(20, TimeUnit.MINUTES);
        httpClient.writeTimeout(20, TimeUnit.MINUTES);
        httpClient.addInterceptor(logging);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppUtils.BASE_FCM_URL)
                .client(httpClient.build())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return retrofit.create(ApiEndpointInterface.class);
    }

}


