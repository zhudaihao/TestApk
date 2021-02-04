package com.gemini.cloud.app.myapplication;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {
    //初始化Alarm
    private AlarmManager am;
    private PendingIntent pi;
    private long time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void load(View view) {
//        finish();
//        initAlarm();//初始化闹钟
//        setAlarm();//启动闹钟定时器

        startActivity(new Intent(this,Test2Activity.class));
    }


    private void initAlarm() {
        pi = PendingIntent.getBroadcast(this, 0, getMsgIntent(), 0);
        time = System.currentTimeMillis();
        am = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    private Intent getMsgIntent() {
        //AlarmReceiver 为广播在下面代码中
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.BC_ACTION);
        intent.putExtra("msg", "闹钟开启");
        return intent;
    }

    //设置定时执行的任务
    private void setAlarm() {
        //android Api的改变不同版本中设 置有所不同
        if (Build.VERSION.SDK_INT < 19) {
            am.set(AlarmManager.RTC_WAKEUP, getTimeDiff(), pi);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, getTimeDiff(), pi);
        }

    }

    public long getTimeDiff() {
        //这里设置的是当天的15：55分，注意精确到秒，时间可以自由设置
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 15);
        ca.set(Calendar.MINUTE, 55);
        ca.set(Calendar.SECOND, 0);
        return ca.getTimeInMillis();

    }

    //取消定时任务的执行
    private void cancelAlarm() {
        am.cancel(pi);
    }


}
