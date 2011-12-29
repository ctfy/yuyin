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

public class TaskViewMessage extends LinearLayout implements ITaskView {
    EditText editTextContact;
    EditText editTextContent;

    public TaskViewMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.itjiaozi_the_main_view_animator_child_message, null), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        editTextContact = (EditText) findViewById(R.id.editText1);
        editTextContent = (EditText) findViewById(R.id.editText2);
    }

    @Override
    public void onSpeechBtnClick(final Button btn) {
        btn.setText("请说联系人");
        IFlySpeechUtil.startRecoginze(ETheAiType.Call, new IRecoginzeResult() {

            @Override
            public void onCallback(SpeechError error, int confidence, String result) {
                editTextContact.setText(result);
                btn.setText("点击说话");
            }
        });
    }

    @Override
    public void setData(String... args) {
        if (null != args && args.length > 0) {
            editTextContact.setText(args[0]);
        }
    }

}
