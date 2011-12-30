package com.itjiaozi.iris.view.task;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.iflytek.speech.SpeechError;
import com.itjiaozi.iris.R;
import com.itjiaozi.iris.adapter.AppListAdapter;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.db.TbAppCache;
import com.itjiaozi.iris.util.IFlySpeechUtil;
import com.itjiaozi.iris.util.IFlySpeechUtil.IRecoginzeResult;
import com.itjiaozi.iris.util.OSUtil;

public class TaskViewOpenApp extends LinearLayout implements ITaskView, OnClickListener {

    public volatile String needOpenPackage;

    AutoCompleteTextView editTextAppName;
    Button buttonOpenApp;

    public TaskViewOpenApp(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.itjiaozi_the_main_view_animator_child_openapp, null), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        editTextAppName = (AutoCompleteTextView) findViewById(R.id.editText1);
        buttonOpenApp = (Button) findViewById(R.id.button1);
        buttonOpenApp.setOnClickListener(this);

        Cursor cursor = TbAppCache.queryLikeAppCursorByName("");
        AppListAdapter adapter = new AppListAdapter(getContext(), cursor);
        editTextAppName.setAdapter(adapter);
        editTextAppName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                needOpenPackage = (String) view.getTag(AppListAdapter.VIEW_TYPE_APP_PACKAGE);
            }
        });
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

    @Override
    public void onClick(View v) {
        if (null != needOpenPackage) {
            OSUtil.startApp(needOpenPackage);
        }
    }

}
