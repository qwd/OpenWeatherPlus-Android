package com.heweather.owp;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.heweather.owp.crash.CrashHandler;
import com.heweather.owp.utils.ContentUtil;

import org.apache.log4j.Level;

import java.io.File;
import java.util.logging.Logger;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;


public class MyApplication extends Application {
    //获取屏幕的高，宽
    private static MyApplication instance = null;

    public Logger log;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //在主线程中new的handler就是主线程的handler
        //初始化Handler
        HeConfig.init(ContentUtil.APK_USERNAME, ContentUtil.APK_KEY);
        HeConfig.switchToFreeServerNode();
        configLog();

    }

    public void configLog() {
        try {
            final LogConfigurator logConfigurator = new LogConfigurator();

            logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + "crifanli_log4j.log");
            // Set the root log level
            logConfigurator.setRootLevel(Level.DEBUG);
            // Set log level of a specific logger
            logConfigurator.setLevel("org.apache", Level.ERROR);
            logConfigurator.configure();
            CrashHandler catchHandler = CrashHandler.getInstance();
            catchHandler.init(getApplicationContext());
        } catch (Exception e) {
            String TAG = "sky";
            Log.i(TAG, "configLog: " + e);
        }

        //gLogger = Logger.getLogger(this.getClass());
        log = Logger.getLogger("CrifanLiLog4jTest");
    }
    /**
     * 获得实例
     *
     * @return
     */
    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 获取context对象
     */
    public static Context getContext() {
        return instance.getApplicationContext();
    }


}
