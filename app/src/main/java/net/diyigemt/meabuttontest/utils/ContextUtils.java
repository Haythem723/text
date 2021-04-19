package net.diyigemt.meabuttontest.utils;

import android.content.Context;
import android.content.ContextWrapper;

public class ContextUtils {

    public static ContextWrapper contextWrapper;

    public ContextUtils(Context context){
        contextWrapper = new ContextWrapper(context);
    }

    public static String getPath(String type){
        return contextWrapper.getExternalFilesDir(type).getPath();
    }
}
