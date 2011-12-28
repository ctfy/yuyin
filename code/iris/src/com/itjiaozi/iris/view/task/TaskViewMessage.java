package com.itjiaozi.iris.view.task;

import com.itjiaozi.iris.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class TaskViewMessage extends LinearLayout implements ITaskView {

    public TaskViewMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.itjiaozi_the_main_view_animator_child_message, null), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

}
