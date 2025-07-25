package test.app.ui.window.dialog;

import android.app.Dialog;
import android.content.Context;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

/**
 * 所有的自定义的dialog，都应该继承BaseDialog
 * Created by 郭敏 on 2018/3/28 0028.
 */

public class BaseDialog extends Dialog implements View.OnClickListener {


    public BaseDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    protected void init() {
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {

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

    protected OnDialogBackListener onDialogBackListener;

    //设置监听
    public void setOnDialogBackListener(OnDialogBackListener onDialogBackListener) {
        this.onDialogBackListener = onDialogBackListener;
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
}
