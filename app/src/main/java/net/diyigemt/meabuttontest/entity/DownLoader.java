package net.diyigemt.meabuttontest.entity;

import net.diyigemt.meabuttontest.utils.HttpDownLoader;

public class DownLoader extends Thread {
  private String url;
  private String path;
  private String fileName;
  private OnDownloadComplete cb;
  public DownLoader(String url, String path, String fileName) {
    super();
    this.url = url;
    this.path = path;
    this.fileName = fileName;
  }
  public static interface OnDownloadComplete {
    public void start();
  }

  public void setCb(OnDownloadComplete cb) {
    this.cb = cb;
  }

  @Override
  public void run() {
    super.run();
    HttpDownLoader httpDownLoader = new HttpDownLoader();
    httpDownLoader.downloadFile(this.url, this.path, this.fileName);
    try {
      sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (this.cb != null) {
      cb.start();
    }
  }
}
