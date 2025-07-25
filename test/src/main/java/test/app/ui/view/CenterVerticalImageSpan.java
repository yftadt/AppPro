package test.app.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * 图文混排 图片居中
 * 用法：
 *val spannableString = SpannableString(msg)
 *         val defaultSpan: ImageSpan = CenterVerticalImageSpan(context, icon)
 *
 *         spannableString.setSpan(
 *             defaultSpan,
 *             0,
 *             1,
 *             Spanned.SPAN_INCLUSIVE_EXCLUSIVE
 *         );
 *  text =spannableString
 */
public class CenterVerticalImageSpan extends ImageSpan {

    public CenterVerticalImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public CenterVerticalImageSpan(Drawable drawable) {
        super(drawable);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fontMetricsInt) {
        try {
            Drawable drawable = getDrawable();
            //不需要自适应时，只需外部传入宽高(realWidth, realHeight)，直接设置给drawable
            //((Drawable) drawable).setBounds(0, 0, realWidth, realHeight);
            Rect rect = drawable.getBounds();
            if (fontMetricsInt != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                //对于这里我表示,我不知道为啥是这样。不应该是fontHeight/2?但是只有fontHeight/4才能对齐
                //难道是因为TextView的draw的时候top和bottom是大于实际的？具体请看下图
                //所以fontHeight/4是去除偏差?
                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fontMetricsInt.ascent = -bottom;
                fontMetricsInt.top = -bottom;
                fontMetricsInt.bottom = top;
                fontMetricsInt.descent = top;
            }
            return rect.right;
        } catch (Exception e) {
            return 20;
        }
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        try {
            Drawable drawable = getDrawable();
            canvas.save();
            int transY;
            //获得将要显示的文本高度-图片高度除2等居中位置+top(换行情况)
            transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        } catch (Exception e) {
        }
    }

}
