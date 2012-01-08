package com.itjiaozi.iris;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.admogo.AdMogoLayout;
import com.admogo.AdMogoTargeting;
import com.mobclick.android.MobclickAgent;

public class AdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = new View(this);
        setContentView(v);

        AdMogoLayout adMogoLayoutFull = new AdMogoLayout(this, "5dc6b75be998489095b532f184072220", AdMogoTargeting.FULLSCREEN_AD);
        FrameLayout.LayoutParams paramsFull = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        addContentView(adMogoLayoutFull, paramsFull);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
