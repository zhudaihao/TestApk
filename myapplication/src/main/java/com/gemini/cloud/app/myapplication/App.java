package com.gemini.cloud.app.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class App extends Application {

    boolean isTag=false;//false前台 true后台
    private String tagName;
    private static final String TAG = "App--------";

    /**
     * 2020-12-09 12:35:08.262 21638-21638/com.gemini.cloud.app.myapplication E/App--------: --------------onActivityCreated
     *
     * 2020-12-09 12:35:08.371 21638-21638/com.gemini.cloud.app.myapplication E/App--------: --------------onActivityStarted
     * 2020-12-09 12:35:08.373 21638-21638/com.gemini.cloud.app.myapplication E/App--------: --------------onActivityResumed
     */

    /**
     * 2020-12-09 12:35:28.895 21638-21638/com.gemini.cloud.app.myapplication E/App--------: --------------onActivityPaused
     * 2020-12-09 12:35:28.944 21638-21638/com.gemini.cloud.app.myapplication E/App--------: --------------onActivityStopped
     * 2020-12-09 12:35:28.946 21638-21638/com.gemini.cloud.app.myapplication E/App--------: --------------onActivitySaveInstanceState
     */

    /**
     * 2020-12-09 12:35:59.135 21638-21638/com.gemini.cloud.app.myapplication E/App--------: --------------onActivityStarted
     * 2020-12-09 12:35:59.139 21638-21638/com.gemini.cloud.app.myapplication E/App--------: --------------onActivityResumed
     *
     */



    @Override
    public void onCreate() {
        // 程序创建的时候执行
        Log.e(TAG, "第一次启动app onCreate");
        super.onCreate();



        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

                Log.e(TAG,"--------------onActivityCreated");
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                if (isTag&&tagName.equals(activity.getLocalClassName())){
                    Log.e(TAG,"--------------onActivityStarted >> 后台切换到 前台");
                }

                isTag= isApplicationBroughtToBackground(getApplicationContext());
                tagName=activity.getLocalClassName();
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.e(TAG,"--------------onActivityResumed");
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.e(TAG,"--------------onActivityPaused");
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                if (!isTag&&tagName.equals(activity.getLocalClassName())){
                    Log.e(TAG,"--------------onActivityStarted >> 前台切换到 后台");
                }

                isTag= isApplicationBroughtToBackground(getApplicationContext());


            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                Log.e(TAG,"--------------onActivitySaveInstanceState");
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.e(TAG,"--------------onActivityDestroyed");
            }
        });

    }



    /**
     *判断当前应用程序处于前台还是后台 true是后台 , false是前台
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }



    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.e(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }


}