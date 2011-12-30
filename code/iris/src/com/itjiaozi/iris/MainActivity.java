package com.itjiaozi.iris;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import jregex.Pattern;

import org.taptwo.android.widget.ViewFlow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.iflytek.speech.SpeechError;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.util.IFlySpeechUtil;
import com.itjiaozi.iris.util.IFlySpeechUtil.IRecoginzeResult;
import com.itjiaozi.iris.util.ToastUtil;
import com.itjiaozi.iris.view.task.TaskViewCall;
import com.itjiaozi.iris.view.task.TaskViewManager;
import com.itjiaozi.iris.view.task.TaskViewMessage;
import com.itjiaozi.iris.view.task.TaskViewOpenApp;
import com.itjiaozi.iris.view.task.TaskViewWebSearch;

public class MainActivity extends Activity {
    ViewFlow mViewFlow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TheApplication.getInstance().setCurrentActivity(this);

        setContentView(R.layout.itjiaozi_the_main);
        mViewFlow = (ViewFlow) findViewById(R.id.viewflow);

        TaskViewManager.init(this);
        TaskViewManager.addTaskView("打电话", new TaskViewCall(this, null));
        TaskViewManager.addTaskView("发短信", new TaskViewMessage(this, null));
        TaskViewManager.addTaskView("打开应用", new TaskViewOpenApp(this, null));
        TaskViewManager.addTaskView("网络搜索", new TaskViewWebSearch(this, null));
        ContentAdapter ca = new ContentAdapter();
        ca.addItemView(View.inflate(this, R.layout.itjiaozi_the_main_view_animator_child_btns, null));
        ca.addItemView(TaskViewManager.getManagerView());

        mViewFlow.setAdapter(ca);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        case R.id.itjiaozi_the_main_view_animator_child_btns_call:
            TaskViewManager.setDisplayTaskView("打电话");
            mViewFlow.snapToScreen(1);
            break;
        case R.id.itjiaozi_the_main_view_animator_child_btns_message:
            TaskViewManager.setDisplayTaskView("发短信");
            mViewFlow.snapToScreen(1);
            break;
        case R.id.itjiaozi_the_main_view_animator_child_btns_openapp:
            TaskViewManager.setDisplayTaskView("打开应用");
            mViewFlow.snapToScreen(1);

            break;
        case R.id.itjiaozi_the_main_view_animator_child_btns_websearch:
             TaskViewManager.setDisplayTaskView("网络搜索");
            mViewFlow.snapToScreen(1);

            break;

        case R.id.speekButton:
            if (mViewFlow.getSelectedItemPosition() > 0) {
                TaskViewManager.performSpeechClick((Button) v);
            } else {
                IFlySpeechUtil.startRecoginze(ETheAiType.All, new IRecoginzeResult() {

                    @Override
                    public void onCallback(SpeechError error, int confidence, String result) {
                        if (null != error) {
                            ToastUtil.showToast("无法识别命令");
                        } else {
                            boolean needOpenTaskScreen = TaskViewManager.executeTask(error, confidence, result);
                            if (needOpenTaskScreen) {
                                mViewFlow.snapToScreen(1);
                            }
                        }
                    }
                });
            }
            break;

        default:
            break;
        }
    }

    public static class ContentAdapter extends BaseAdapter {

        private List<View> list = new ArrayList<View>();

        public void addItemView(View view) {
            list.add(view);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    @Override
    public void finish() {
        TaskViewManager.unInit();
        TheApplication.getInstance().setCurrentActivity(null);
        super.finish();
    }
}