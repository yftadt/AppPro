package com.app.ui.window.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;


import com.library.baseui.utile.toast.ToastUtile;
import com.retrofits.net.common.RequestBack;

import sj.mblog.Logx;


public class BaseDialog extends Dialog implements View.OnClickListener, RequestBack {


    public BaseDialog(@NonNull Context context) {
        super(context);

    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    protected void init() {
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void show() {
        super.show();
        onPageExpose();
    }

    protected void onPageExpose() {
    }

    //true 可以点击返回键
    private boolean isAvailable = true;

    public void setBackPressedAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public void onBackPressed() {
        if (!isAvailable) {
            return;
        }
        super.onBackPressed();

    }

    protected OnDialogBackListener onDialogBackListener;

    //设置监听
    public void setOnDialogBackListener(OnDialogBackListener onDialogBackListener) {
        this.onDialogBackListener = onDialogBackListener;
    }

    //设置宽度全屏
    protected void setScreenWidthfull() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(layoutParams);
        }
    }

    public void onReqBack(int what, Object obj, String msg, String other) {
        showToast(msg, other);
    }

    @Override
    public void onBack(int what, Object obj, String msg, String other) {
        if (!isShowing()) {
            return;
        }
        onReqBack(what, obj, msg, other);
    }

    private void showToast(String msg, String other) {
        Logx.d("消息-->", "msg:" + msg + " other:" + other);
        if ("成功".equals(msg)) {
            msg = "";
        }
        if (!TextUtils.isEmpty(other)) {
            ToastUtile.showToast(other);
            return;
        }
        if (!TextUtils.isEmpty(msg)) {
            ToastUtile.showToast(msg);
            return;
        }
    }

    @Override
    public void onBackProgress(int i, String s, String s1, long l, long l1) {

    }

    //DialogHint
    public static final int DIALOG_TYPE_HINT = 1;

    public interface OnDialogBackListener {
        /**
         * dialog 自定义事件
         *
         * @param dialogType dialog类型
         * @param eventType  dialog事件类型
         * @param arg        dialog要传出来的值
         */
        void onDialogBack(int dialogType, int eventType, String... arg);
    }

    //==============================Handler====================
    protected Handler uiHandler;

    protected Handler getHandler() {
        if (uiHandler == null) {
            initHandler();
        }
        return uiHandler;
    }

    private void initHandler() {
        uiHandler = new UiHandler();
    }

    class UiHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!isShowing()) {
                return;
            }
            BaseDialog.this.handleMessage(msg);
        }
    }

    protected void handleMessage(Message msg) {

    }


}
