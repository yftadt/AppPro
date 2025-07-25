package com.library.baseui.view.line;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 虚线 要设置高度
 * Created by 郭敏 on 2018/6/14 0014.
 */

public class DashLineView extends View {
    private Paint paint;

    public DashLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffe5e5e5);
        paint.setStrokeWidth(5);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

    }

    public void setPaint(Paint paint) {
        this.paint = paint;
        postInvalidate();
    }
    public void setStrokeWidth(int width) {
        paint.setStrokeWidth(width);
     }
    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setDashGap(float[] dashGap) {
        paint.setPathEffect(new DashPathEffect(dashGap, 0));
    }

    public void onRefresh() {
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerY = getHeight() / 2;
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawLine(0, centerY, getWidth(), centerY, paint);
    }


}
