package com.library.baseui.utile.img;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by Admin on 2016/8/16.
 */
public class ChatTransform {
    //0: 三角形在左侧 ； 1：三角形在右侧；
    private int type;
    public ChatTransform(int type) {
        this.type = type;
    }

    //宽度大于w时进行缩小
    public Bitmap drawBitmap(Bitmap source, int w) {
        source = ImageUtile.zoomBitmap(source, w);
        return drawBitmap(source);
    }

    private int paintWidth = 0;
    //triangleH:三角形高；triangleW:三角形宽；triangleT:三角形离顶点距离
    private int triangleH = 15, triangleW = 30, triangleT = 20;

    public Bitmap drawBitmap(Bitmap source) {
        int bitW = source.getWidth();
        int bitH = source.getHeight();
        //设置画布
        Bitmap result = Bitmap.createBitmap(bitW, bitH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        //设置画笔
        Paint paint = new Paint();
        paint.setColor(0xffffffff);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(paintWidth);
        Bitmap squared = Bitmap.createBitmap(source, 0, 0, bitW, bitH);
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP,
                BitmapShader.TileMode.CLAMP));
        //画三角形在左边
        if (0 == type) {
            drawTriangleLeft(canvas, paint, bitW, bitH);
        }
        if (1 == type) {
            drawTriangleRight(canvas, paint, bitW, bitH);
        }
        return result;
    }

    // 画三角形在左边
    private void drawTriangleLeft(Canvas canvas, Paint paint, int bitW, int bitH) {
        Path path = new Path();
        int x0 = paintWidth;
        int y0 = paintWidth;
        int x1 = bitW - paintWidth;
        int y1 = bitH - paintWidth;
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

    // 画三角形在右边
    private void drawTriangleRight(Canvas canvas, Paint paint, int bitW, int bitH) {
        Path path = new Path();
        int x0 = paintWidth;
        int y0 = paintWidth;
        int x1 = bitW - paintWidth;
        int y1 = bitH - paintWidth;
        //画矩形
        RectF oval3 = new RectF(x0, y0, x1 - triangleH, y1);
        canvas.drawRoundRect(oval3, 10, 10, paint);
        // 三角形
        path.moveTo(x1, triangleT + (triangleW / 2));//顶点
        //向上
        path.lineTo(x1 - triangleH, triangleT);
        //顶点的对边
        path.lineTo(x1 - triangleH, triangleT + triangleW);
        path.close();
        canvas.drawPath(path, paint);
        canvas.drawRoundRect(oval3, 10, 10, paint);
    }

}