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
//https://juejin.cn/post/7491927782772932623
class XfermodeView2 extends View {
    public XfermodeView2(Context context) {
        super(context);
        init();

    }

    public XfermodeView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public XfermodeView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public XfermodeView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        for (int row = 0; row <= 4; row++) {
            for (int column = 0; column <= 3; column++) {
                if (row == 4 && (column == 2 || column == 4)) {
                    return;
                }
                canvas.save();
                int width = getWidth();
                int height = getHeight();
                //保存当前图层
                int layer = canvas.saveLayer(0f, 0f, width, height, null);
                mPaint.setXfermode(null);
                int index = row * 4 + column;
                float translateX = (mItemSize + mItemHorizontalOffset) * column;
                float translateY = (mItemSize + mItemVerticalOffset) * row;
                canvas.translate(translateX, translateY);

                //画文字
                String text = sLabels.get(index);
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
                mPaint.setXfermode(sModes.get(index));
                //3、画矩形(SRC)
                mPaint.setColor(mRectColor);
                float rectRight = mCircleRadius + mRectSize;
                float rectBottom = mCircleRadius + mRectSize;
                canvas.drawRect(left, top, rectRight, rectBottom, mPaint);
                //4、清空xfermode
                mPaint.setXfermode(null);
                canvas.restoreToCount(layer);
            }
        }


    }

    /**
     * NOTE：先绘制DST(下层)  后绘制SRC(上层)
     */
    private List<Xfermode> sModes = Arrays.asList(
            //Alpha合成效果：
            /**
             * IN：取指定层的交集部分，如：SRC_IN 表示取交集部分的SRC图像
             * OUT：取指定层的非交集部分，如：DST_OUT 表示取非交集的DST图像部分
             * OVER：取指定层在上层显示，如：SRC_OVER表示SRC图像覆盖DST图像
             * XOR：取上下两层的非交集部分显示
             * ATOP：取指定层的交集部分和非指定层的非交集部分，如： DST_ATOP表示取交集部分的DST + 非交集部分的SRC
             */
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),  // DST区域被清空，不绘制任何东西，要闭硬件加速，否则无效
            new PorterDuffXfermode(PorterDuff.Mode.SRC),  // 显示SRC图像，完全覆盖DST图像
            new PorterDuffXfermode(PorterDuff.Mode.DST),  // 显示DST图像，不显示SRC图像。
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),  // SRC图像覆盖DST图像
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),  // DST图像覆盖SRC图像
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),  // 取交集部分的SRC图像
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),  // 取交集部分的DST图像
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),  // SRC图像的非交集区域显示，DST图像被移除
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),  // DST图像的非交集区域显示，SRC图像被移除
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),  //取交集部分的SRC + 非交集部分的DST
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),  //取交集部分的DST + 非交集部分的SRC
            new PorterDuffXfermode(PorterDuff.Mode.XOR),  // 显示SRC图像与DST图像不重叠的部分，交集区域被移除。
            //混合特效：注意要闭硬件加速，否则无效
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),  // 相交区域变暗。
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),  // 相交区域变亮。
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),  // 显示重合的图像，SRC图像和DST图像的颜色值相乘，颜色合并
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN), // SRC图像和DST图像的颜色值叠加，得到更亮的颜色
            new PorterDuffXfermode(PorterDuff.Mode.ADD), //饱和度叠加，SRC图像和DST图像的颜色值相加，颜色变得更亮
            new PorterDuffXfermode(PorterDuff.Mode.OVERLAY) //结合 MULTIPLY 和 SCREEN 模式，根据背景的亮度进行混合
    );

    private List<String> sLabels = Arrays.asList("Clear", "Src", "Dst", "SrcOver", "DstOver", "SrcIn", "DstIn", "SrcOut", "DstOut", "SrcATop", "DstATop", "Xor", "Darken", "Lighten", "Multiply", "Screen", "Add", "Overlay");
}
