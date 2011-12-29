package com.itjiaozi.iris.view.task;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.iflytek.speech.SpeechError;
import com.itjiaozi.iris.R;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.util.IFlySpeechUtil;
import com.itjiaozi.iris.util.IFlySpeechUtil.IRecoginzeResult;

public class TaskViewOpenApp extends LinearLayout implements ITaskView {

    EditText editTextAppName;

    public TaskViewOpenApp(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.itjiaozi_the_main_view_animator_child_openapp, null), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        editTextAppName = (EditText) findViewById(R.id.editText1);
    }

    @Override
    public void onSpeechBtnClick(final Button btn) {
        btn.setText("请说应用名");
        IFlySpeechUtil.startRecoginze(ETheAiType.Call, new IRecoginzeResult() {

            @Override
            public void onCallback(SpeechError error, int confidence, String result) {
                editTextAppName.setText(result);
                btn.setText("点击说话");
            }
        });
    }

    @Override
    public void setData(String... args) {
        if (null != args && args.length > 0) {
            editTextAppName.setText(args[0]);
        }
    }

}
