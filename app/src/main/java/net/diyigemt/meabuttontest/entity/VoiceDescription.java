package net.diyigemt.meabuttontest.entity;

import net.diyigemt.meabuttontest.utils.Constant;
import net.diyigemt.meabuttontest.utils.FileUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class VoiceDescription {
  private String voiceName = "";
  private String voiceDescription;

  public VoiceDescription(String voiceName, String voiceDescription) {
    this.voiceName = voiceName;
    this.voiceDescription = voiceDescription;
  }

  public String getDownloadURL() {
    return Constant.BASIC_DOWNLOAD_PATH + this.voiceName;
  }

  public String getVoiceName() {
    return voiceName;
  }

  public void setVoiceName(String voiceName) {
    this.voiceName = voiceName;
  }

  public String getVoiceDescription() {
    return voiceDescription;
  }

  public void setVoiceDescription(String voiceDescription) {
    this.voiceDescription = voiceDescription;
  }

}
