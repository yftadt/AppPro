package test.app.ui.view.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

/**
 * 文字轮播
 * Created by 郭敏 on 2018/5/21 0021.
 */

public class TextBannerView extends View {
    //viwe 的宽度 和 高度
    private int w, h;
    //item 间距（上边距）
    private int textSpace = 10;
    //内容字体大小
    private int textSize = 30;
    //内容字体高度
    private int textHeight;
    //内容文字基线
    private float baseLineY;
    private Paint textP, tagP;
    String[] titles = new String[]{"1.发光飞碟规范梵蒂冈", "2.单打独斗付所付多",
            "3.发个梵蒂冈梵蒂冈的鬼地方个", "4.发个梵蒂冈梵蒂冈的鬼地方个"};
    String[] tag = new String[]{"热门", "直播", "热门", "直播"};
    private HandlerBanner handlerBanner;
    private int moveY = 0;

    private boolean isStop;

    public TextBannerView(Context context) {
        super(context);
        initData();
    }

    public TextBannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public TextBannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public void initData() {
        handlerBanner = new HandlerBanner();
        //文字
        textP = new Paint();
        textP.setAntiAlias(true);
        textP.setTextSize(textSize);
        textP.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textP.setTextAlign(Paint.Align.LEFT);
        textP.setColor(0xff333333);
        //tag边框
        tagP = new Paint();
        tagP.setAntiAlias(true);
        tagP.setColor(0xff333333);
        tagP.setStyle(Paint.Style.STROKE);
        //
        Paint.FontMetrics fontMetrics = textP.getFontMetrics();
        baseLineY = -fontMetrics.top + (textSpace / 2);
        textHeight = (int) (fontMetrics.bottom - fontMetrics.top) + textSpace;
        //
        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams lp = getLayoutParams();
                if (lp == null) {
                    lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            textHeight * 2);
                } else {
                    lp.height = textHeight * 2;
                }
                setLayoutParams(lp);
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (w == 0) {
            w = getWidth();
            h = getHeight();
        }
        drawItem1(canvas);
        handlerBanner.sendEmptyMessageDelayed(1, 0);
    }

    private int titleCount;

    private void drawItem1(Canvas canvas) {
        //画的item个数
        int items = 0;
        //内容
        int titleIndex = titleCount;
        //可以画item的个数
        int itemSize = (h / textHeight);
        itemSize += 1;
        if (textHeight % h > textHeight / 2) {
            itemSize += 1;
        }
        //下一个item的高度(大于0，表示可以画下一个item)
        int nextItemHeight = h - (textHeight - moveY);
        Log.e("--------", "itemSize:" + itemSize + " moveY:" + moveY);
        while (nextItemHeight > 0) {
            titleIndex = titleIndex % titles.length;
            int itemIndex = items % itemSize;
            //标签框
            RectF rectF = new RectF();
            rectF.left = 5;
            rectF.right = 70;
            rectF.top = itemIndex * textHeight - moveY + textSpace / 2;
            rectF.bottom = textHeight + (items * textHeight) - moveY - textSpace / 2;
            canvas.drawRoundRect(rectF, 5, 5, tagP);
            //标签
            String tagContext = tag[titleIndex];
            if (tagContext == null) {
                tagContext = "";
            }
            canvas.drawText(tagContext, 10, (itemIndex * textHeight + baseLineY) - moveY, textP);
            //内容
            String title=titles[titleIndex];
            if(title==null){
                title="";
            }
            canvas.drawText(title, 90, (itemIndex * textHeight + baseLineY) - moveY, textP);
            items += 1;
            titleIndex += 1;
            nextItemHeight = h - (items * textHeight - moveY);
        }

    }


    class HandlerBanner extends Handler {
        //移动完整的文本数量
        private int moveFullTextSize;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isStop) {
                return;
            }
            switch (msg.what) {
                case 1:
                    if (moveY == textHeight) {
                        moveY = 0;
                        titleCount += 1;
                        moveFullTextSize += 1;
                    }
                    if (moveFullTextSize > 0 && moveY == 0 && moveFullTextSize % 2 == 0) {
                        sendEmptyMessageDelayed(2, 3 * 1000);
                        break;
                    }
                case 2:
                    moveY += 1;
                    if (moveY > textHeight) {
                        moveY = textHeight;
                    }
                    invalidate();
                    break;
            }

        }
    }
}
