package net.diyigemt.meabuttontest.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import net.diyigemt.meabuttontest.entity.VoiceDescription;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class FileUtils {

  private String SD_PATH;

  public String getSD_PATH() {
    return this.SD_PATH;
  }

  public FileUtils() {
//    SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + '/' + Constant.APP_DATA_FOLDER_NAME + '/';
      SD_PATH = ContextUtils.getPath(Environment.DIRECTORY_MUSIC) + '/';
  }

  public File createSDDir(String fileName) throws IOException {
    File dir = new File(SD_PATH + fileName);
    return dir.mkdir() ? dir : null;
  }

  public File createSDFile(String fileName) throws IOException {
    File dir = new File(SD_PATH + fileName);
    dir.createNewFile();
    return dir;
  }

  public boolean isFileExist(String fileName) {
    File file = new File(SD_PATH + fileName);
    return file.exists();
  }

  public File write2SDFromInput(String path, String fileName, InputStream input) {
    File file = null;
    OutputStream outputStream = null;
    try {
      createSDDir(path);
      file = createSDFile(path + fileName);
      outputStream = new FileOutputStream(file);
      byte[] buffer = new byte[4 * 1024];
      int length;
      while ((length = input.read(buffer)) != -1) {
        outputStream.write(buffer, 0, length);
      }
      outputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (outputStream != null) {
          outputStream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return file;
  }

  public void write2SDToFile(String fileName, String contain) throws IOException {
    if (!isFileExist(fileName)) {
      File sdFile = createSDFile(fileName);
      FileOutputStream stream = new FileOutputStream(sdFile);
      OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
      writer.write(contain);
      if (writer != null) {
        writer.close();
      }
      if (stream != null) {
        stream.close();
      }
    }
  }

  public void copy(InputStream source, String dest) {
    OutputStream out = null;
    try {
      out = new FileOutputStream(getFile(dest));
      int len;
      byte[] bytes = new byte[1024];
      while ((len = source.read(bytes)) > 0) {
        out.write(bytes, 0, len);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (source != null) source.close();
        if (out != null) out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void readVoiceSaved() {
    File file = new File(getFileDir(Constant.VOICE_SAVE_FILE_NAME));
    if (!file.exists()) return;
    FileInputStream stream = null;
    BufferedReader reader = null;
    try {
      stream = new FileInputStream(file);
      reader = new BufferedReader(new InputStreamReader(stream));
      String s = null;
      while ((s = reader.readLine()) != null) {
        int index = s.indexOf("description");
        String voiceName = s.substring(Constant.FILE_NAME_LENGTH, index);
        String voiceDescription = s.substring(index + Constant.FILE_DESCRIPTION_LENGTH);
        VoiceDescription t = new VoiceDescription(voiceName, voiceDescription);
        VoiceHelper.voiceExist.add(t);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (reader != null) reader.close();
        if (stream != null) stream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String getFileDir(String fileName) {
    return SD_PATH + fileName;
  }

  public File getFile(String fileName) throws IOException {
    return isFileExist(fileName) ? new File(getFileDir(fileName)) : createSDFile(fileName);
  }
}