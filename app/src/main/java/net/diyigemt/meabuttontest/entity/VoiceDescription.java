package net.diyigemt.meabuttontest.entity;

import net.diyigemt.meabuttontest.utils.Constant;

public class VoiceDescription {
  private String voiceName = "";
  private String voiceDescription;
  private String md5 = "";

  public VoiceDescription(String voiceName, String voiceDescription) {
    this.voiceName = voiceName;
    this.voiceDescription = voiceDescription;
  }

  public VoiceDescription(String voiceName, String voiceDescription, String md5) {
    this.voiceName = voiceName;
    this.voiceDescription = voiceDescription;
    this.md5 = md5;
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

  public String getMd5() {
    return md5;
  }

  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public boolean verifyMD5(String md5) {
    return this.md5.equals(md5);
  }
}
