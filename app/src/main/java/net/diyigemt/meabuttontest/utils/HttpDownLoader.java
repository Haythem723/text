package net.diyigemt.meabuttontest.utils;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



public class HttpDownLoader {
  private URL url = null;
  FileUtils fileUtils = new FileUtils();

  public int downloadFile(String url, String path, String fileName) {
    if (fileUtils.isFileExist(path + fileName)) {
      return 1;
    } else {
      try {
        InputStream inputStream = getInputStream(url);
        File res = fileUtils.write2SDFromInput(path, fileName, inputStream);
        if (res == null) {
          return -1;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return 0;
  }

  public InputStream getInputStream(String urlString) throws IOException {
    InputStream inputStream = null;
    try {
      url = new URL(urlString);
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setConnectTimeout(3000);
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Host", Constant.HEADER_HOST);
      connection.setRequestProperty("referer", Constant.HEADER_REFERER);
      connection.setRequestProperty("Connection", "keep-alive");
      connection.setRequestProperty("cookie", Constant.HEADER_COOKIE);
      connection.setRequestProperty("User-Agent", Constant.HEADER_USER_AGENT);
      connection.connect();
      inputStream = connection.getInputStream();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return inputStream;
  }
}
