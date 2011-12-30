package com.itjiaozi.iris.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.itjiaozi.iris.TheApplication;

public class OSUtil {
    public static void startCall(String phoneNumber) {
        if (null == phoneNumber || "".equals(phoneNumber)) {
            return;
        }

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

    public static void startBroswerSearch(String keyword) {
        try {
            String strUri = String.format("http://www.baidu.com/s?wd=%s", URLEncoder.encode(keyword, "utf-8"));
            Uri uri = Uri.parse(strUri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            TheApplication.getInstance().getCurrentActivity().startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统发短信功能
     */
    public static void startSysMessage(String content, String... numbers) {
        StringBuilder sb = new StringBuilder();
        sb.append("smsto://");
        if (null != numbers) {
            for (int i = 0; i < numbers.length; i++) {
                if (i < numbers.length - 1) {
                    sb.append(numbers[i] + ",");
                } else {
                    sb.append(numbers[i]);
                }
            }
        }
        Uri smsToUri = Uri.parse(sb + "");
        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", content);
        TheApplication.getInstance().getCurrentActivity().startActivity(intent);
    }
}
