package com.itjiaozi.iris.ai;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.view.View;
import android.view.View.OnClickListener;

import com.itjiaozi.iris.about.TheApps;
import com.itjiaozi.iris.about.TheContacts;
import com.itjiaozi.iris.ai.TheAiManager.ICallback;

public class TheAiAll extends BaseTheAi {

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub
        TheContacts.onChanged.addObserver(mObserver);
        TheApps.onChanged.addObserver(mObserver);
    }

    @Override
    public void onUnLoad() {
        TheContacts.onChanged.deleteObserver(mObserver);
        TheApps.onChanged.deleteObserver(mObserver);
    }

    private Observer mObserver = new Observer() {

        @Override
        public void update(Observable observable, Object data) {
            setIsNeedUploadKeys(true);
        }
    };

    public String getKeysString() {
        StringBuilder sb = new StringBuilder();
        List<String> list = TheContacts.getAllContactsName();
        for (String t : list) {
            sb.append("打电话给" + t + ",");
            sb.append("发短信给" + t + ",");
        }

        List<String> list2 = TheApps.getAllAppName();
        for (String t : list2) {
            sb.append("打开" + t + ",");
            sb.append("启动" + t + ",");
        }

        if (sb.toString().endsWith(",")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {
        // TODO Auto-generated method stub

    };

    @Override
    public OnClickListener getSpeechBtnOnClickListener() {
        return mSpeechBtnClickListener;
    }

    private View.OnClickListener mSpeechBtnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            TheAiManager.getInstance().startRecognize(ETheAiType.All, new ICallback() {

                @Override
                public void onResult(boolean success, String result) {

                }
            });
        }
    };
}
