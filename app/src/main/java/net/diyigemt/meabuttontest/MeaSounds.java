package net.diyigemt.meabuttontest;


import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import net.diyigemt.meabuttontest.entity.DownLoader;
import net.diyigemt.meabuttontest.entity.VoiceDescription;
import net.diyigemt.meabuttontest.permission.IRequestPermissions;
import net.diyigemt.meabuttontest.permission.RequestPermissions;
import net.diyigemt.meabuttontest.requestresult.IRequestPermissionsResult;
import net.diyigemt.meabuttontest.requestresult.PermissionUtils;
import net.diyigemt.meabuttontest.requestresult.RequestPermissionsResultSetApp;
import net.diyigemt.meabuttontest.utils.ContextUtils;
import net.diyigemt.meabuttontest.utils.DownloadService;
import net.diyigemt.meabuttontest.utils.FileUtils;
import net.diyigemt.meabuttontest.utils.HttpDownLoader;
import net.diyigemt.meabuttontest.utils.VoiceHelper;
import net.diyigemt.roll.MainActivity;
import net.diyigemt.roll.R;

import java.io.IOException;
import java.util.Random;

public class MeaSounds {

  IRequestPermissions requestPermissions = RequestPermissions.getInstance();//动态权限请求
  SoundPool soundPool = new SoundPool.Builder().setMaxStreams(AudioManager.STREAM_MUSIC).build();
  static FileUtils fileUtils;
  Random random = new Random();
  private int soundId;

  public MeaSounds(Activity activity){
    init(activity);
  }

  private void init(Activity activity){
    ContextUtils temp = new ContextUtils(activity.getApplicationContext());//实例化fileUtils类之前务必先实例化此类
    fileUtils = new FileUtils();
//    String s = temp.getPath(Environment.DIRECTORY_MUSIC);
//    Toast.makeText(activity.getApplicationContext(),s,Toast.LENGTH_LONG).show();
    if(!requestPermissions(activity)){
      return;
    }
    try {
      VoiceHelper.update(activity.getAssets());
//      VoiceHelper.fetchExistVoice(activity.getAssets());
    } catch (IOException e) {
      e.printStackTrace();
    }
    soundPool.setOnLoadCompleteListener((SoundPool soundPool, int sampleId, int status) -> {
      soundPool.play(soundId, 1, 1, 0, 0, 1);
    });
  }

  public void clickEvent(eventCallBack callBack){
    String name =  doClickEvent();
    if (callBack != null) {
      callBack.putLog(name);
    }
  }

  public void clickEvent(){
    doClickEvent();
  }

  public interface eventCallBack {
    void putLog(String log);
  }

  private String doClickEvent() {
    soundPool.stop(soundId);
    random.setSeed(System.nanoTime());
    int roll = random.nextInt(VoiceHelper.getVoiceCount());
    VoiceDescription voiceDescription = VoiceHelper.get(roll);
    while (!fileUtils.isFileExist(voiceDescription.getVoiceName())) {
      roll = random.nextInt(VoiceHelper.getVoiceCount());
      voiceDescription = VoiceHelper.get(roll);
    };
    soundId = soundPool.load(fileUtils.getFileDir(voiceDescription.getVoiceName()), 0);
    return voiceDescription.getVoiceDescription();
  }

  public static void StartDownloadService(Activity activity){
    boolean flag = true;
    int total = VoiceHelper.getVoiceCount();
    VoiceDescription voiceDescription;
    for(int i = 0; i < total; i++){
      voiceDescription = VoiceHelper.get(i);
      if(!fileUtils.isFileExist(voiceDescription.getVoiceName())){
        if(flag){
          Toast.makeText(activity.getApplicationContext(),"正在下载音效资源",Toast.LENGTH_LONG).show();
          flag = false;
        }
        Intent intent = new Intent(activity.getApplicationContext(), DownloadService.class);
        intent.putExtra(DownloadService.ACTION, i);
        activity.startService(intent);
      }
    }
  }

  //请求权限
  private boolean requestPermissions(Activity activity){
    //需要请求的权限
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //开始请求权限
    return requestPermissions.requestPermissions(
        activity,
        permissions,
        PermissionUtils.ResultCode1);
  }
}