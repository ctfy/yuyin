package com.itjiaozi.iris.view.task;

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
import com.itjiaozi.iris.util.OSUtil;
import com.itjiaozi.iris.util.IFlySpeechUtil.IRecoginzeResult;

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

    @Override
    public void onClick(View v) {
        String content = editTextMessageContent.getText().toString();
        if (!"".equals(content) && null != needSendMessageContactNumber) {
            OSUtil.startSysMessage(content, needSendMessageContactNumber, "1234");
        }
    }
}
