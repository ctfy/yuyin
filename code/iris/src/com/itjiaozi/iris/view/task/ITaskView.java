package com.itjiaozi.iris.view.task;

import android.widget.Button;

public interface ITaskView {
    void wakeUp();
    
    void setData(String... args);

    void onSpeechBtnClick(Button btn);
}
