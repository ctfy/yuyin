package com.itjiaozi.iris.ai;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.view.View;
import android.view.View.OnClickListener;

import com.itjiaozi.iris.about.TheApps;
import com.itjiaozi.iris.about.TheContacts;
import com.itjiaozi.iris.ai.TheAiManager.ICallback;

public class TheAiApp extends BaseTheAi {

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub
        TheContacts.onChanged.addObserver(mObserver);
    }

    @Override
    public void onUnLoad() {
        TheContacts.onChanged.deleteObserver(mObserver);
    }

    private Observer mObserver = new Observer() {

        @Override
        public void update(Observable observable, Object data) {
            setIsNeedUploadKeys(true);
        }
    };

    public String getKeysString() {
        StringBuilder sb = new StringBuilder();
        List<String> list = TheApps.getAllAppName();
        for (String t : list) {
            sb.append(t + ",");
            sb.append("打开" + t + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public void onShow() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onHide() {
        // TODO Auto-generated method stub

    };
}
