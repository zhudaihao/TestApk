package com.gemini.cloud.app.testapk;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


/**
 * todo 注意： 文件读取权限 ，把 girl.apk放到sd卡里面
 * 
 *
 */
public class MainActivity extends AppCompatActivity {
    //文件的总块数
    private volatile int chunckTotal;
    //查询到该文件开始的块位置
    private volatile int chunck = 0;
    //某块文件信息
    private volatile byte[] mBlock;
    //上传的文件
    private File file;
    //申请权限工具
    private HandlerThread handlerThread;
    private Handler handler;
    //线程池
    private static volatile ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlerThread = new HandlerThread("upload");
        handlerThread.start();
    }


    //获取线程池对象
    public ExecutorService getExecutorService() {
        if (executorService == null) {
            synchronized (executorService) {
                if (executorService == null) {
                    executorService = new ThreadPoolExecutor(8, 16, 60, TimeUnit.SECONDS,
                            new SynchronousQueue<Runnable>(), new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread thread = new Thread(r, "upload");
                            thread.setDaemon(false);
                            return thread;
                        }
                    });
                }
            }
        }

        return executorService;
    }

    public void load(View view) {
        //        //线程池
//        executorService=getExecutorService();
//
        init();
//        //添加线程到线程池
//        UploadThread uploadThread=new UploadThread();
//        executorService.execute(uploadThread);

    }

    //线程
    public class UploadThread extends Thread {
        @Override
        public synchronized void run() {


        }
    }

    //初始化handler
    private void init() {
        handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        if (chunck == 0) {
                            //第一次
                            chunck = 1;
                            getBlockFile();
                        } else {
                            //请求网络
                            Log.e("zdh", "---------------请求网络 第 " + chunck + "-------- 块的信息 " + mBlock.length);
                            try {
                                //模拟网络请求耗时
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //请求网络成功
                            Log.e("zdh", "---------------请求网络成功 第 " + chunck + "-------- 块的信息 " + mBlock.length);
                            chunck++;
                            //调用获取下块数据
                            if (chunck <= chunckTotal) {
                                getBlockFile();
                            }
                        }

                        break;

                    default:
                        break;
                }

                return false;
            }
        });

        //初始化
        Message message = handler.obtainMessage();
        message.what = 1;
        handler.sendMessage(message);
    }


    //获取某块数据
    private void getBlockFile() {

        setFile(true);
    }

    private void setFile(Boolean aBoolean) {
        Log.e("zdh", "-----------currentThread " + Thread.currentThread().getName());
        if (aBoolean) {
            //文件分块 一块2M
            file = new File(Environment.getExternalStorageDirectory(), "girl.apk");
            //获取文件大小
            long fileLength = file.length();
            Log.e("zdh", "-----------fileLength " + fileLength);
            //根据文件大小计算分块的总数
            if (fileLength % Global.BLOCKLENGTH == 0) {
                chunckTotal = (int) fileLength / Global.BLOCKLENGTH;
            } else {
                chunckTotal = (int) fileLength / Global.BLOCKLENGTH + 1;
            }

            //获取每块数据
            mBlock = FileUtils.getBlock(1 * Global.BLOCKLENGTH, file, Global.BLOCKLENGTH);
            if (mBlock != null) {
                //获取某块数据 --请求接口
                Message message = handler.obtainMessage();
                message.what = 1;
                handler.sendMessage(message);
                Log.e("zdh", "--------------- 第 " + chunck + "-------- 块的信息 " + mBlock.length);
            } else {
                Log.e("zdh", "--------------- 块的信息 空");
            }

        } else {
            Log.e("zdh", "--------------- 没有权限 ");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handlerThread.getLooper().quitSafely();
        }
    }
}
