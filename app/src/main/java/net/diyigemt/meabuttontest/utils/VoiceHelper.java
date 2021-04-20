package net.diyigemt.meabuttontest.utils;

import android.content.res.AssetManager;


import net.diyigemt.meabuttontest.entity.VoiceDescription;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class VoiceHelper {
  public static ArrayList<String> voiceNames = new ArrayList<String>();
  public static ArrayList<VoiceDescription> voiceExist = new ArrayList<VoiceDescription>();
  public static void update(AssetManager manager) throws IOException {
    InputStreamReader reader = new InputStreamReader(manager.open("voices.json"));
    BufferedReader readL = new BufferedReader(reader);
    String s;
    while ((s = readL.readLine()) != null) {
      String[] split = s.split(";");
      if (split.length != 3) continue;
      VoiceDescription t = new VoiceDescription(split[0], split[1], split[2]);
      VoiceHelper.voiceExist.add(t);
    }
    try {
      readL.close();
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void fetchExistVoice(AssetManager manager) throws IOException {
    FileUtils fileUtils = new FileUtils();
    if (!fileUtils.isFileExist("")) {
      fileUtils.createSDDir("");
    }
    if (!fileUtils.isFileExist(Constant.VOICE_SAVE_FILE_NAME)) {
      fileUtils.copy(manager.open(Constant.VOICE_SAVE_FILE_NAME), Constant.VOICE_SAVE_FILE_NAME);
    }
    fileUtils.readVoiceSaved();
  }

  public static int getVoiceCount() {
    return voiceExist.size();
  }

  public static boolean isExist(int index) {
    return voiceExist.size() > index;
  }

  public static VoiceDescription get(int index) {
    if (isExist(index)) return voiceExist.get(index);
    return null;
  }
}
