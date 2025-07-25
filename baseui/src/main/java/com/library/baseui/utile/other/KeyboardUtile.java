package com.library.baseui.utile.other;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * 开启或者关闭输入法
 */
public class KeyboardUtile {

    //强制关闭输入法
    public static void hideKeyBoard(Context context, View et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getApplicationWindowToken(), 0);
    }

    //强制关闭输入法
    public static void hideKeyBoardActivity(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public static void showInput(Context activity, EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    //强制打开键盘
    public static void showKeyBoard(Context context, View et) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et, InputMethodManager.SHOW_FORCED);

    }
    //延时弹出输入法
    public static void showKeyBoardDelayed(Context context, EditText view) {
        showKeyBoardDelayed(context, view, 300);
    }

    public static void showKeyBoardDelayed(Context context, EditText view, int time) {
        new HandlerOpen(view, context).sendEmptyMessageDelayed(1, time);
    }

    static class HandlerOpen extends Handler {
        private Context context;
        private EditText view;

        public HandlerOpen(EditText view, Context context) {
            this.context = context;
            this.view = view;
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    showInput(context, view);
                    break;
            }
        }
    }
}
