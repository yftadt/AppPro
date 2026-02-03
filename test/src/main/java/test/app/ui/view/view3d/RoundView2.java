package test.app.ui.view.view3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class RoundView2 extends View {
    // 核心绘制工具
    private Paint mPaint;
    private Path mPath;
    // 可配置参数（默认值，可通过代码动态修改）


    public RoundView2(Context context) {
        this(context, null);
    }

    public RoundView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaintAndPath();
    }

    /**
     * 初始化画笔和路径（只执行一次，避免onDraw重复创建）
     */
    private void initPaintAndPath() {
        // 1. 初始化画笔：支持填充+描边
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 抗锯齿，避免边缘毛糙
        mPaint.setStyle(Paint.Style.FILL); // 同时填充和描边
        mPaint.setColor(Color.WHITE); // 设置背景色（可选）
        // 2. 初始化路径
        mPath = new Path();
        mPath.setFillType(Path.FillType.WINDING); // 填充规则（默认，保证内部完全填充）
    }

    private float mCornerRadius = 20f; // 圆角半径
    private float mSlantOffset = 50f;  // 右侧斜线的水平偏移量（值越大，斜线越陡）

    @Override
    protected void onDraw(Canvas canvas) {
        test2(canvas);
        super.onDraw(canvas);

    }

    private void test1(Canvas canvas) {
        // 重置路径（避免多次绘制路径叠加）
        mPath.reset();
        // 获取View的实际绘制区域（左右上下留描边宽度，避免描边超出View范围）
        float startX = 50f;
        float startY = 50f;
        float endX = getWidth() - startX;
        float endY = getHeight() - startY;

        mPath.moveTo(startX, startY + mCornerRadius);

        RectF topLeftOval = new RectF(startX, startY, startX + mCornerRadius, startY + mCornerRadius);

        mPath.arcTo(topLeftOval, 90f, 90f, false);

        mPath.lineTo(endX, startY); // 顶部直边
        mPath.lineTo(endX - 80f, endY); // 右侧斜线
        mPath.lineTo(startX, endY);
        RectF bottomLeftOval = new RectF(startX, endY - mCornerRadius, startX + mCornerRadius, endY);
        // startAngle=180°（正左），sweepAngle=90°（顺时针），拼接前序点
        mPath.arcTo(bottomLeftOval, 180f, 90f, false);

        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    private void test2(Canvas canvas) {
        // 初始化画笔
        Paint paint = new Paint();
        paint.setAntiAlias(true); // 抗锯齿
        RectF oval = new RectF(50, 50, 250, 250); // 外切正方形（绘制标准圆弧）

// 1. 绘制红色扇形（useCenter=true，填充）
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(oval, 0, 90, true, paint); // 0°起始，顺时针扫90°，中心闭合

// 2. 绘制蓝色纯圆弧（useCenter=false，描边，宽度5px）
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        canvas.drawArc(oval, 90, 180, false, paint); // 90°起始，顺时针扫180°，不闭合
    }

    public void setRound() {
        setBackgroundColor(Color.parseColor("#333333"));
        invalidate();
    }


}