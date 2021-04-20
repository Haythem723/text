package net.diyigemt.meabuttontest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static boolean FLAG = true;
    protected static MessageDigest messageDigest = null;
    protected static String hexDigits[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static void init(){
        try{
            messageDigest = MessageDigest.getInstance("MD5");
        }catch (NoSuchAlgorithmException e){
            FLAG = false;
            e.printStackTrace();
        }
    }

    public static String getFileMD5(File file){
        init();
        try{
            InputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = inputStream.read(buffer)) > 0){
                messageDigest.update(buffer, 0, read);
            }
            inputStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
        return buffer2Hex(messageDigest.digest());
    }

    private static String buffer2Hex(byte[] bytes){
        return buffer2Hex(bytes, 0, bytes.length);
    }

    private static String buffer2Hex(byte[] bytes, int offset, int length){
        StringBuffer stringBuffer = new StringBuffer(2 * length);
        for(int i = offset; i < (offset + length); i++){
            appendHex(bytes[i], stringBuffer);
        }
        return stringBuffer.toString();
    }

    private static void appendHex(byte bytes, StringBuffer stringBuffer){
        String c0 = hexDigits[(bytes & 0xf0) >> 4];
        String c1 = hexDigits[bytes & 0xf];
        stringBuffer.append(c0);
        stringBuffer.append(c1);
    }
}
