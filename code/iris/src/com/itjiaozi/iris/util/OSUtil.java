package com.itjiaozi.iris.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.MotionEvent;

import com.itjiaozi.iris.Constant;
import com.itjiaozi.iris.TheApplication;
import com.mobclick.android.MobclickAgent;

public class OSUtil {
    public static void startCall(String phoneNumber) {
        if (null == phoneNumber || "".equals(phoneNumber)) {
            return;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.CALL");
        intent.setData(Uri.parse("tel:" + phoneNumber));

        MobclickAgent.onEvent(TheApplication.getInstance(), Constant.UMENG_EXE_CALL, phoneNumber);
        TheApplication.getInstance().startActivity(intent);
    }

    public static void startApp(String appPackage) {
        PackageManager packageManager = TheApplication.getInstance().getPackageManager();
        Intent ttt = new Intent(Intent.ACTION_VIEW);
        try {
            ttt = packageManager.getLaunchIntentForPackage(appPackage);
            MobclickAgent.onEvent(TheApplication.getInstance(), Constant.UMENG_EXE_OPENAPP, appPackage);
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
            MobclickAgent.onEvent(TheApplication.getInstance(), Constant.UMENG_EXE_WEBSEARCH, keyword);
            TheApplication.getInstance().getCurrentActivity().startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统发短信功能
     */
    public static void startSysMessage(String content, String number) {
        StringBuilder sb = new StringBuilder();
        sb.append("smsto://" + number);
        Uri smsToUri = Uri.parse(sb + "");
        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", content);
        MobclickAgent.onEvent(TheApplication.getInstance(), Constant.UMENG_EXE_MESSAGE, number);
        TheApplication.getInstance().getCurrentActivity().startActivity(intent);
    }
}
