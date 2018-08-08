package com.example.usbreadwriterdaemon;

import android.app.Application;
import android.content.Context;

/**
 * Created by yuanpk on 2018/7/10  15:09
 * <p>
 * Description:TODO
 */

public class InitApplication extends Application {

    private static Application mApplication;
    private static Context sApplicationContext;
    private boolean isDebug = true;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;
        sApplicationContext = getApplicationContext();

    }


    public static Application getApplication() {
        return mApplication;
    }

    public static Context getContext() {
        return sApplicationContext;
    }


}
