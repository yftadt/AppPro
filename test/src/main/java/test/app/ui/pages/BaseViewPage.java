package test.app.ui.pages;

import android.content.Context;
import android.text.TextUtils;


import com.library.baseui.activity.BaseApplication;
import com.library.baseui.page.BaseCompatPage;
import com.library.baseui.utile.toast.ToastUtile;
import com.retrofits.net.common.RequestBack;

import test.app.ui.activity.R;
import test.app.ui.window.dialog.BaseDialog;
import test.app.ui.window.dialog.DialogCustomWaiting;

/**
 * Created by Administrator on 2016/8/15.
 */
public abstract class BaseViewPage extends BaseCompatPage implements RequestBack, BaseDialog.OnDialogBackListener {

    protected BaseApplication baseApplication;
    public BaseViewPage(Context context) {
        super(context);
        baseApplication = (BaseApplication)  context.getApplicationContext();

    }

    public BaseViewPage(Context context, boolean isLoadingShow) {
        super(context, isLoadingShow);
        baseApplication = (BaseApplication)  context.getApplicationContext();
    }


    @Override
    protected int[] getLoadingImg() {
        return new int[]{R.mipmap.loading_fixation, R.mipmap.loading_tailor,
                R.mipmap.loading_failure};
    }
    //=======================等待dialog===============================
    private DialogCustomWaiting dialog;

    protected void dialogShow() {
        if (dialog == null) {
            dialog = new DialogCustomWaiting(context);
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

    @Override
    public void onBackProgress(int i, String s, String s1, long l, long l1) {

    }

    protected void loadingSucceed(boolean isEmpty, boolean isEmptyClick) {
        loadingSucceed(isEmpty, R.mipmap.loagding_empty, "什么都没有发现", isEmptyClick);
    }

    /**
     * @param isEmpty      是否显示空数据视图
     * @param iconResId    图片资源id
     * @param content      文本提示
     * @param emptyClick 是否响应点击事件
     */
    public void loadingSucceed(boolean isEmpty, int iconResId, String content, int emptyClick) {
        if (!isEmpty) {
            loadingSucceed();
            return;
        }
        loadingEmptyClick(iconResId, content, emptyClick);
    }

    //=========================网络请求回调===================================
    @Override
    public void onBack(int what, Object obj, String msg, String other) {
        showToast(msg, other);
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
