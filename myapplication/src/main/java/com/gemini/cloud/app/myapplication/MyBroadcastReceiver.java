package com.gemini.cloud.app.myapplication;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * 创建个广播接受者  接收广播
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //判断接受到的广播意图里面的活动是不是 闹钟的意图
        if (TextUtils.equals(intent.getAction(),"alarm")){

            //处理需要 隔断时间就处理的事件
//            int background = isBackground(context, context.getPackageName());
//            Log.e("zdh","------------- "+background);

            boolean applicationBroughtToBackground = isApplicationBroughtToBackground(context);
            Log.e("zdh","-------------onReceive "+applicationBroughtToBackground);
        }


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



    public static int isBackground(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    //应用处于后台
                    return 1;
                }else{
                    //应用在前台展示着
                    return 2;
                }
            }
        }
        return 3;
    }
}
