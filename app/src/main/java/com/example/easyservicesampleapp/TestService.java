package com.example.easyservicesampleapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class TestService extends Service {
    private MediaPlayer mediaPlayer;
    private int counter;
    private int requestCode;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this, R.raw.menuettm);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        requestCode = intent.getIntExtra("REQUEST_CODE",0);
        Context context = getApplication();
        String ChannelID = "default";
        String title = context.getString(R.string.app_name);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel notificationChannel =
                new NotificationChannel(ChannelID, title, NotificationManager.IMPORTANCE_DEFAULT);

        if (notificationManager != null){
            notificationManager.createNotificationChannel(notificationChannel);

            Notification notification = new Notification.Builder(context, ChannelID)
                    .setContentTitle(title)
                    .setSmallIcon(android.R.drawable.ic_media_play)
                    .setContentText("MediaPlay")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .build();
            startForeground(1, notification);

            audioStart();
        }


        return START_NOT_STICKY;

    }

    private void audioStart(){
        counter++;

        if (mediaPlayer != null){
            mediaPlayer.setLooping(true);

            mediaPlayer.start();

            String str = "Start Walking\n(c)Music-Note.jp";
            Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
            toast.show();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    audioStop();

                    stopSelf();
                }
            });

        }
    }

    private void audioStop(){
        mediaPlayer.stop();

        mediaPlayer.reset();

        mediaPlayer.release();

        mediaPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null){
            audioStop();
        }

        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
