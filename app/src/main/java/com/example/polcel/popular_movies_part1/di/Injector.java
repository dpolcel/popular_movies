package com.example.polcel.popular_movies_part1.di;

import com.example.polcel.popular_movies_part1.BuildConfig;
import com.example.polcel.popular_movies_part1.services.MoviesService;
import com.example.polcel.popular_movies_part1.utilities.NetworkUtils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by polcel on 17/02/2018.
 */

public class Injector {

    public static Interceptor provideLoggingCapableHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        return logging;
    }

    public static Interceptor provideDefaultParameterInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key", BuildConfig.MOVIES_DB_API_KEY).build();

                request = request.newBuilder().url(url).build();

                return chain.proceed(request);
            }
        };
    }


    public static Retrofit provideRetroFit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(provideDefaultParameterInterceptor())
                .addInterceptor(provideLoggingCapableHttpClient())
                .build();

        return new Retrofit.Builder()
                .baseUrl(NetworkUtils.MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static MoviesService provideMoviesService() {
        return provideRetroFit().create(MoviesService.class);
    }
}
