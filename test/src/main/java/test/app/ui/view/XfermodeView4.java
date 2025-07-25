package test.app.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class XfermodeView4 extends View {
    public XfermodeView4(Context context) {
        super(context);
        init();

    }

    public XfermodeView4(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public XfermodeView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public XfermodeView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint mPaint;


    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null); //关闭硬件加速器
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStrokeWidth(2f);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

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
        //1、画矩形  (DST)
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, width, height, mPaint);
        //2、设置xfermode
        mPaint.setXfermode(sModes);
        //3、画圆角矩形(SRC)
        mPaint.setColor(-0x995501);
        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), 100, 100, Path.Direction.CW);
        canvas.drawPath(path, mPaint);
        //4、清空xfermode
        mPaint.setXfermode(null);
        canvas.restoreToCount(layer);


    }

    /**
     * NOTE：先绘制DST(下层)  后绘制SRC(上层)
     */
    private Xfermode sModes = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT); // DST图像的非交集区域显示，SRC图像被移除


}
