package net.diyigemt.meabuttontest.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import net.diyigemt.meabuttontest.entity.DownLoader;
import net.diyigemt.meabuttontest.entity.VoiceDescription;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class DownloadService extends Service {

    public static final String ACTION = "TARGET";
    public static final String PATH = "PATH";
    public static final String MD5 = "MD5";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int targetSound = intent.getExtras().getInt(ACTION);
        String path = intent.getExtras().getString(PATH);
        String md5 = intent.getExtras().getString(MD5);
        VoiceDescription voiceDescription = VoiceHelper.get(targetSound);
        DownLoader downLoader = new DownLoader(voiceDescription.getDownloadURL(), "", voiceDescription.getVoiceName());
        downLoader.setCb(() -> {
            File file = new File(path);
            if(MD5Utils.FLAG){//需要系统MD5算法支持
                String s  = MD5Utils.getFileMD5(file);
                if(!s.equals(md5)){
                    file.delete();
                }
                stopService(intent);
            }else {//无MD5算法支持，使用传统模式
                stopService(intent);
            }
        });
        downLoader.start();

        return super.onStartCommand(intent, flags, startId);
    }
}
