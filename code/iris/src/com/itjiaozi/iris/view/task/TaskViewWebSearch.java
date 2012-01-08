package com.itjiaozi.iris.view.task;

import java.net.URLEncoder;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
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
import com.itjiaozi.iris.util.OSUtil;

public class TaskViewWebSearch extends LinearLayout implements ITaskView, View.OnClickListener {

    EditText editTextKeywords;
    Button buttonSearch;

    public TaskViewWebSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.itjiaozi_the_main_view_animator_child_websearch, null), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        editTextKeywords = (EditText) findViewById(R.id.editText1);
        buttonSearch = (Button) findViewById(R.id.button1);
        buttonSearch.setOnClickListener(this);
    }

    @Override
    public void onSpeechBtnClick(final Button btn) {
        btn.setText("请说要搜索的内容");
        IFlySpeechUtil.startRecoginze(new IRecoginzeResult() {

            @Override
            public void onCallback(SpeechError error, int confidence, String result) {
                editTextKeywords.setText(result);
                btn.setText("点击说话");
            }
        });
    }

    @Override
    public void setData(String... args) {
        if (null != args && args.length > 0) {
            editTextKeywords.setText(args[0]);
        }
    }

    @Override
    public void onClick(View arg0) {
        String keyword = editTextKeywords.getText().toString();
        OSUtil.startBroswerSearch(keyword);
    }

    @Override
    public void wakeUp() {
        StringBuilder helpSb = new StringBuilder();
        helpSb.append("通过语音输入或输入法输入<a href='#'>搜索内容</a><br/>");
        helpSb.append("然后点击\"搜索\"");
        TaskViewManager.getHelpTextView().setText(Html.fromHtml(helpSb.toString()));
    }
}
