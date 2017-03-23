package com.app.ui.view.popup;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 选择
 * Created by Administrator on 2016/9/14.
 */
public class CustomPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    public CustomPopupWindow(View view) {
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //点击空白处时，隐藏掉pop窗口
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        setOnDismissListener(this);
    }

    @Override
    public void onDismiss() {
        if (activity != null) {
            backgroundAlpha(activity, 1f);
            activity = null;
        }
    }

    private Activity activity;

    public void showDown(Activity activity, View view) {
        showAsDropDown(view);
        if (activity != null) {
            this.activity = activity;
            backgroundAlpha(activity, 0.5f);
        }
    }

    //showLocation(activity,view, Gravity.BOTTOM, 0, 0)
    public void showLocation(Activity activity, View view, int location) {
        showAtLocation(view, location, 0, 0);
        if (activity != null) {
            this.activity = activity;
            backgroundAlpha(activity, 0.5f);
        }
    }


    private void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
