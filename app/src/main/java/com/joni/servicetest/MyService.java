package com.joni.servicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
    
    private String TAG = "haha_myservice";
    public int progress = 0;
    
    public MyService() {
    }

    private DownloadBinder mBinder = new DownloadBinder();

    class DownloadBinder extends Binder {

        public void startDownload() {
            Log.d(TAG, "startDownload executed");
            synchronized ((Object) progress){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i=0; i<100; i++) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Log.d(TAG, "run: error");
                            }
                            progress++;
                        }
                    }
                }).start();
            }
        }

        public int getProgress() {
            Log.d(TAG, "Progress:"+progress);
            return progress;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate!");
        //前台服务代码
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .build();
        startForeground(1, notification);
    }

    //启动服务时会被调用
    //执行服务的逻辑
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
