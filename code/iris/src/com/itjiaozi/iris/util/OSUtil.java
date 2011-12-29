package com.itjiaozi.iris.util;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.itjiaozi.iris.TheApplication;

public class OSUtil {
    public static void startCall(String phoneNumber) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + phoneNumber));
        TheApplication.getInstance().startActivity(intent);
    }

    public static void startApp(String appPackage) {
        PackageManager packageManager = TheApplication.getInstance().getPackageManager();
        Intent ttt = new Intent(Intent.ACTION_VIEW);
        try {
            ttt = packageManager.getLaunchIntentForPackage(appPackage);
            TheApplication.getInstance().startActivity(ttt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
