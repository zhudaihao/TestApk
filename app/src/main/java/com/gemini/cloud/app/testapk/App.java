package com.gemini.cloud.app.testapk;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;

import com.tbruyelle.rxpermissions3.RxPermissions;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class App extends Application {

    boolean isService = false;//false前台 true后台
    private String tagActivityName;
    private static final String TAG = "App--------";


    private int actCount = 0;

    /**
     * 判断应用是否在前台
     *
     * @return
     */
    public boolean isAppForeground() {
        return actCount > 0;
    }


    @Override
    public void onCreate() {
        // 程序创建的时候执行
        Log.e(TAG, "第一次启动app onCreate");
        super.onCreate();


    }



    private void regiest() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                Log.e(TAG, "--------------onActivityCreated");
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                actCount++;


                if (isAppForeground()){
                    Log.e(TAG, "--------------onActivityStarted >> 后台切换到 前台");
                }


//                if (isService && tagActivityName.equals(activity.getLocalClassName())) {
//                    Log.e(TAG, "--------------onActivityStarted >> 后台切换到 前台");
//                }
//
//                isService = isApplicationBroughtToBackground(getApplicationContext());
//                tagActivityName = activity.getLocalClassName();

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                actCount--;

                if (!isAppForeground()){
                    Log.e(TAG, "--------------onActivityStopped >> 前台切换到 后台");
                }






//                if (!isService && tagActivityName.equals(activity.getLocalClassName())) {
//                    Log.e(TAG, "--------------onActivityStopped >> 前台切换到 后台");
//                }
//
//                isService = isApplicationBroughtToBackground(getApplicationContext());
//                Log.e(TAG, "--------------onActivityStopped---isTag " + isService);


            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }


    /**
     * 判断当前应用程序处于前台还是后台 true是后台 , false是前台
     */
    public  boolean isApplicationBroughtToBackground(final Context context) {
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