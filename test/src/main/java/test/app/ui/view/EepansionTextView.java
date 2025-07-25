package test.app.ui.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;



import androidx.annotation.Nullable;

import test.app.utiles.other.DLog;


/**
 * 可以展开的查看更多的msg
 * Created by Administrator on 2017/10/25 0025.
 */

public class EepansionTextView extends LinearLayout implements View.OnClickListener {

    private TextView msgTv;
    private TextView tagTv;

    public EepansionTextView(Context context) {
        super(context);
        init(context);
    }

    public EepansionTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EepansionTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        msgTv = new TextView(context);
        msgTv.setTextSize(14);
        msgTv.setTextColor(0xff666666);
        msgTv.setLineSpacing(8, 1);
        addView(msgTv);
        msgTv.setEllipsize(TextUtils.TruncateAt.END);
        //
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT;
        tagTv = new TextView(context);
        tagTv.setTextSize(14);
        tagTv.setTextColor(0xff576b95);
        tagTv.setOnClickListener(this);
        addView(tagTv, layoutParams);
    }

    //true 展开
    private boolean isEepansion;
    //显示2行
    private int showLine = 2;

    public void setText(String msg) {
        isEepansion = false;
        msgTv.setText(msg);
        tagTv.setVisibility(View.GONE);
        msgTv.post(new Runnable() {
                       @Override
                       public void run() {
                           int line = msgTv.getLineCount();
                           if (line > showLine) {
                               DLog.e("=====", "" + line);
                               onSetText();
                               tagTv.setVisibility(View.VISIBLE);

                           }
                       }
                   }

        );

    }

    private void onSetText() {
        if (isEepansion) {
            tagTv.setText("　　收起");
            msgTv.setMaxLines(Integer.MAX_VALUE);
        }
        if (!isEepansion) {
            tagTv.setText("显示更多");
            msgTv.setMaxLines(showLine);
        }
    }

    @Override
    public void onClick(View v) {
        isEepansion = !isEepansion;
        onSetText();

    }


}