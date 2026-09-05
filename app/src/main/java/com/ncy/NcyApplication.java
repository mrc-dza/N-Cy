package com.ncy;

import android.app.Application;

public class NcyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppServicios.init(this);
    }
}