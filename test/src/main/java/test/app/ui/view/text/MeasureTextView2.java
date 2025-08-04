package test.app.ui.view.text;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class MeasureTextView2 extends AppCompatTextView {
    public MeasureTextView2(Context context) {
        super(context);
        init();
    }

    public MeasureTextView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeasureTextView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setMovementMethod(LinkMovementMethod.getInstance()); // 允许点击链接等
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Layout layout = getLayout();
        if (layout != null) {
            int lineCount = layout.getLineCount();
            tempWidth = 0;
            if (lineCount > 0) {
                tempWidth = (int) layout.getLineWidth(lineCount - 1);
                if (onTextChange != null) {
                    if (lineCount > getMaxLines()) {
                        tempWidth = -1;
                    }
                    onTextChange.onChangeComplete(tempWidth);
                }
            }
        }
    }

    //计算出的字体宽度
    private float tempWidth;

    private OnTextChange onTextChange;

    public void setOnTextChange(OnTextChange onTextChange) {
        this.onTextChange = onTextChange;
    }


    public void setMsg(String msg) {
        setText(msg);
    }

    interface OnTextChange {
        void onChangeComplete(float textWidth);
    }
}