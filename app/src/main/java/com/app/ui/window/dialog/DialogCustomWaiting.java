package com.app.ui.window.dialog;

import android.content.Context;
import android.os.Bundle;

import com.app.ui.activity.R;


/**
 * 自定义进度条,当发起网络请求时显示该对话框，让用户等待
 */
public class DialogCustomWaiting extends BaseDialog {


    public DialogCustomWaiting(Context context) {
        super(context, R.style.dialog_transparent);
        //getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_loading);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public void show() {
        super.show();

    }

}
