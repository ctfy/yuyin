package com.itjiaozi.iris.view.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Contacts;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.iflytek.speech.SpeechError;
import com.itjiaozi.iris.R;
import com.itjiaozi.iris.adapter.ContactListAdapter;
import com.itjiaozi.iris.ai.ETheAiType;
import com.itjiaozi.iris.util.IFlySpeechUtil;
import com.itjiaozi.iris.util.IFlySpeechUtil.IRecoginzeResult;

public class TaskViewCall extends LinearLayout implements ITaskView {

    AutoCompleteTextView editTextContact;

    public TaskViewCall(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.itjiaozi_the_main_view_animator_child_call, null), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        editTextContact = (AutoCompleteTextView) findViewById(R.id.editText1);

        Cursor cursor = getContext().getContentResolver().query(Contacts.People.CONTENT_URI, ContactListAdapter.PEOPLE_PROJECTION, null, null, Contacts.People.DEFAULT_SORT_ORDER);
        ContactListAdapter adapter = new ContactListAdapter(getContext(), cursor);
        editTextContact.setAdapter(adapter);
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

    public static void executeTask(SpeechError error, int confidence, String result) {
    }
}
