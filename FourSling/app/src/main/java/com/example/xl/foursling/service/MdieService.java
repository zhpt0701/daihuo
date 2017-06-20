package com.example.xl.foursling.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.xl.foursling.tools.CharConstants;
import com.example.xl.foursling.tools.Constants;
import com.example.xl.foursling.tools.SharePreferencesUlits;
import com.example.xl.foursling.tools.VibratorUtil;

import java.io.File;
import java.io.IOException;

import static com.example.xl.foursling.R.id.btn_register_auth_code;

/**
 * Created by admin on 2017/1/12.
 */

public class MdieService extends Service{
    private MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (SharePreferencesUlits.getinteger(getApplication(),CharConstants.SHAKE,0) != Constants.ZERO){
            VibratorUtil.Vibrate(getApplication(),new long[]{1000,2000,1000,2000},false);
        }
        if (SharePreferencesUlits.getinteger(getApplication(), CharConstants.MEDIA,0) != Constants.ZERO){
            initMediaplayer();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer arg0) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                }
            });
            switch (SharePreferencesUlits.getinteger(getApplication(), CharConstants.MEDIA,0)){
                case 0:
                    break;
                case 5:
                    new TimeCount(5000,1000).start();
                    break;
                case 10:
                    new TimeCount(10000,1000).start();
                    break;
                case 15:
                    new TimeCount(15000,1000).start();
                    break;
            }
//            if (SharePreferencesUlits.getinteger(getApplication(),CharConstants.MEDIA,0)>0){
//                while (true){
//                    if (System.currentTimeMillis()-SharePreferencesUlits.getLong(getApplication(),CharConstants.TIME_GAP,0)>=3000){
//                        SharePreferencesUlits.saveLong(getApplication(),CharConstants.TIME_GAP,System.currentTimeMillis());
//
//                    }
//                }
//            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaplayer() {
        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量
        int maxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); // 设置为最大声音，可通过SeekBar更改音量大小
        AssetFileDescriptor fileDescriptor;
        try {
            fileDescriptor = getAssets().openFd("tishi.m4a");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    /**
     * 计时器循环播放
     */
    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        //计时过程显示
        @Override
        public void onTick(long l) {

        }
        //计时完毕操作
        @Override
        public void onFinish() {
            mediaPlayer.release();
            initMediaplayer();
        }
    }
}
