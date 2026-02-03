package test.app.ui.view.view3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by 郭敏 on 2018/5/17 0017.
 */

public class RoundView3 extends View {

    public RoundView3(Context context) {
        super(context);
        init();
    }

    public RoundView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RoundView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE); // 设置背景色（可选）
        paint.setStyle(Paint.Style.FILL);
        //
        path = new Path();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        setDraw1(canvas);
        drawTriangle(canvas);
        super.onDraw(canvas);
    }

    private Paint paint;
    private Path path;
    private int rx = 20, ry = 40;

    private void setDraw1(Canvas canvas) {
        int x1 = getWidth() - 40;
        int y1 = getHeight();

        RectF oval3 = new RectF(0, 0, x1, y1);
        createRoundedRectPath(path, oval3, rx, ry); // 创建只有左上角和右上角有圆角的矩形路径
        paint.setColor(Color.WHITE);
        canvas.drawPath(path, paint);
    }

    private void createRoundedRectPath(Path path, RectF rect, float rx, float ry) {
        RectF newRectf = new RectF(rect.left, rect.top, rect.right, rect.bottom);
        float[] radii = new float[]{rx, rx, 0f, 0f, ry, ry, 0f, 0f};
        // 左上角和右上角有圆角，左下角和右下角直角
        path.addRoundRect(newRectf, radii, Path.Direction.CW);
    }

    //
    private void drawTriangle(Canvas canvas) {
        int startX = getWidth() - ry ;
        int endX = getWidth();
        int startY = 0;
        int endY = getHeight();
        path.reset(); //
        path.moveTo(startX, startY);
        path.lineTo(endX, startY);
        path.lineTo(startX, endY - 30);
        path.close(); // 闭合路径，形成三角形
        paint.setColor(Color.WHITE);
        canvas.drawPath(path, paint);
    }


    public void setRound() {
        setBackgroundColor(Color.parseColor("#333333"));
        postInvalidate();
    }
}
