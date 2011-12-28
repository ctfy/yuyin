package com.itjiaozi.iris.view.task;

import java.util.HashMap;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ViewAnimator;

public class TaskViewManager {

    /**
     * 判断是否被声明过了
     * 
     * @return 返回是否已经被初始化过
     */
    public static boolean isDeclared() {
        return null != mViewAnimator;
    }

    private static ViewAnimator mViewAnimator;
    private static HashMap<String, View> maps = new HashMap<String, View>();
    private static HashMap<String, Integer> indexs = new HashMap<String, Integer>();
    private static String currentSelectTaskViewName = null;

    public static void init(Activity context) {
        mViewAnimator = new ViewAnimator(context);
    }

    /**
     * 添加一个任务视图
     * 
     * @param taskname
     *            任务名字
     * @param iTaskView
     *            要添加的任务视图
     */
    public static void addTaskView(String taskname, View iTaskView) {
        if (!isDeclared()) {
            throw new IllegalStateException("未初始化");
        }
        if (null == taskname || null == iTaskView) {
            throw new NullPointerException("添加任务失败, taskname:" + taskname + ", iTaskView:" + iTaskView);
        }
        if (!(iTaskView instanceof ITaskView)) {
            throw new IllegalStateException("任务view必须实现ITaskView" + "接口");
        }
        maps.put(taskname, iTaskView);
        indexs.put(taskname, indexs.size());
        mViewAnimator.addView(iTaskView);
    }

    /**
     * 获取任务视图
     * 
     * @param taskname
     *            任务名字
     * @return 任务视图
     */
    public static View getTaskView(String taskname) {
        View view = maps.get(taskname);
        if (null == view) {
            throw new NullPointerException("不存在名字为" + taskname + "的view!");
        }
        return view;
    }

    public static ITaskView getTaskViewInterface(String taskname) {
        View v = getTaskView(taskname);
        if (null == v) {
            throw new NullPointerException("不存在名字为" + taskname + "的view!");
        }
        if (!(v instanceof ITaskView)) {
            throw new ClassCastException("不能将view转换成ITaskView");
        }
        return (ITaskView) v;
    }

    public static void setDisplayTaskView(String taskname) {
        Integer index = indexs.get(taskname);
        mViewAnimator.setDisplayedChild(index);
        currentSelectTaskViewName = taskname;
    }

    /***
     * 清空状态
     */
    public static void close() {
        indexs.clear();
        maps.clear();
        mViewAnimator = null;
    }

    public static ViewAnimator getManagerView() {
        if (!isDeclared()) {
            throw new NullPointerException("未初始化TaskViewManager");
        }
        return mViewAnimator;
    }

    public static void performSpeechClick(Button v) {
        ITaskView itv = getTaskViewInterface(currentSelectTaskViewName);
        itv.onSpeechBtnClick(v);
    }
}
