package test.app.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

class XfermodeView3 extends View {
    public XfermodeView3(Context context) {
        super(context);
        init();

    }

    public XfermodeView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public XfermodeView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public XfermodeView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint mPaint;
    private float mItemSize = 0f;
    private float mItemHorizontalOffset = 0f;
    private float mItemVerticalOffset = 0f;
    private float mCircleRadius = 0f;
    private float mRectSize = 0f;
    private int mCircleColor = -0x33bc;//黄色
    private int mRectColor = -0x995501;//蓝色
    private float mTextSize = 25f;

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null); //关闭硬件加速器
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(2f);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mItemSize = w / 4.5f;
        mItemHorizontalOffset = mItemSize / 6;
        mItemVerticalOffset = mItemSize * 0.426f;
        mCircleRadius = mItemSize / 3;
        mRectSize = mItemSize * 0.6f;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        int width = getWidth();
        int height = getHeight();
        //保存当前图层
        int layer = canvas.saveLayer(0f, 0f, width, height, null);
        mPaint.setXfermode(null);

        //画文字
        String text = sLabels;
        mPaint.setColor(Color.BLACK);
        float textXOffset = mItemSize / 2;
        float textYOffset = mTextSize + (mItemVerticalOffset - mTextSize) / 2;
        canvas.drawText(text, textXOffset, textYOffset, mPaint);
        canvas.translate(0f, mItemVerticalOffset);

        //画边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(-0x1000000);
        canvas.drawRect(2f, 2f, mItemSize - 2, mItemSize - 2, mPaint);
        mPaint.setStyle(Paint.Style.FILL);

        //1、画圆(DST)
        mPaint.setColor(mCircleColor);
        float left = mCircleRadius + 3;
        float top = mCircleRadius + 3;
        canvas.drawCircle(left, top, mCircleRadius, mPaint);
        //2、设置xfermode
        mPaint.setXfermode(sModes);
        //3、画矩形(SRC)
        mPaint.setColor(mRectColor);
        float rectRight = mCircleRadius + mRectSize;
        float rectBottom = mCircleRadius + mRectSize;
        canvas.drawRect(left, top, rectRight, rectBottom, mPaint);
        //4、清空xfermode
        mPaint.setXfermode(null);
        canvas.restoreToCount(layer);


    }

    /**
     * NOTE：先绘制DST(下层)  后绘制SRC(上层)
     */
    private Xfermode sModes = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT); // DST图像的非交集区域显示，SRC图像被移除


    private String sLabels = "DstOut";
}
