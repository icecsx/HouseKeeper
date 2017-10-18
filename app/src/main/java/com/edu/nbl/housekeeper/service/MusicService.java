package com.edu.nbl.housekeeper.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by 世贤 on 2017/8/2.
 */

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //1.实例化mediaPlayer
        mediaPlayer = new MediaPlayer();
        //2.打开assets
        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd("mo.mp3");
            //getStartOffset()音乐文件的偏移量默认是从0开始  getLength()获取音乐文件的长度
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),fileDescriptor.getLength());
            mediaPlayer.prepare();//准备音乐
            mediaPlayer.start();//启动音乐

        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();//停止音乐
        super.onDestroy();
    }


}