package test.app.ui.window.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.viewbinding.ViewBinding;

import test.app.ui.activity.R;


/**
 * 所有的自定义PopupWindow 都应该继承PopupWindow
 * 自定义的弹窗
 * Created by Administrator on 2016/9/14.
 */
public abstract class BasePopupWindow<T extends ViewBinding> extends PopupWindow implements PopupWindow.OnDismissListener {
    private Activity activity;
    private View contextView;
    protected String tag = "BasePopupWindow_";

    public BasePopupWindow(Activity activity) {
        this.activity = activity;
    }

    protected T bidingView;

    protected T getLayoutBinding(LayoutInflater inflater) {
        return null;
    }

    protected void onCreate() {
        if (bidingView != null) {
            setInitData();
            return;
        }
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bidingView = getLayoutBinding(inflater);
        setPopupWindowView(bidingView.getRoot());
        setInitView();
        setInitData();
    }

    /**
     * 调用一次
     */
    protected void setInitView() {
    }

    /**
     * 每次调用
     */
    protected void setInitData() {
    }

    protected void setPopupWindowView(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutId, null);
        setPopupWindowView(view);
    }

    protected void setPopupWindowView(View view) {
        setView(view);
    }

    private void setView(View view) {
        contextView = view;
        setContentView(view);
        setLayoutParams();
        setaAnimation();
        //点击空白处时，隐藏掉pop窗口
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        setOnDismissListener(this);
    }

    protected void setLayoutParams() {
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    //设置动画
    protected void setaAnimation() {
        // 弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
    }

    protected View findViewById(int viewId) {
        return contextView.findViewById(viewId);
    }


    /**
     * @param view     哪个view为基线
     * @param location 显示的位置（ Gravity.BOTTOM...）
     */
    public void showLocation(View view, int location) {
        onCreate();
        showAtLocation(view, location, 0, 0);
        backgroundAlpha(activity, 0.5f);
    }

    /**
     * @param location 显示的位置（ Gravity.BOTTOM...）
     */
    public void showLocation(int location) {
        View view = activity.findViewById(android.R.id.content);
        showLocation(view, location);
    }

    private void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 无效
     *
     * @param view 显示在这个view下
     */
    public void showDown(View view) {
        showAsDropDown(view);
        backgroundAlpha(activity, 0.5f);
    }

    @Override
    public void showAsDropDown(View anchor) {
        //在Android 7.0上PopupWindow.showAsDropDown不起作用的解决方法
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

    /**
     * @param anchorView 显示在这个view下
     */
    public void showViewDown(View anchorView) {
        onCreate();
        // 1. 获取指定View的坐标
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location); // 获取View在屏幕上的坐标
        int x = location[0]; // View的X坐标
        int y = location[1] + anchorView.getHeight(); // View的Y坐标，加上View的高度以显示在正下方
        // 2. 显示PopupWindow
        showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
        backgroundAlpha(activity, 0.5f);
    }


    @Override
    public void onDismiss() {
        backgroundAlpha(activity, 1f);
    }

    protected OnPopupBackListener onPopupBackListener;

    //设置监听
    public void setOnPopupBackListener(OnPopupBackListener onPopupBackListener) {
        this.onPopupBackListener = onPopupBackListener;
    }

    //PopupPhotoOption
    public static final int POPUP_TYPE_PHOTO = 1;

    public interface OnPopupBackListener {
        /**
         * pupup 自定义事件
         *
         * @param pupupType pupup类型
         * @param eventType pupup事件类型
         * @param object    pupup要传出来的值
         */
        void onPopupBack(int pupupType, int eventType, Object object);
    }

    private int mStartY = 0;
    private int mDiffY = 0;

    /**
     * 设置下滑消失
     *
     * @param view
     */
    protected void setSlideDisappear(View view) {

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View rootView = bidingView.getRoot();
                ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
                FrameLayout.LayoutParams fraLayoutParams = null;
                if (layoutParams instanceof FrameLayout.LayoutParams) {
                    fraLayoutParams = (FrameLayout.LayoutParams) layoutParams;
                }
                if (fraLayoutParams == null) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartY = (int) event.getRawY() + fraLayoutParams.bottomMargin;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mDiffY = (int) event.getRawY() - mStartY;
                        int diffY = -mDiffY;
                        if (diffY >= 0) {
                            diffY = 0;
                        }
                        if (diffY != fraLayoutParams.bottomMargin) {
                            fraLayoutParams.bottomMargin = diffY;
                            rootView.setLayoutParams(fraLayoutParams);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (fraLayoutParams.bottomMargin != 0) {
                            dismiss();
                        }

                        break;
                }
                return true;
            }


        });
    }
}
