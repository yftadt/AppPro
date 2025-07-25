package com.library.baseui.view.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;


/**
 * 所有的loading 都要继承
 * Created by Administrator on 2016/3/30.
 */
public class BaseLoadingLayout extends RelativeLayout {
    // 0 不响应点击事件，1：重新请求事件 2：其它操作事件
    protected int clickType = 1;

    public BaseLoadingLayout(Context context) {
        super(context);
    }

    public BaseLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context, int fixationImgId,
                     int tailorImgId, int failureImgId) {
        setOnClickListenerOpen();
    }

    public void setOnClickListenerOpen() {
        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setOnClickListenerClose() {
        setOnClickListener(null);
    }

    /**
     * 开始显示加载效果
     */
    public void startLoading() {

    }

    /**
     * 重新显示加载显示
     */
    public void startRest() {
    }

    /**
     * 加载成功调用
     */
    public void setLoadingSucceed() {

    }

    /**
     * 加载失败调用
     */
    public void setLoadingFailed() {

    }

    /**
     * @param typeClick 0 不响应点击事件，1：重新请求事件 2：其它操作事件
     */
    public void setLoadingEmpty(int typeClick) {
        this.clickType = typeClick;
    }

    /***
     *
     * @param iconResId 图片资源id
     * @param content 内容
     * @param typeClick 0 不响应点击事件，1：重新请求事件 2：其它操作事件
     */
    public void setEmptyContent(int iconResId, String content, int typeClick) {
        this.clickType = typeClick;
    }

    protected OnResetLoagding onResetLoagding;

    public void setOnResetLoagding(OnResetLoagding onResetLoagding) {
        this.onResetLoagding = onResetLoagding;
    }

    public interface OnResetLoagding {
        void onWarningClick(int clickType);
    }
}
