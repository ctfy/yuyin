package com.itjiaozi.iris;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import jregex.Pattern;

import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.ViewSwitchListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.admogo.AdMogoLayout;
import com.admogo.AdMogoTargeting;
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
import com.mobclick.android.MobclickAgent;

public class MainActivity extends Activity implements ViewSwitchListener {
    ViewFlow mViewFlow;
    public TextView mHelp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 友盟统计的crash报告信息
        MobclickAgent.setUpdateOnlyWifi(false);
        MobclickAgent.update(this, 1000 * 60 * 60 * 6);
        MobclickAgent.onError(this);

        TheApplication.getInstance().setCurrentActivity(this);

        setContentView(R.layout.itjiaozi_the_main);
        mViewFlow = (ViewFlow) findViewById(R.id.viewflow);
        mViewFlow.setOnViewSwitchListener(this);
        mHelp = (TextView) findViewById(R.id.help);

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
        case R.id.ad:
            MobclickAgent.onEvent(this, Constant.UMENG_CLICK_AD_BTN, "进入全屏广告页面");
            startActivity(new Intent(this, AdActivity.class));

//            AdMogoLayout adMogoLayoutFull = new AdMogoLayout(this, "5dc6b75be998489095b532f184072220", AdMogoTargeting.FULLSCREEN_AD);
//            FrameLayout.LayoutParams paramsFull = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//            addContentView(adMogoLayoutFull, paramsFull);
            break;
        case R.id.itjiaozi_the_main_view_animator_child_btns_call:
            MobclickAgent.onEvent(this, Constant.UMENG_WITCH_TO_TASK_CALL, "通过点击");
            TaskViewManager.setDisplayTaskView("打电话");
            mViewFlow.snapToScreen(1);
            break;
        case R.id.itjiaozi_the_main_view_animator_child_btns_message:
            MobclickAgent.onEvent(this, Constant.UMENG_SWITCH_TO_TASK_MESSAGE, "通过点击");
            TaskViewManager.setDisplayTaskView("发短信");
            mViewFlow.snapToScreen(1);
            break;
        case R.id.itjiaozi_the_main_view_animator_child_btns_openapp:
            MobclickAgent.onEvent(this, Constant.UMENG_SWITCH_TO_TASK_OPENAPP, "通过点击");
            TaskViewManager.setDisplayTaskView("打开应用");
            mViewFlow.snapToScreen(1);

            break;
        case R.id.itjiaozi_the_main_view_animator_child_btns_websearch:
            MobclickAgent.onEvent(this, Constant.UMENG_SWITCH_TO_TASK_WEBSEARCH, "通过点击");
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
    public void onBackPressed() {
        if (mViewFlow.getSelectedItemPosition() > 0) {
            mViewFlow.snapToScreen(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        TaskViewManager.unInit();
        TheApplication.getInstance().setCurrentActivity(null);
        super.finish();
    }

    @Override
    public void onSwitched(View view, int position) {
        if (0 == position) {
            MobclickAgent.onEvent(this, Constant.UMENG_SWITCH_TO_TASK_MAIN, "进入主任务");

            StringBuilder helpSb = new StringBuilder();
            helpSb.append("<b>帮助，“点击说话”后你可以这样说：</b><br/>");
            helpSb.append("打电话给<a href'#'>小沈阳</a><br/>");
            helpSb.append("发短信给<a href'#'>赵本山</a><br/>");
            helpSb.append("打开<a href'#'>录音机</a>");
            mHelp.setText(Html.fromHtml(helpSb.toString()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}