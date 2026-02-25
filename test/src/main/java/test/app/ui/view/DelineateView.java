package test.app.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import sj.mblog.Logx;

public class DelineateView extends View {
    private Paint paint = new Paint();
    private Path path = new Path();
    private float lastX, lastY;

    public DelineateView(Context context) {
        super(context);
        init();
    }

    public DelineateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DelineateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DelineateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(12);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isRest) {
            isRest = false;
            super.onDraw(canvas);
        } else {
            canvas.drawPath(path, paint);
        }

    }

    private boolean isMove;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = false;
                path.moveTo(eventX, eventY);
                lastX = eventX;
                lastY = eventY;
                return true;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                resetPath(eventX, eventY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (!isMove) {
                    isMove = true;
                    resetPath(eventX, eventY);
                    invalidate();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private void resetPath(float eventX, float eventY) {
        path.quadTo(lastX, lastY, (lastX + eventX) / 2, (lastY + eventY) / 2);
        lastX = eventX;
        lastY = eventY;
    }

    public float getLastX() {
        return lastX;
    }

    public float getLastY() {
        return lastY;
    }

    private boolean isRest = false;

    public void setReset() {
        isRest = true;
        path.reset();
        invalidate();
    }
}