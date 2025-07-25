package com.library.baseui.view.loading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/3/30.
 */
public class LoadingLayout extends BaseLoadingLayout {
    //固定图，动态图，失败图
    private int fixationImgId, tailorImgId, failureImgId;

    public LoadingLayout(Context context) {
        super(context);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init(Context context, int fixationImgId, int tailorImgId, int failureImgId) {
        super.init(context, fixationImgId, tailorImgId, failureImgId);
        this.fixationImgId = fixationImgId;
        this.tailorImgId = tailorImgId;
        this.failureImgId = failureImgId;
        init(context);
        super.init(context, fixationImgId, tailorImgId, failureImgId);
    }

    private ImageView loagdingFixationIv, loagdingTailorIv;
    private ImageView loagdingFailureIv;
    //没有内容，空
    private TextView loagdingEmptyTv;
    private int backgColor = 0xfff1f1f1;
    //true : 正在加载
    private boolean isloading;

    private void init(Context context) {
        setBackgroundColor(backgColor);
        //加入固定加载灰色图
        loagdingFixationIv = new ImageView(context);
        loagdingFixationIv.setImageResource(fixationImgId);
        LayoutParams rlpFixation = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlpFixation.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(loagdingFixationIv, rlpFixation);

        //加入动态加载高亮图
        loadingBit = BitmapFactory.decodeResource(getResources(),
                tailorImgId);
        loagdingTailorIv = new ImageView(context);
        loagdingTailorIv.setImageBitmap(loadingBit);
        LayoutParams rlpTailor = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlpTailor.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(loagdingTailorIv, rlpTailor);

        //加入加载失败的图
        loagdingFailureIv = new ImageView(context);
        loagdingFailureIv.setImageResource(failureImgId);
        loagdingFailureIv.setBackgroundColor(backgColor);
        LayoutParams rlpFailure = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlpFailure.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(loagdingFailureIv, rlpFailure);

        loagdingFailureIv.setOnClickListener(new OnClick());
        //加载完后，空数据
        loagdingEmptyTv = new TextView(context);
        LayoutParams rlpEmpty = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rlpEmpty.addRule(RelativeLayout.CENTER_IN_PARENT);
        rlpEmpty.leftMargin = 32;
        rlpEmpty.rightMargin = 32;
        loagdingEmptyTv.setGravity(Gravity.CENTER);
        loagdingEmptyTv.setTextColor(0xff999999);
        loagdingEmptyTv.setTextSize(14);
        loagdingEmptyTv.setLineSpacing(6, 1);
        loagdingEmptyTv.setCompoundDrawablePadding(10);
        loagdingEmptyTv.setOnClickListener(new OnClick());
        addView(loagdingEmptyTv, rlpEmpty);

    }

    //开始显示加载效果
    public void startLoading() {
        isloading = true;
        setLoadingState(1);
        handler.sendEmptyMessageDelayed(LOADING, 10);
    }

    //加载成功
    public void setLoadingSucceed() {
        isloading = false;
        handler.removeMessages(LOADING);
    }

    //加载失败
    public void setLoadingFailed() {
        isloading = false;
        setLoadingState(2);
        handler.removeMessages(LOADING);
    }

    //重新显示加载显示
    public void startRest() {
        if (isloading) {
            return;
        }
        isloading = true;
        setLoadingState(1);
        handler.sendEmptyMessageDelayed(LOADING, 5);
    }


    @Override
    public void setLoadingEmpty(int typeClick) {
        super.setLoadingEmpty(typeClick);
        isloading = false;
        handler.removeMessages(LOADING);
        setLoadingState(4);
    }

    @Override
    public void setEmptyContent(int iconResId, String content, int typeClick) {
        super.setEmptyContent(iconResId, content, typeClick);
        if (iconResId != 0) {
            Drawable drawable = getContext().getResources().getDrawable(iconResId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            loagdingEmptyTv.setCompoundDrawables(null, drawable, null, null);
        }
        loagdingEmptyTv.setText(content);
        isloading = false;
        setLoadingState(3);
    }

    private void setLoadingState(int state) {
        switch (state) {
            case 1:
                //要加载中的状态
                loagdingEmptyTv.setVisibility(View.GONE);
                loagdingFailureIv.setVisibility(View.GONE);
                loagdingFixationIv.setVisibility(View.VISIBLE);
                loagdingTailorIv.setVisibility(View.VISIBLE);
                break;
            case 2:
                //要加载失败的状态
                loagdingEmptyTv.setVisibility(View.GONE);
                loagdingFailureIv.setVisibility(View.VISIBLE);
                loagdingFixationIv.setVisibility(View.GONE);
                loagdingTailorIv.setVisibility(View.GONE);
                break;
            case 3:
                //要没数据的状态
                loagdingEmptyTv.setVisibility(View.VISIBLE);
                loagdingFailureIv.setVisibility(View.GONE);
                loagdingFixationIv.setVisibility(View.GONE);
                loagdingTailorIv.setVisibility(View.GONE);
                break;

        }
    }


    class OnClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == loagdingEmptyTv && clickType == 0) {
                return;
            }
            if (isloading) {
                return;
            }
            if (clickType > 1) {
                onResetLoagding.onWarningClick(clickType);
                return;
            }
            //重新加载
            startRest();
            if (onResetLoagding == null) {
                return;
            }
            onResetLoagding.onWarningClick(clickType);
        }
    }


    // 加载中
    private final int LOADING = -1;
    /**
     * 当前百分数
     */
    private int num = 0;
    private Bitmap loadingBit;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!isloading) {
                return;
            }
            switch (msg.what) {
                case LOADING:
                    num++;
                    if (num > 100) {
                        num = 0;
                    }
                    loagdingTailorIv.setImageBitmap(getRoundedCornerBitmap(
                            loadingBit, num));
                    handler.sendEmptyMessageDelayed(LOADING, 5);
                    break;
            }
        }
    };

    // 这里把bitmap图片截取出来pr是指截取比例
    private Bitmap getRoundedCornerBitmap(Bitmap bit, float pr) {
        Bitmap bitmap = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 0, 0);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.clipRect(0, bit.getHeight(), bit.getWidth(), bit.getHeight()
                - (pr * bit.getHeight() / 100));
        canvas.drawBitmap(bit, 0, 0, paint);
        return bitmap;

    }
}
