package com.test.testapp;

import com.test.testapp.logger.LoggerUtils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * Description:
 * Created by zhangjianliang on 2019/12/17
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LoggerUtils.init();
    }
}
