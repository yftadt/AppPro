package test.app.ui.view.down;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by 郭敏 on 2018/5/3 0003.
 */

public class CodeEditText extends EditText {

    public CodeEditText(Context context) {
        super(context);
        init();
    }

    public CodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CodeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        String content = getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        int length = content.length();
        if (selEnd == length) {
            return;
        }
        setSelection(length);
    }


}
