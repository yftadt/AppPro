package test.app.ui.view.down;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;


import com.library.baseui.utile.toast.ToastUtile;
import com.library.baseui.view.tick.CountDownView;
import com.retrofits.net.common.RequestBack;

/**
 * 获取验证码 和 对比验证码
 * Created by Administrator on 2017/9/12.
 */

public class VerificationCodeView extends CountDownView {

    public VerificationCodeView(Context context) {
        super(context);
    }

    public VerificationCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private String phone;

    //去获取验证码
    public void verificationCodeReq(String phone, int type) {

        timeStart();
    }



    private OnBack onBack = new OnBack();

    //网络数据回调
    class OnBack implements RequestBack {

        @Override
        public void onBack(int what, Object obj, String msg, String other) {

            if (TextUtils.isEmpty(msg)) {
                msg = other;
            }
            if (!TextUtils.isEmpty(msg)) {
                ToastUtile.showToast(msg);
            }
        }

        @Override
        public void onBackProgress(int i, String s, String s1, long l, long l1) {

        }
    }
}
