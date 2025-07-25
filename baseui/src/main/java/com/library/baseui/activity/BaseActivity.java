package com.library.baseui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.library.baseui.key.KeyboardManager2;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/7.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHandler();
    }

    //====================Intent传值================================
    private Intent it;
    private Bundle bundle;

    protected void refreshIntent(Intent it) {
        this.it = it;
        bundle = it.getExtras();
    }

    //arg0,   arg1
    protected String getStringExtra(String key) {
        if (it == null) {
            it = getIntent();
        }
        return it.getStringExtra(key);
    }

    protected Serializable getObjectExtra(String key) {
        if (it == null) {
            it = getIntent();
        }
        if (bundle == null) {
            bundle = it.getExtras();
        }
        if (bundle == null) {
            return null;
        }
        Serializable bean = bundle.getSerializable(key);
        return bean;
    }

    //====================================输入监听=======================================
    public class TextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            BaseActivity.this.beforeTextChanged(s, start, count, after);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            BaseActivity.this.onTextChanged(s, start, before, count);

        }

        @Override
        public void afterTextChanged(Editable s) {
            BaseActivity.this.afterTextChanged(s);
        }
    }


    protected void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    protected void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    protected void afterTextChanged(Editable s) {

    }

    //==============================设置输入法上的按钮和事件监听=======================
    public class EditorActionListener implements TextView.OnEditorActionListener {
        public EditorActionListener(EditText chatEt) {
            chatEt.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            chatEt.setImeOptions(EditorInfo.IME_ACTION_SEND);
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean tag = BaseActivity.this.onEditorAction(v, actionId, event);
            return tag;
        }
    }

    protected boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    //==============================Handler====================
    private Handler uiHandler;

    protected Handler getHandler() {
        if (uiHandler == null) {
            initHandler();
        }
        return uiHandler;
    }

    private void initHandler() {
        uiHandler = new UiHandler();
    }

    class UiHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseActivity.this.handleMessage(msg);
        }
    }

    protected void handleMessage(Message msg) {

    }

    //===============键盘高度监听相关======================
    private KeyboardManager2 keyboardManager2;

    public KeyboardManager2 getKeyboardManager() {
        if (keyboardManager2 == null) {
            keyboardManager2 = new KeyboardManager2(this);
            keyboardManager2.setOnMultiWindowMode(new OnMultiWindowBack());
        }
        return keyboardManager2;
    }

    //true 分屏模式
    protected boolean isInMultiWindowMode;

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        this.isInMultiWindowMode = isInMultiWindowMode;
    }

    class OnMultiWindowBack implements KeyboardManager2.OnMultiWindowMode {

        @Override
        public boolean isInMultiWindowMode() {
            return isInMultiWindowMode;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (keyboardManager2 != null) {
            keyboardManager2.dismiss();
        }

    }
}
