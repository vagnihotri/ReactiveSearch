package com.rxjava.frpsearch;

import android.app.Application;

import com.rxjava.frpsearch.network.NetworkService;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new NetworkService().initialize("https://api.github.com");
    }
}
