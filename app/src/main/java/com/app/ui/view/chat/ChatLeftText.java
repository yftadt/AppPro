package com.app.ui.view.chat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/22.
 */
public class ChatLeftText extends TextView {


    public ChatLeftText(Context context) {
        super(context);
        init();
    }

    public ChatLeftText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatLeftText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Path path;
    private Paint paint;

    public void init() {
        paint = new Paint();
        paint.setColor(0xffffffff);
        //paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(paintWidth);
        path = new Path();
        setPadding(PaddingL, PaddingT, PaddingR, PaddingB);
        setMinimumWidth(paintWidth + triangleH + paintWidth);
        setMinimumHeight(paintWidth + triangleW + triangleT + triangleT + paintWidth);
    }

    private int paintWidth = 5;

    //triangleH:三角形高；triangleW:三角形宽；triangleT:三角形离顶点距离
    private int triangleH = 15, triangleW = 30, triangleT = 20;
    //设置文字边距
    private int PaddingT = paintWidth + 10, PaddingB = paintWidth + 10,
            PaddingL = triangleH + paintWidth + 20, PaddingR = paintWidth + 20;

    private void drawBackground(Canvas canvas) {
        int x0 = paintWidth;
        int y0 = paintWidth;
        int x1 = getWidth() - paintWidth;
        int y1 = getHeight() - paintWidth;
        path.reset();
        // 三角形
        path.moveTo(x0, triangleT + (triangleW / 2));//顶点
        //向上
        path.lineTo(x0 + triangleH, triangleT);
        //顶点的对边
        path.lineTo(x0 + triangleH, triangleT + triangleW);
        path.close();
        canvas.drawPath(path, paint);
        //画矩形
        RectF oval3 = new RectF(x0 + triangleH, y0, x1, y1);
        canvas.drawRoundRect(oval3, 10, 10, paint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        super.onDraw(canvas);

    }
    private void test(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        // 实例化路径
        Path mPath = new Path();
        // 移动起点至[100,100]
        mPath.moveTo(100, 100);
        mPath.lineTo(110, 100);
        // 连接路径到点
        RectF oval = new RectF(110, 100, 130, 120);
        mPath.arcTo(oval, 270, 90);
        mPath.lineTo(130, 150);
        //
        RectF oval2 = new RectF(130 - 20, 170 - 30, 130, 160);
        mPath.arcTo(oval2, 0, 90);
        mPath.lineTo(20, 160);
        //
        RectF oval3 = new RectF(20 - 15, 160 - 40, 15, 160);
        mPath.arcTo(oval3, 90, 90);
        //
        mPath.lineTo(5, 120);
        RectF oval4 = new RectF(5, 120 - 20, 25, 120);
        mPath.arcTo(oval4, 180, 90);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }
}
