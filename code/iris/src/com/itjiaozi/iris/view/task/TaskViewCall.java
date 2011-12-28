package com.itjiaozi.iris.view.task;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.itjiaozi.iris.R;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.util.IFlySpeechUtil;

public class TaskViewCall extends LinearLayout implements ITaskView {

    EditText editTextContact;

    public TaskViewCall(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.itjiaozi_the_main_view_animator_child_call, null), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        editTextContact = (EditText) findViewById(R.id.editText1);
    }

    @Override
    public void onSpeechBtnClick(final Button btn) {
        btn.setText("请说联系人");
        IFlySpeechUtil.startRecoginze(ETheAiType.Call, new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                String str = (String) arg;
                editTextContact.setText(str);
                btn.setText("点击说话");
            }
        });
    }
}
