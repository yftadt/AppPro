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

//使用path.arcTo 绘制矩形
public class RoundView4 extends View {


    public RoundView4(Context context) {
        this(context, null);
    }

    public RoundView4(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Paint paint;
    private Path path;

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        path = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //onDraw1(canvas);
        //onDraw2(canvas);
        onDraw3(canvas);
    }

    private float cornerRadius = 20f; // 圆角半径

    //四个圆角 无斜边
    private void onDraw1(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // 定义圆角矩形的边界（留出边距）
        RectF rect = new RectF(0, 0, width, height);
        // 重置路径
        path.reset();
        // 绘制左上角圆角
        path.arcTo(new RectF(rect.left, rect.top, rect.left + 2 * cornerRadius, rect.top + 2 * cornerRadius),
                180, 90, false);
        // 绘制顶部直线
        path.lineTo(rect.right - cornerRadius, rect.top);
        // 绘制右上角圆角
        path.arcTo(new RectF(rect.right - 2 * cornerRadius, rect.top, rect.right, rect.top + 2 * cornerRadius),
                270, 90, false);
        // 绘制右侧直线
        path.lineTo(rect.right, rect.bottom - cornerRadius);
        // 绘制右下角圆角
        path.arcTo(new RectF(rect.right - 2 * cornerRadius, rect.bottom - 2 * cornerRadius, rect.right, rect.bottom),
                0, 90, false);
        // 绘制底部直线
        path.lineTo(rect.left + cornerRadius, rect.bottom);
        // 绘制左下角圆角
        path.arcTo(new RectF(rect.left, rect.bottom - 2 * cornerRadius, rect.left + 2 * cornerRadius, rect.bottom),
                90, 90, false);
        // 闭合路径（绘制左侧直线）
        path.close();
        canvas.drawPath(path, paint);
    }

    //四个圆角 有斜边(右侧)
    private int xb = 30;

    private void onDraw2(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // 定义圆角矩形的边界（留出边距）
        RectF rect = new RectF(0, 0, width, height);
        // 重置路径
        path.reset();
        // 绘制左上角圆角
        path.arcTo(new RectF(rect.left, rect.top, rect.left + 2 * cornerRadius, rect.top + 2 * cornerRadius),
                180, 90, false);
        // 绘制顶部直线
        path.lineTo(rect.right - cornerRadius, rect.top);
        // 绘制右上角圆角
        path.arcTo(new RectF(rect.right - 2 * cornerRadius, rect.top, rect.right, rect.top + 2 * cornerRadius),
                270, 90, false);
        // 绘制右侧直线
        path.lineTo(rect.right - xb, rect.bottom - cornerRadius);
        // 绘制右下角圆角
        path.arcTo(new RectF(rect.right - xb - 2 * cornerRadius, rect.bottom - 2 * cornerRadius, rect.right - xb, rect.bottom),
                0, 90, false);
        // 绘制底部直线
        path.lineTo(rect.left + cornerRadius, rect.bottom);
        // 绘制左下角圆角
        path.arcTo(new RectF(rect.left, rect.bottom - 2 * cornerRadius, rect.left + 2 * cornerRadius, rect.bottom),
                90, 90, false);
        // 闭合路径（绘制左侧直线）
        path.close();
        canvas.drawPath(path, paint);
    }

    //四个圆角 有斜边 左侧
    private void onDraw3(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        // 定义圆角矩形的边界（留出边距）
        RectF rect = new RectF(0, 0, width, height);
        // 重置路径
        path.reset();
        // 绘制左上角圆角
        path.arcTo(new RectF(rect.left, rect.top, rect.left + 2 * cornerRadius, rect.top + 2 * cornerRadius),
                180, 90, false);
        // 绘制顶部直线
        path.lineTo(rect.right - cornerRadius, rect.top);
        // 绘制右上角圆角
        path.arcTo(new RectF(rect.right - 2 * cornerRadius, rect.top, rect.right, rect.top + 2 * cornerRadius),
                270, 90, false);
        // 绘制右侧直线
        path.lineTo(rect.right, rect.bottom - cornerRadius);
        // 绘制右下角圆角
        path.arcTo(new RectF(rect.right - 2 * cornerRadius, rect.bottom - 2 * cornerRadius, rect.right, rect.bottom),
                0, 90, false);
        // 绘制底部直线
        path.lineTo(rect.left + cornerRadius+xb, rect.bottom);
        // 绘制左下角圆角
        path.arcTo(new RectF(rect.left+xb, rect.bottom - 2 * cornerRadius, rect.left+xb + 2 * cornerRadius, rect.bottom),
                90, 90, false);
        // 闭合路径（绘制左侧直线）
        path.close();
        canvas.drawPath(path, paint);
    }
}
