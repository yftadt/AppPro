package test.app.ui.view.errorlayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.baseui.utile.app.APKInfo;
import com.library.baseui.view.text.TextViewManager;

import test.app.ui.activity.R;

/**
 * 账号 密码，等输错的提示
 * Created by Administrator on 2017/9/8.
 */

class AccountRrrorView extends TextView {


    public AccountRrrorView(Context context) {
        super(context);
        init(context);
    }

    public AccountRrrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AccountRrrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private int showHeight;

    private void init(Context context) {
        int errorTvHigh = (int) APKInfo.getInstance().getDIPTOPX(30);
        this.showHeight = (errorTvHigh + 20);
        setPadding(0, 20, 0, 0);
    }

    //
    public void setErrorMsg(int typeError, String errorMsg) {
        switch (typeError) {
            case 0:
                //隐藏错误提示
                setErrorMsgShow(0, "");
                break;
            default:
                //其他错误提示
                setErrorMsgShow(R.mipmap.error_layout_et_error, errorMsg);
                break;
        }
    }

    //显示错误信息
    private void setErrorMsgShow(int icon, String msg) {
        if (icon == 0 || TextUtils.isEmpty(msg)) {
            setViewShow(false);
            return;
        }
        TextViewManager.setText(this.getContext(),this, icon, msg, 0);
        setViewShow(true);
    }

    private boolean isShow;

    //
    private void setViewShow(boolean issShow) {
        if (this.isShow && issShow) {
            return;
        }
        if (!this.isShow && !issShow) {
            return;
        }
        this.isShow = issShow;
        animator(issShow);
    }

    //
    private void setShow(int h) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        if (lp == null) {
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h);
        }
        lp.height = h;
        setLayoutParams(lp);

    }

    private ValueAnimator animator;

    private void animator(boolean issShow) {
        if (animator == null) {
            animator = ValueAnimator.ofInt();
            animator.setDuration(300);
            animator.addUpdateListener(new OnAnimatorUpdateListener());
            animator.addListener(new onAnimatorListener());
        }
        if (issShow) {
            animator.setFloatValues(0, showHeight);
        } else {
            animator.setFloatValues(showHeight, 0);
        }
        animator.cancel();
        animator.start();
    }

    class OnAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float move = (Float) animation.getAnimatedValue();
            setPadding(0, -showHeight + (int) move, 0, 0);
            setShow((int) move);
        }
    }

    class onAnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
