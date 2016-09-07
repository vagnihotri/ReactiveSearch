package com.rxjava.frpsearch.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rxjava.frpsearch.model.Issues;


import java.util.List;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public class NetworkService {
    public static APIInterface API;
    public static RestAdapter adapter;
    public static Gson gson;

    public static String LOG_TAG = "NETWORK CALL LOG";

    public void initialize(String baseUrl) {
        API = initialize(baseUrl, APIInterface.class);
    }

    protected static <T> T initialize(String baseUrl, Class<T> restInterface) {
        if(adapter == null) {
            gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();
            adapter = new RestAdapter.Builder().setEndpoint(baseUrl)
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog(LOG_TAG))
                    .build();
        }
        return adapter.create(restInterface);

    }

    public interface APIInterface {
        @GET("/repos/{user}/{repo}/issues")
        Observable<List<Issues>> getIssues(@Path("user") String user, @Path("repo") String repo);
    }
}
