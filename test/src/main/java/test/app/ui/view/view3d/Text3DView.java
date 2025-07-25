package test.app.ui.view.view3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by 郭敏 on 2018/5/17 0017.
 */

public class Text3DView extends View {
    private int textSize = 30;
    private int itemVisibleCount = 5;

    public Text3DView(Context context) {
        super(context);
        initData();
    }

    public Text3DView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public Text3DView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public void setTextConfiguration(int textSize, int itemVisibleCount) {

    }

    private void initData() {
        Paint textP = new Paint();
        textP.setAntiAlias(true);
        textP.setTextSize(textSize);
        textP.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textP.setTextAlign(Paint.Align.CENTER);
        textP.setColor(0xff333333);
        //
        Paint.FontMetrics fontMetrics = textP.getFontMetrics();
        float textHeight = fontMetrics.bottom- fontMetrics.top ;
        d("initData","textHeight:"+textHeight);
        float angles= 180/(float)19;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float[] bt = s(canvas);
        //上
        float b1 = bt[0];
       // b1 = s1(canvas, b1, 0.9f);
        b1 = s1(canvas, b1, 0.8f);
      //  b1 = s1(canvas, b1, 0.7f);
        b1 = s1(canvas, b1, 0.6f);
      //  b1 = s1(canvas, b1, 0.5f);
        b1 = s1(canvas, b1, 0.4f);
     //   b1 = s1(canvas, b1, 0.3f);
        b1 = s1(canvas, b1, 0.2f);
     //   b1 = s1(canvas, b1, 0.1f);
      //  b1 = s1(canvas, b1, 0.01f);
        //下
        float t1 = bt[1];
       // t1 = s2(canvas, t1, 0.9f);
        t1 = s2(canvas, t1, 0.8f);
      //  t1 = s2(canvas, t1, 0.7f);
        t1 = s2(canvas, t1, 0.6f);
      //  t1 = s2(canvas, t1, 0.5f);
        t1 = s2(canvas, t1, 0.4f);
      //  t1 = s2(canvas, t1, 0.3f);
        t1 = s2(canvas, t1, 0.2f);
     //   t1 = s2(canvas, t1, 0.1f);
     //   t1 = s2(canvas, t1, 0.01f);
    }

    private float s2(Canvas canvas, float t1, float scaleY) {
        int y = getHeight();
        int x = getWidth();
        canvas.save();
        //
        Paint textP = new Paint();
        textP.setAntiAlias(true);
        textP.setTextSize(textSize);
        textP.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textP.setTextAlign(Paint.Align.CENTER);
        textP.setColor(0xff333333);
        //
        Paint.FontMetrics fontMetrics = textP.getFontMetrics();
        //基线到字体上边框的距离,
        float top = fontMetrics.top;
        //基线到字体下边框的距离
        float bottom = fontMetrics.bottom;
        float textHeight = bottom - top;
        //适合的基线
        float baseLineY = t1 - top;
        Rect rect = new Rect(0, (int) t1, x, (int) (t1 + textHeight));
        canvas.clipRect(rect);
        canvas.drawColor(Color.RED);
        //缩放
        // canvas.scale(1.0F, 0.8f);
        canvas.scale(1.0F, scaleY, rect.centerX(), rect.centerY());
        //移动到中间
        // canvas.translate( rect.centerX(), rect.centerY());
        d("s1", "centerX:" + rect.centerX() + " centerY:" + rect.centerY());
        //画基线
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        d("s1", "baseLineY:" + baseLineY);
        // baseLineY += textHeight * (1 - 0.8f)+20;
        d("s1", "baseLineY:" + textHeight * (1 - 0.8f) + " centerY:" + rect.centerY());
        canvas.drawLine(0, baseLineY, x, baseLineY, paint);
        //
        canvas.drawText("我爱你", x / 2, baseLineY, textP);
        canvas.restore();
        return rect.bottom;
    }

    private float s1(Canvas canvas, float b1, float scaleY) {
        int y = getHeight();
        int x = getWidth();
        canvas.save();
        //
        Paint textP = new Paint();
        textP.setAntiAlias(true);
        textP.setTextSize(textSize);
        textP.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textP.setTextAlign(Paint.Align.CENTER);
        textP.setColor(0xff333333);
        //
        Paint.FontMetrics fontMetrics = textP.getFontMetrics();
        //基线到字体上边框的距离,
        float top = fontMetrics.top;
        //基线到字体下边框的距离
        float bottom = fontMetrics.bottom;
        float textHeight = bottom - top;
        //适合的基线
        float baseLineY = b1 - bottom;
        Rect rect = new Rect(0, (int) (b1 - textHeight), x, (int) b1);
        canvas.clipRect(rect);
        canvas.drawColor(Color.RED);
        //缩放
        // canvas.scale(1.0F, 0.8f);
        canvas.scale(1.0F, scaleY, rect.centerX(), rect.centerY());
        //移动到中间
        // canvas.translate( rect.centerX(), rect.centerY());
        d("s1", "centerX:" + rect.centerX() + " centerY:" + rect.centerY());
        //画基线
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        d("s1", "baseLineY:" + baseLineY);
        // baseLineY += textHeight * (1 - 0.8f)+20;
        d("s1", "baseLineY:" + textHeight * (1 - 0.8f) + " centerY:" + rect.centerY());
        canvas.drawLine(0, baseLineY, x, baseLineY, paint);
        //
        canvas.drawText("我爱你", x / 2, baseLineY, textP);
        canvas.restore();
        return rect.top;
    }

    private float[] s(Canvas canvas) {
        canvas.save();
        int y = getHeight();
        int x = getWidth();
        Paint textP = new Paint();
        textP.setAntiAlias(true);
        textP.setTextSize(textSize);
        textP.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textP.setTextAlign(Paint.Align.CENTER);
        textP.setColor(0xff333333);
        //
        Paint.FontMetrics fontMetrics = textP.getFontMetrics();
        //基线到字体上边框的距离,
        float top = fontMetrics.top;
        //基线到字体下边框的距离
        float bottom = fontMetrics.bottom;
        float textHeight = bottom - top;
        //文字离顶部的距离
        float distance = (y - (bottom - top)) / 2;
        //适合的基线
        float baseLineY = distance - top;
        d("s", "x:" + x + " y:" + y + " top:" + top + " bottom:" + bottom + " z:" + baseLineY + " sin:" + (float) Math.sin(50));
        //画基线
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        //
        canvas.drawLine(0, baseLineY, x, baseLineY, paint);
        canvas.drawLine(0, distance, x, distance, paint);
        //
        Rect rect = new Rect(0, (int) distance, x, (int) (distance + textHeight));
        canvas.clipRect(rect);
        canvas.drawColor(Color.GREEN);
        //
        canvas.drawText("我爱你", x / 2, baseLineY, textP);
        canvas.restore();
        return new float[]{rect.top, rect.bottom};
    }

    void d(String tag, String value) {
        Log.e(tag, value);
    }
}
