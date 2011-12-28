package com.itjiaozi.iris;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.taptwo.android.widget.ViewFlow;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.iflytek.ui.UploadDialog;
import com.iflytek.ui.UploadDialogListener;
import com.itjiaozi.iris.ai.BaseTheAi;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.ai.TheAiManager;
import com.itjiaozi.iris.util.SPUtil;
import com.itjiaozi.iris.util.ToastUtil;
import com.itjiaozi.iris.view.task.TaskViewCall;
import com.itjiaozi.iris.view.task.TaskViewManager;
import com.itjiaozi.iris.view.task.TaskViewMessage;
import com.itjiaozi.iris.view.task.TaskViewOpenApp;

public class MainActivity extends Activity {
    ViewFlow mViewFlow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itjiaozi_the_main);
        mViewFlow = (ViewFlow) findViewById(R.id.viewflow);

        TaskViewManager.init(this);
        TaskViewManager.addTaskView("打电话", new TaskViewCall(this, null));
        TaskViewManager.addTaskView("发短信", new TaskViewMessage(this, null));
        TaskViewManager.addTaskView("打开应用", new TaskViewOpenApp(this, null));
        ContentAdapter ca = new ContentAdapter();
        ca.addItemView(View.inflate(this, R.layout.itjiaozi_the_main_view_animator_child_btns, null));
        ca.addItemView(TaskViewManager.getManagerView());

        mViewFlow.setAdapter(ca);
    }

    public void onClick(View v) {
        startRecoginze(ETheAiType.App);
        // mViewAnimator.setDisplayedChild(1);
    }

    @Override
    public void onBackPressed() {
        // if (mViewAnimator.getDisplayedChild() != 0) {
        // mViewAnimator.setDisplayedChild(0);
        // } else {
        super.onBackPressed();
        // }
    }

    public void startRecoginze(final ETheAiType eTheAiType) {
        final BaseTheAi bta = TheAiManager.getInstance().getTheAi(eTheAiType);

        if (bta.getIsNeedUploadKeys()) {
            UploadDialog uploadDialog = new UploadDialog(this, "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
            uploadDialog.setListener(new UploadDialogListener() {

                @Override
                public void onEnd(SpeechError error) {
                    if (null != error) {
                        ToastUtil.showToast("数据上传错误：" + error);
                    }
                }

                @Override
                public void onDataUploaded(String arg0, String grammarID) {
                    bta.setGrammarID(grammarID);
                    bta.setIsNeedUploadKeys(false);
                    startRecoginze(eTheAiType);
                }
            });
            String keys = bta.getKeysString();
            try {
                uploadDialog.setContent(keys.getBytes("UTF-8"), "dtt=keylist", bta.getGrammarName());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            uploadDialog.show();
        } else {
            RecognizerDialog rd = new RecognizerDialog(this, "appid=" + SPUtil.getString(Constant.SP_KEY_XUNFEI_APP_ID, null));
            rd.setListener(new RecognizerDialogListener() {

                StringBuilder sb = new StringBuilder();

                @Override
                public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
                    if (null != results && results.size() > 0) {
                        sb.append(results.get(0).text);
                    }
                    if (isLast) {
                        String ret = sb.toString();
                        onResult(ret);
                        sb = new StringBuilder();
                    }
                }

                @Override
                public void onEnd(SpeechError error) {
                    if (null != error) {
                        ToastUtil.showToast("识别错误：" + error);
                    }
                }
            });
            String engine = null;
            String grammarID = bta.getGrammarID();
            if (null == grammarID) {
                engine = "sms";
            }
            rd.setEngine(engine, null, bta.getGrammarID());
            rd.show();
        }
    }

    public void onResult(String str) {
        ToastUtil.showToast(str);
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
}