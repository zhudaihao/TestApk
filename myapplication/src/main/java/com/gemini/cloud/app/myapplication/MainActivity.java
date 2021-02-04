package com.gemini.cloud.app.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startService(new Intent(this,KeepLifeService.class));


        startService(new Intent(this,CheckService.class));


    }

    private static final String TAG = "App--------";
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销广播接受者
        unregisterReceiver(myBroadcastReceiver);


    }


    private MyBroadcastReceiver myBroadcastReceiver;
    public void load(View view) {

        startActivity(new Intent(this, TestActivity.class));

//
//        //动态创建个广播
//        //创建个意图管理器 添加个活动 （这个活动是需要接受的活动 所以需要和发送的活动一样）
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("alarm");
//        myBroadcastReceiver = new MyBroadcastReceiver();
//        //动态注册广播
//        registerReceiver(myBroadcastReceiver, filter);
//
//
//
//        //获取闹钟管理员
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//
//        //创建个意图 添加个活动
//        Intent intent = new Intent();
//        intent.setAction("alarm");//添加个活动
//
//        //创建个延迟意图（上下文，意图的请求码（区分其他延迟意图），intent,标记）；
//        /**
//         * PendingIntent.getBroadcast第四个参数flags
//         * (1) android.app.PendingIntent.FLAG_UPDATE_CURRENT
//         *
//         * 如果PendingIntent已经存在，保留它并且只替换它的extra数据。
//         *
//         * (2) android.app.PendingIntent.FLAG_CANCEL_CURRENT
//         *
//         * (3) android.app.PendingIntent.FLAG_ONE_SHOT
//         *
//         * PendingIntent只能使用一次。调用了实例方法send()之后，它会被自动cancel掉，再次调用send()方法将失败。
//         *
//         * (4) android.app.PendingIntent.FLAG_NO_CREATE
//         *
//         * 如果PendingIntent不存在，简单了当返回null。
//         */
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//        //创建个闹钟
//        /**
//         * 参数1 闹钟类型
//         * 闹钟的类型，常用的有5个值：
//         * AlarmManager.ELAPSED_REALTIME、手机休眠时不启动闹钟 时间 可以使用相对时间
//         * AlarmManager.ELAPSED_REALTIME_WAKEUP、手机休眠时启动闹钟 时间 可以使用相对时间
//         * AlarmManager.RTC、手机休眠不启动闹钟 时间取系统当前时间
//         * AlarmManager.RTC_WAKEUP、手机休眠也启动闹钟 时间取系统当前时间
//         * AlarmManager.POWER_OFF_WAKEUP。 手机关闭了也启动闹钟
//         */
//
//        /**
//         * 参数2 开启的时间 单位毫秒
//         */
//        /**
//         * 参数3 重复的时间间隔 单位毫秒
//         */
//
//        /**
//         * 参数4 pendingIntent 延迟意图
//         */
//
//        /**
//         * 注意
//         * 类型为  相对时间 参数2使用 SystemClock.elapsedRealtime()
//         * 绝对时间 参数2使用 System.currentTimeMillis()
//         */
//        Log.e("zdh","-----------"+SystemClock.elapsedRealtime()+"------>>"+System.currentTimeMillis());
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 500, pendingIntent);

    }



    public void load2(View view) {

//        finish();


        alarmTime=alarmTime*1000;//换成毫秒

        Intent intent = new Intent(MainActivity.this, AlarmService.class);
        //Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("alarmKey",alarmKey);

        //PendingIntent还可以用getActivity()直接启动Activity
        PendingIntent sender = PendingIntent.getService(MainActivity.this, alarmKey, intent, 0);
        //PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, alarmKey, intent, 0);

        long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
        firstTime += alarmTime;

        // 进行闹铃注册
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 500, sender);//重复闹钟
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
    }

    int alarmKey, alarmTime;
}
