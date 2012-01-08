package com.itjiaozi.iris.view.task;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.iflytek.speech.SpeechError;
import com.itjiaozi.iris.R;
import com.itjiaozi.iris.adapter.ContactListAdapter;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.db.TbContactCache;
import com.itjiaozi.iris.util.IFlySpeechUtil;
import com.itjiaozi.iris.util.IFlySpeechUtil.IRecoginzeResult;
import com.itjiaozi.iris.util.OSUtil;
import com.itjiaozi.iris.util.TaskUtil;
import com.itjiaozi.iris.util.ToastUtil;

public class TaskViewCall extends LinearLayout implements ITaskView, OnClickListener {

    AutoCompleteTextView editTextContact;
    Button buttonCall;

    private String needCallContactName;
    private String needCallContactNumber;

    public TaskViewCall(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.itjiaozi_the_main_view_animator_child_call, null), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        editTextContact = (AutoCompleteTextView) findViewById(R.id.editText1);
        buttonCall = (Button) findViewById(R.id.button1);

        Cursor cursor = TbContactCache.queryContactsCursor("");
        ContactListAdapter adapter = new ContactListAdapter(getContext(), cursor);
        editTextContact.setAdapter(adapter);
        buttonCall.setOnClickListener(this);

    }

    @Override
    public void onSpeechBtnClick(final Button btn) {
        btn.setText("请说联系人");
        IFlySpeechUtil.startRecoginze(ETheAiType.Call, new IRecoginzeResult() {

            @Override
            public void onCallback(SpeechError error, int confidence, String result) {
                result = TaskUtil.searchContactNameForCall(result);
                List<TbContactCache> list = TbContactCache.queryContacts(result);
                if (list.size() > 0) {
                    needCallContactName = list.get(0).FullName;
                    needCallContactNumber = list.get(0).Number;
                } else {
                    needCallContactName = "";
                    needCallContactNumber = "";
                }
                editTextContact.setText(needCallContactName + "\t\t" + needCallContactNumber);
                btn.setText("点击说话");

                if (confidence > 45) {
                    OSUtil.startCall(needCallContactNumber);
                }
            }
        });
    }

    @Override
    public void setData(String... args) {
        if (null != args && args.length > 0) {
            editTextContact.setText(args[0]);
        }
    }

    public static void executeTask(SpeechError error, int confidence, String result) {
    }

    @Override
    public void onClick(View arg0) {
        if (null == needCallContactNumber || "".equals(needCallContactNumber)) {
            return;
        }
        OSUtil.startCall(needCallContactNumber);
    }

    @Override
    public void wakeUp() {
        StringBuilder helpSb = new StringBuilder();
        helpSb.append("输入联系人：键入或说出联系人的名字<br/>");
        helpSb.append("然后点击\"拨打\"");
        TaskViewManager.getHelpTextView().setText(Html.fromHtml(helpSb.toString()));
    }
}
