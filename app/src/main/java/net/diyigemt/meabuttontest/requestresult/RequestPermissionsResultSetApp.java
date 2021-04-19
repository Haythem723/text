package net.diyigemt.meabuttontest.requestresult;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class RequestPermissionsResultSetApp implements IRequestPermissionsResult{
  private static RequestPermissionsResultSetApp requestPermissionsResult;
  public static RequestPermissionsResultSetApp getInstance(){
    if(requestPermissionsResult == null){
      requestPermissionsResult = new RequestPermissionsResultSetApp();
    }
    return requestPermissionsResult;
  }

  @Override
  public boolean doRequestPermissionsResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults) {
    List<String> deniedPermission = new ArrayList<>();
    for (int i=0; i<grantResults.length;i++){
      if(grantResults[i] == PackageManager.PERMISSION_DENIED){
        deniedPermission.add(permissions[i]);
      }
    }

    //已全部授权
    if (deniedPermission.size() == 0) {
      return true;
    }
    //引导用户去授权
    else {
      String name = PermissionUtils.getInstance().getPermissionNames(deniedPermission);
      SetPermissions.openAppDetails(activity,name);
    }
    return false;
  }
}