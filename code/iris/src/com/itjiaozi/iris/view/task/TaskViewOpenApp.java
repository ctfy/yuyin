package com.itjiaozi.iris.view.task;

import com.itjiaozi.iris.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class TaskViewOpenApp extends LinearLayout implements ITaskView {

    public TaskViewOpenApp(Context context, AttributeSet attrs) {
        super(context, attrs);
        addView(View.inflate(context, R.layout.itjiaozi_the_main_view_animator_child_openapp, null), LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

}
