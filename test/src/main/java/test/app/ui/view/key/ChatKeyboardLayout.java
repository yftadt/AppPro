package test.app.ui.view.key;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.library.baseui.utile.other.KeyboardUtile;

import test.app.ui.activity.R;
import test.app.ui.manager.KeyboardManager;
import test.app.ui.view.RecordTextView;
import test.app.utiles.other.DLog;

/**
 * 会话键盘
 * Created by Administrator on 2017/9/1.
 */

public class ChatKeyboardLayout extends LinearLayout implements View.OnClickListener {
    //发送
    TextView chatSendTv;
    //编辑文字
    EditText chatEt;
    //更多（照相，照片）
    LinearLayout moreLl;
    //聊天类型（文字 和 语音）
    ImageView chatTypeIv;
    //录音
    RecordTextView chatRecordTv;
    private String tag = "ChatKeyboardLayout";

    private KeyboardManager keyboardManager;

    public ChatKeyboardLayout(Context context) {
        super(context);
        init();
    }

    public ChatKeyboardLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatKeyboardLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_merge_chat_keyboard, this);
        initView(view);
        moreLl.setVisibility(View.GONE);
        chatEt.addTextChangedListener(new TextChangeListener());
        chatRecordTv.setOnCompleteSoundListener(new OnSoundListener());
    }

    private void initView(View view) {
        //发送
        chatSendTv = (TextView) view.findViewById(R.id.chat_send_tv);
        chatSendTv.setOnClickListener(this);
        //编辑文字
        chatEt = (EditText) view.findViewById(R.id.chat_et);
        //更多（照相，照片）
        moreLl = (LinearLayout) view.findViewById(R.id.more_ll);
        //聊天类型（文字 和 语音）
        chatTypeIv = (ImageView) view.findViewById(R.id.chat_type_iv);
        chatTypeIv.setOnClickListener(this);
        //录音
        chatRecordTv = (RecordTextView) view.findViewById(R.id.chat_record_tv);
        //
        view.findViewById(R.id.chat_send_camera_tv).setOnClickListener(this);
        view.findViewById(R.id.chat_send_iamge_tv).setOnClickListener(this);
        view.findViewById(R.id.chat_more_iv).setOnClickListener(this);
    }

    /**
     * @param activity
     * @param recordShowView 录音时显示的view
     */
    public void setViewInit(Activity activity, View recordShowView) {
        setKeyboardListener(activity);
        chatRecordTv.setView(recordShowView, activity);
    }

    //输入法弹起监听
    private void setKeyboardListener(Activity activity) {
        keyboardManager = new KeyboardManager();
        keyboardManager.setEventListener(activity);
        keyboardManager.setOnKeyboardStateListener(new BoardListener());
    }

    private int chatType = 1;


    public boolean onBackPressed() {
        if (moreLl.getVisibility() != View.VISIBLE) {
            return false;
        }
        //收回键盘
        moreShow = false;
        moreLl.setVisibility(View.GONE);
        return true;
    }

    //更多是否显示
    boolean moreShow;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.chat_type_iv) {
            //显示会话或者语音
            chatType += 1;
            int icon = chatType % 2 == 0 ? R.mipmap.chat_keyboard : R.mipmap.chat_record;
            int isRecord = chatType % 2 == 0 ? View.VISIBLE : View.GONE;
            int isEdit = chatType % 2 == 0 ? View.GONE : View.VISIBLE;
            chatEt.setVisibility(isEdit);
            chatTypeIv.setImageResource(icon);
            chatRecordTv.setVisibility(isRecord);
            KeyboardUtile.hideKeyBoard(getContext(), chatEt);
            return;
        }
        if (id == R.id.chat_et) {
            //
            moreLl.setVisibility(View.GONE);
            return;
        }
        if (id == R.id.chat_send_camera_tv) {
            //拍照
            if (onKeyboardListener == null) {
                return;
            }
            onKeyboardListener.onOther(2);
            return;
        }
        if (id == R.id.chat_send_iamge_tv) {
            //图片
            if (onKeyboardListener == null) {
                return;
            }
            onKeyboardListener.onOther(1);
            return;
        }
        if (id == R.id.chat_more_iv) {
            //显示或隐藏更多
            boolean keyboardState = keyboardManager.getKeyboardState();
            if (keyboardState) {
                moreShow = true;
                KeyboardUtile.hideKeyBoard(getContext(), chatEt);
                return;
            }
            moreShow = !moreShow;
            int more = moreShow ? View.VISIBLE : View.GONE;
            moreLl.setVisibility(more);
            return;
        }
        if (id == R.id.chat_send_tv) {
            //发送消息
            DLog.e(tag, "发送消息");
            if (onKeyboardListener == null) {
                return;
            }
            onKeyboardListener.onSendMsg(chatEt.getText().toString().trim());
            chatEt.setText("");
            return;
        }

    }

    //键盘监听
    class BoardListener implements KeyboardManager.OnKeyboardStateListener {

        @Override
        public void onVisibilityChanged(boolean isOpen, int keyboardHeight) {
            if (isOpen) {
                moreLl.setVisibility(View.GONE);
            }
            if (!isOpen) {
                int more = moreShow ? View.VISIBLE : View.GONE;
                moreLl.setVisibility(more);
            }
            if (onKeyboardListener == null) {
                return;
            }
            onKeyboardListener.onVisibilityChanged(isOpen, keyboardHeight);
        }
    }


    //输入监听
    class TextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int sendShow = TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE;
            chatSendTv.setVisibility(sendShow);
            if (onKeyboardListener == null) {
                return;
            }
            onKeyboardListener.onTextChanged(s, start, before, count);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    //录音完成监听
    class OnSoundListener implements RecordTextView.OnCompleteSoundListener {

        @Override
        public void onCompleteSound(String path, double length) {
            //录音完成
            if (onKeyboardListener == null) {
                return;
            }
            onKeyboardListener.onCompleteSound(path, (int) length);
        }
    }

    private OnKeyboardListener onKeyboardListener;

    public void setOnKeyboardListener(OnKeyboardListener onKeyboardListener) {
        this.onKeyboardListener = onKeyboardListener;
    }

    public interface OnKeyboardListener {
        //1:发送图片 2:发送图片-拍照 3:发送文章
        void onOther(int type);

        //发送消息
        void onSendMsg(String msg);

        //键盘弹起监听
        void onVisibilityChanged(boolean isOpen, int keyboardHeight);

        //输入框变化
        void onTextChanged(CharSequence s, int start, int before, int count);

        //录音完成
        void onCompleteSound(String path, int length);

    }
}
