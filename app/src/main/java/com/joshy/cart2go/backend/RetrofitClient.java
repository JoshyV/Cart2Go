package com.joshy.cart2go.backend;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://c2g.dev/";
    private static Retrofit retrofit;

    public static Retrofit getClient(String token) {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new AuthInterceptor(token));

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    public static class AuthInterceptor implements okhttp3.Interceptor {
        private String token;

        public AuthInterceptor(String token) {
            this.token = token;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", " " + token)
                    .header("Content-Type", "multipart/form-data");
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    }
}