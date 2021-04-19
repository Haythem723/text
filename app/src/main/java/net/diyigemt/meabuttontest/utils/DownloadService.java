package net.diyigemt.meabuttontest.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import net.diyigemt.meabuttontest.MeaSounds;
import net.diyigemt.meabuttontest.entity.DownLoader;
import net.diyigemt.meabuttontest.entity.VoiceDescription;

import java.io.File;

public class DownloadService extends Service {

    public static final String ACTION = "TARGET";
    public static final String SINGAL = "COMPLETE";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int targetSound = intent.getExtras().getInt(ACTION);
        VoiceDescription voiceDescription = VoiceHelper.get(targetSound);
        DownLoader downLoader = new DownLoader(voiceDescription.getDownloadURL(), "", voiceDescription.getVoiceName());
        downLoader.setCb(() -> {
            intent.putExtra(SINGAL, targetSound);
            stopService(intent);
        });
        downLoader.start();

        return super.onStartCommand(intent, flags, startId);
    }
}
