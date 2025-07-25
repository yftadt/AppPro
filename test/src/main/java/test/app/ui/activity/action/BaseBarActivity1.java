package test.app.ui.activity.action;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.core.view.ViewCompat;

import com.library.baseui.activity.BaseCompatBarActivity;
import com.library.baseui.utile.app.APKInfo;
import com.library.baseui.utile.app.ActivityCycle;
import com.library.baseui.utile.toast.ToastUtile;
import com.retrofits.net.common.RequestBack;

import test.app.ui.activity.R;
import test.app.ui.window.dialog.BaseDialog;
import test.app.ui.window.dialog.DialogCustomWaiting;
import test.app.utiles.other.DLog;

/**
 * Created by Administrator on 2016/3/30.
 */
public class BaseBarActivity1 extends BaseCompatBarActivity implements RequestBack, BaseDialog.OnDialogBackListener {

   // private BaseApplication baseApplication;
    //true 开启手势返回
    private boolean isGestureBack = false;
    //true 关闭activity
    private boolean isFinish;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isFinish = true;
    }

    @Override
    public void finish() {
        super.finish();
        isFinish = true;
    }

    @Override
    protected int[] getLoadingImg() {
        return new int[]{R.mipmap.loading_fixation, R.mipmap.loading_tailor,
                R.mipmap.loading_failure};
    }

    //是否支持手势关闭 要在onCreate里，setContentView之前调用
    protected void setGestureBack(boolean isGestureBack) {
        this.isGestureBack = isGestureBack;
    }

    @Override
    protected void contentView(RelativeLayout relativeView) {
        //baseApplication = (BaseApplication) getApplication();
        if (isGestureBack) {
            relativeView.setOnTouchListener(new GestureBackTouch(relativeView));
        }
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




    //========================手势返回=========================
    class GestureBackTouch implements View.OnTouchListener {
        private float x;
        //true 符合手势关闭
        private boolean isTouchOff;
        private View view;
        //移动距离
        private float moveTranslation;
        //true 动画中
        private boolean isAnimation;
        //手机屏宽
        private float wpx;
        private View backView;

        public GestureBackTouch(View view) {
            this.view = view;
            wpx = APKInfo.getInstance().getScreenWidthPixels();
            Activity backActivity = ActivityCycle.getInstance().getBackActivity();
            if (backActivity != null) {
                DLog.e("backActivity", backActivity.getClass().getName());
                backView = backActivity.findViewById(android.R.id.content);
                // backView.clearAnimation();
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (isFinish) {
                return true;
            }
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getRawX();
                    isTouchOff = (x <= 100);
                    if (isAnimation) {
                        isTouchOff = false;
                    }
                    int size = ActivityCycle.getInstance().getBackActivitys();
                    if (size <= 1) {
                        isTouchOff = false;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isTouchOff) {
                        break;
                    }
                    float moveX = event.getRawX();
                    float move = moveX - x;
                    if (move < 0) {
                        move = 0;
                    }
                    moveTranslation = move;
                    ViewCompat.setTranslationX(view, move);
                    if (backView == null) {
                        break;
                    }
                    ViewCompat.setTranslationX(backView, (move - wpx) / 4);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    setAnimation();
                    break;
            }
            return true;
        }


        private void setAnimation() {
            if (moveTranslation == 0 || !isTouchOff) {
                isAnimation = false;
                return;
            }
            isAnimation = true;

            final boolean isBack = moveTranslation > wpx / 3;
            float fromX = moveTranslation, toX = 0;
            if (isBack) {
                //关闭
                fromX = moveTranslation;
                toX = wpx;
            }
            ValueAnimator valueAnimatorSmall = ValueAnimator.ofInt();
            valueAnimatorSmall.setFloatValues(fromX, toX);
            valueAnimatorSmall.setDuration(300);
            valueAnimatorSmall.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float move = (Float) animation.getAnimatedValue();
                    ViewCompat.setTranslationX(view, move);
                    if (backView == null) {
                        return;
                    }
                    ViewCompat.setTranslationX(backView, (move - wpx) / 4);
                }
            });
            valueAnimatorSmall.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimation = false;
                    ViewCompat.setTranslationX(backView, 0);
                    if (isBack) {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    isAnimation = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            valueAnimatorSmall.start();

        }
    }

    /**
     * A(them 没有translucent)-->B(them有translucent)
     * B有动画，而A没有动画 ，原因是translucent的不兼容.
     * activityTranslucentLeftOut()解决此问题。
     * 同样的 B-->A , B有动画，而A没有动画 ,
     * activityTranslucentLeftIn()解决此问题。
     */
    protected void activityTranslucentLeftOut() {
        overridePendingTransition(R.anim.activity_right_in_anim,
                R.anim.activity_left_out_anim);
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.activity_translucent_left_out_anim);
        //
        findViewById(android.R.id.content).startAnimation(animation);

    }

    //activity的进出动画
    protected void activityTranslucentLeftIn() {
        overridePendingTransition(R.anim.activity_left_in_anim, R.anim.activity_right_out_anim);
        //
        View backView = null;
        Activity backActivity = ActivityCycle.getInstance().getBackActivity();
        if (backActivity != null) {
            DLog.e("backActivity", backActivity.getClass().getName());
            backView = backActivity.findViewById(android.R.id.content);
        }
        if (backView != null) {
            ViewCompat.setTranslationX(backView, 0);
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.activity_translucent_left_in_anim);
            backView.startAnimation(animation);
        }
    }
}
