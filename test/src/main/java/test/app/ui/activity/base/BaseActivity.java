package test.app.ui.activity.base;

import android.os.Bundle;
import android.text.TextUtils;


import com.library.baseui.activity.BaseCompatActivity;
import com.library.baseui.utile.toast.ToastUtile;
import com.retrofits.net.common.RequestBack;

import test.app.ui.window.dialog.BaseDialog;
import test.app.ui.window.dialog.DialogCustomWaiting;

/**
 * Created by Administrator on 2016/9/7.
 */
public class BaseActivity extends BaseCompatActivity implements RequestBack, BaseDialog.OnDialogBackListener {
    //private BaseApplication baseApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //baseApplication = (BaseApplication) getApplication();
    }
    //=======================等待dialog===============================
    private DialogCustomWaiting dialog;

    protected void dialogShow() {
        if (dialog == null) {
            dialog = new DialogCustomWaiting(this);
        }
        dialog.show();
    }

    protected void dialogDismiss() {
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }

    //================================dialog事件回调=========================================
    @Override
    public void onDialogBack(int dialogType, int eventType, String... arg) {

    }
    //=============================网络请求回调==========================================
    @Override
    public void onBack(int what, Object obj, String msg, String other) {
        showToast(msg, other);
    }

    @Override
    public void onBackProgress(int i, String s, String s1, long l, long l1) {

    }

    private void showToast(String msg, String other) {
        if (!TextUtils.isEmpty(other)) {
            ToastUtile.showToast(other);
            return;
        }
        if (!TextUtils.isEmpty(msg)) {
            ToastUtile.showToast(msg);
            return;
        }
    }
}
