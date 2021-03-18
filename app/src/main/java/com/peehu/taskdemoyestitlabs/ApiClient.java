package com.peehu.taskdemoyestitlabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient implements Serializable {
    public static final String BASE_URL = "https://api.androidhive.info";
    private static final String TAG = ApiClient.class.getSimpleName();
    private static ApiClient apiClient = null;
    private static final Object mLock = new Object();
    private static final boolean production = false;
    private static Retrofit retrofit = null;
    public Context context;

    public static boolean isProduction() {
        return false;
    }

    public ApiClient() {
    }

    public ApiClient(Context context2) {
        this.context = context2;
    }

    public static ApiClient getSingletonApiClient() {
        ApiClient apiClient2;
        synchronized (mLock) {
            if (apiClient == null) {
                apiClient = new ApiClient();
            }
            apiClient2 = apiClient;
        }
        return apiClient2;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60 * 5, TimeUnit.SECONDS)
                    .readTimeout(60 * 5, TimeUnit.SECONDS)
                    .addInterceptor(new LoggingInterceptor())
                    .writeTimeout(60 * 5, TimeUnit.SECONDS);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient.build())
                    .addConverterFactory(gsonConverterFactory)
                    .build();
        }
        return retrofit;
    }


    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String token;
            /*  AppConstants.TOKEN = Method.getPreferences(AppConstants.CONTEXT, "Authorization");*/
            Request original = chain.request();
           /* if (AppConstants.TOKEN != null && !AppConstants.TOKEN.equalsIgnoreCase("")) {
                token = Method.getPreferences(AppConstants.CONTEXT, "Authorization");
                // // token = "Bearer" + " " + Method.getPreferences(AppConstants.CONTEXT, "Authorization");
            } else {
                token = "";
            }*/
            Request request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build();

            long t1 = System.nanoTime();
          /*  String requestLog = String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers());
            if (request.method().compareToIgnoreCase("post") == 0) {
                requestLog = "\n" + requestLog + "\n" + bodyToString(request);
            }
            try {
                Log.d(TAG, "request" + "\n" + requestLog);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }*/

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            @SuppressLint("DefaultLocale") String responseLog = String.format("Final URL %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers());
            String bodyString = response.body().string();
            Log.d(TAG, "response" + "\n" + responseLog /*+ "\n" + bodyString*/);


            try {
                return response.newBuilder()
                        .body(ResponseBody.create(response.body().contentType(), bodyString))
                        .build();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage() + "---- XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            }
            return null;
        }

        public static String bodyToString(final Request request) {
           /* try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "did not work";
            }*/
            return null;
        }
    }

}