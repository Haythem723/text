package com.example.ailipay;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

public class CrashHandler implements UncaughtExceptionHandler {
    private Context context;
    private static CrashHandler instance;
    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context ctx) {
        context = ctx;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread arg0, Throwable arg1) {

        String logPath;
        File dir;
            logPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        ContextWrapper cw = new ContextWrapper(context);
        dir = cw.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(String.valueOf(dir));
            if (!file.exists()) {
                file.mkdirs();
                Log.e("crash handler", "Errlog saved ats: "+dir.toString());
            }
            try {
                FileWriter fw = new FileWriter(dir + "Errlog.log", true);
                fw.write(new Date() + "：\n");
                StackTraceElement[] stackTrace = arg1.getStackTrace();
                fw.write(arg1.getMessage() + "\n");
                for (int i = 0; i < stackTrace.length; i++) {
                    fw.write("file:" + stackTrace[i].getFileName() + " class:"
                            + stackTrace[i].getClassName() + " method:"
                            + stackTrace[i].getMethodName() + " line:"
                            + stackTrace[i].getLineNumber() + "\n");
                }
                fw.write("\n");
                fw.close();
            } catch (IOException e) {
                Log.e("crash handler", "load file failed...", e.getCause());
            }
        arg1.printStackTrace();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
