package test.app.ui.view.view3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import test.app.ui.activity.R;

/**
 * Created by 郭敏 on 2018/5/17 0017.
 */

public class RoundView extends View {

    public RoundView(Context context) {
        super(context);
        init();
    }

    public RoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        super.onDraw(canvas);
    }

    private Paint paint;
    private Path path;

    private void setDraw1(Canvas canvas) {
        int x1 = getWidth();
        int y1 = getHeight();
        //画矩形
        float rx = 20f; // 设置左上角和右上角的圆角半径
        float ry = 20f; // 这里我们让rx和ry相同，你也可以设置为不同的值来改变椭圆的形状
        RectF oval3 = new RectF(0, 0, x1, y1);
        createRoundedRectPath(path, oval3, rx, ry); // 创建只有左上角和右上角有圆角的矩形路径
        //canvas.drawRoundRect(oval3, 10, 10, paint);
        canvas.drawPath(path, paint);
    }

    private void createRoundedRectPath(Path path, RectF rect, float rx, float ry) {
        RectF newRectf = new RectF(rect.left, rect.top, rect.right, rect.bottom);
        float[] radii = new float[]{rx, rx, ry, ry, 0f, 0f, 0f, 0f};
        // 左上角和右上角有圆角，左下角和右下角直角
        path.addRoundRect(newRectf, radii, Path.Direction.CW);
    }

    public void setRound() {
        setBackgroundColor(Color.parseColor("#333333"));
        postInvalidate();
    }
}
