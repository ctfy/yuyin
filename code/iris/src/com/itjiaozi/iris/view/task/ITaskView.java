package com.itjiaozi.iris.view.task;

import android.widget.Button;

public interface ITaskView {
    void setData(String... args);

    void onSpeechBtnClick(Button btn);
}
