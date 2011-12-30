package com.itjiaozi.iris.view.task;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.iflytek.speech.SpeechError;
import com.itjiaozi.iris.R;
import com.itjiaozi.iris.adapter.ContactListAdapter;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.db.TbContactCache;
import com.itjiaozi.iris.util.IFlySpeechUtil;
import com.itjiaozi.iris.util.TaskUtil;
import com.itjiaozi.iris.util.ToastUtil;
import com.itjiaozi.iris.util.IFlySpeechUtil.IRecoginzeResult;
import com.itjiaozi.iris.util.OSUtil;

public class TaskViewMessage extends LinearLayout implements ITaskView, OnClickListener {
    AutoCompleteTextView editTextContact;
    EditText editTextMessageContent;
    Button buttonSend;
    protected volatile String needSendMessageContactName;
    protected volatile String needSendMessageContactNumber;

    public TaskViewMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.itjiaozi_the_main_view_animator_child_message, null), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        editTextContact = (AutoCompleteTextView) findViewById(R.id.editText1);
        editTextMessageContent = (EditText) findViewById(R.id.editText2);
        buttonSend = (Button) findViewById(R.id.button1);
        buttonSend.setOnClickListener(this);

        Cursor cursor = TbContactCache.queryContactsCursor("");
        ContactListAdapter adapter = new ContactListAdapter(getContext(), cursor);
        editTextContact.setAdapter(adapter);

        editTextContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                needSendMessageContactName = (String) view.getTag(ContactListAdapter.VIEW_TYPE_CONTACT_NAME);
                needSendMessageContactNumber = (String) view.getTag(ContactListAdapter.VIEW_TYPE_CONTACT_NUMBER);
            }
        });
    }

    private IRecoginzeResult mIRecoginzeResult = new IRecoginzeResult() {

        @Override
        public void onCallback(SpeechError error, int confidence, String result) {
            if (editTextContact.hasFocus()) {
                result = TaskUtil.searchContactNameForMessage(result);
                List<TbContactCache> list = TbContactCache.queryContacts(result);
                if (list.size() > 0) {
                    needSendMessageContactName = list.get(0).FullName;
                    needSendMessageContactNumber = list.get(0).Number;
                } else {
                    needSendMessageContactName = "";
                    needSendMessageContactNumber = "";
                }
                editTextContact.setText(needSendMessageContactName + "\t\t" + needSendMessageContactNumber);
            } else {
                editTextMessageContent.getText().append(result + " ");
            }
        }
    };

    @Override
    public void onSpeechBtnClick(final Button btn) {
        if (editTextContact.hasFocus()) {
            IFlySpeechUtil.startRecoginze(ETheAiType.Message, mIRecoginzeResult);
        } else {
            IFlySpeechUtil.startRecoginze(mIRecoginzeResult);
        }
    }

    @Override
    public void setData(String... args) {
        if (null != args && args.length > 0) {
            editTextContact.setText(args[0]);
        }
    }

    @Override
    public void onClick(View v) {
        if (editTextContact.hasFocus()) {
            ToastUtil.showToast("联系人");
        }
        if (editTextMessageContent.hasFocus()) {
            ToastUtil.showToast("内容");
        }

        String content = editTextMessageContent.getText().toString();
        if (!"".equals(content) && null != needSendMessageContactNumber) {
            OSUtil.startSysMessage(content, needSendMessageContactNumber);
        }
    }
}
