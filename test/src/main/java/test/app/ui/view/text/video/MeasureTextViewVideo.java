package test.app.ui.view.text.video;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class MeasureTextViewVideo extends AppCompatTextView {
    public MeasureTextViewVideo(Context context) {
        super(context);
        init();
    }

    public MeasureTextViewVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeasureTextViewVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setMovementMethod(LinkMovementMethod.getInstance()); // 允许点击链接等
    }

    private int lines = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Layout layout = getLayout();
        if (layout != null) {
            int lineCount = layout.getLineCount();
            lines = lineCount;
            if (lineCount > 0) {
                int lastLine = lineCount - 1;
                float tempWidth = layout.getLineWidth(lastLine);
                int lastLineStart = layout.getLineStart(lastLine);
                int lastLineEnd = layout.getLineEnd(lastLine);
                String lastLineText = getText().toString().substring(lastLineStart, lastLineEnd);
                if (onTextChange != null) {
                    onTextChange.onChangeComplete(lastLineText, (lineCount - 1), tempWidth);
                }
            }
        }
    }

    public int getLines() {
        return lines;
    }

    public String getLineText(int lineIndex) {
        Layout layout = getLayout();
        int lineCount = layout.getLineCount();
        if (lineIndex >= lineCount) {
            return "";
        }
        int lastLineStart = layout.getLineStart(lineIndex);
        int lastLineEnd = layout.getLineEnd(lineIndex);
        String lastLineText = getText().toString().substring(lastLineStart, lastLineEnd);
        return lastLineText;
    }

    public float getLineWidth(int lineIndex) {
        Layout layout = getLayout();
        int lineCount = layout.getLineCount();
        if (lineIndex >= lineCount) {
            return 0;
        }
        float tempWidth = layout.getLineWidth(lineIndex);
        return tempWidth;
    }

    public float getStrWidth(String str) {
        TextPaint paint = getPaint();
        return paint.measureText(str);
    }

    private OnTextChangeListener onTextChange;

    public void setOnTextChange(OnTextChangeListener onTextChange) {
        this.onTextChange = onTextChange;
    }


    public void setMsg(String msg) {
        setText(msg);
    }

    public void setMsg(SpannableString msg) {
        setText(msg);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

}