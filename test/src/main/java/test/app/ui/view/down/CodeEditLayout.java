package test.app.ui.view.down;

import android.content.Context;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;


import com.library.baseui.utile.app.APKInfo;
import com.library.baseui.utile.other.KeyboardUtile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭敏 on 2018/5/4 0004.
 */

public class CodeEditLayout extends LinearLayout {
    //true 获取到焦点
    private boolean isFocus;
    private String code = "";
    private int codeLength = 4;
    private int indexEt;
    private List<EditText> codesEt = new ArrayList<>();

    public CodeEditLayout(Context context) {
        super(context);
        init();
    }

    public CodeEditLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CodeEditLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < codeLength; i++) {
            View view = getView();
            if (i > 0) {
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
                lp.leftMargin = (int) APKInfo.getInstance().getDIPTOPX(38);
            }
            addView(view);
        }
        setOnClickListener(new OnClick());
    }

    private View getView() {
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);

        int w = (int) APKInfo.getInstance().getDIPTOPX(38);
        int h = (int) APKInfo.getInstance().getDIPTOPX(60);
        CodeEditText et = getEt(w, h);
        codesEt.add(et);
        ll.addView(et);
        //
        LayoutParams lp = new LayoutParams(w, 1);
        View lineView = new View(getContext());
        lineView.setBackgroundColor(0xff999999);
        lineView.setLayoutParams(lp);
        ll.addView(lineView);
        //
        return ll;
    }

    private CodeEditText getEt(int w, int h) {
        CodeEditText et = new CodeEditText(getContext());
        LayoutParams lp = new LayoutParams(w, h);
        et.setLayoutParams(lp);
        et.setMaxLines(1);
        InputFilter[] filters = {new InputFilter.LengthFilter(1)};
        et.setFilters(filters);
        et.setTextColor(0xff333333);
        et.setGravity(Gravity.CENTER);
        et.setInputType(InputType.TYPE_CLASS_PHONE);
        et.setTextSize(16);
        et.setBackgroundColor(0x00ffffff);
        et.addTextChangedListener(new OnTextWatcher());
        et.setOnFocusChangeListener(new OnFocusChange());
        et.setCursorVisible(false);
        return et;
    }

    //设置显示光标的输入框
    private void setCompile() {
        int index = code.length();
        if (index == codeLength) {
            index -= 1;
        }
        for (int i = 0; i < codesEt.size(); i++) {
            codesEt.get(i).setCursorVisible(false);
        }
        if (isFocus && indexEt == index) {
            codesEt.get(index).setCursorVisible(true);
            codesEt.get(index).requestFocus();
            KeyboardUtile.showKeyBoard(getContext(),codesEt.get(index));
            return;
        }
        indexEt = index;
        codesEt.get(index).requestFocus();
        codesEt.get(index).setCursorVisible(true);
    }

    //删除验证码
    private void deleteCode() {
        int codeLength = code.length();
        if (codeLength == 1) {
            code = "";
        }
        if (codeLength > 1) {
            code = code.substring(0, (code.length() - 1));
        }
        if (onCodeChangListener != null) {
            onCodeChangListener.onCodeChang(code);
        }
        setCompile();
    }

    public boolean getIsFocus() {
        return isFocus;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int key = event.getKeyCode();
        if (isFocus &&
                event.getAction() != KeyEvent.ACTION_UP && key == KeyEvent.KEYCODE_DEL) {
            deleteCode();
        }
        return super.dispatchKeyEvent(event);
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            setCompile();
        }
    }

    class OnFocusChange implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isFocus = hasFocus;
            if (hasFocus) {
                setCompile();
            }
        }
    }

    class OnTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String c = s.toString();
            if (TextUtils.isEmpty(c)) {
                return;
            }
            code += c;
            if (onCodeChangListener != null) {
                onCodeChangListener.onCodeChang(code);
            }
            int length = code.length();
            if (codeLength == length) {
                KeyboardUtile.hideKeyBoard(getContext(), codesEt.get(3));
                return;
            }
            setCompile();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    private OnCodeChangListener onCodeChangListener;

    public void setOnCodeChangListener(OnCodeChangListener onCodeChangListener) {
        this.onCodeChangListener = onCodeChangListener;
    }

    public interface OnCodeChangListener {
        void onCodeChang(String codeNew);
    }
}
