package test.app.ui.activity.test;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;




import com.app.ui.adapter.test.TestChatAdapter;

import com.images.config.entity.ImageEntity;
import com.library.baseui.utile.app.ActivityUtile;
import com.library.baseui.utile.media.MediaPlayerManager;
import com.library.baseui.utile.toast.ToastUtile;
import com.list.library.able.OnLoadingListener;
import com.list.library.view.BaseListView;
import com.list.library.view.refresh.head.RefreshCustomList;

import java.io.File;
import java.util.List;

import test.app.test.ChatMessageTest;
import test.app.ui.activity.MainActivity;
import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.view.key.ChatKeyboardLayout;
import test.app.utiles.other.Constant;
import test.app.utiles.other.DLog;
import test.app.utiles.photo.ImageSelectManager;


//测试会话
public class TestChatActivity extends NormalActionBar {


    RefreshCustomList chatLv;

    ChatKeyboardLayout chatKeyboardLayout;

    private TestChatAdapter adapter;
    private ImageSelectManager photoManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chat, false);

        setBarColor();
        setBarTvText(1, "会话");
        //
        chatKeyboardLayout = findViewById(R.id.chat_keyboard_layout);
        chatLv = findViewById(R.id.list_lv);
        //
        TextView tv = new TextView(this);
        tv.setHeight(100);
        tv.setBackgroundColor(0xffffffff);
        chatLv.addFooterView(tv);
        //
        chatKeyboardLayout.setViewInit(this, findViewById(R.id.chat_popup_in));
        chatKeyboardLayout.setOnKeyboardListener(new KeyboardListener());
        //
        photoManager = new ImageSelectManager(this);
        adapter = new TestChatAdapter(this, photoManager);
        adapter.setHeads("", R.mipmap.default_head_pat_man, "", R.mipmap.default_head_doc);
        chatLv.setAdapter(adapter);
        chatLv.setHeadType(2);
        chatLv.setOnLoadingListener(new LoadingListener());
        //
        onNewIntent(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    public void onBackPressed() {
        if (chatKeyboardLayout.onBackPressed()) {
            return;
        }
        if (isTaskRoot()) {
            ActivityUtile.startActivityCommon(MainActivity.class);
            finish();
            return;
        }
        finish();
    }

    private void setSelectLast() {
        chatLv.setSelection(adapter.getCount());
    }


    //发送消息
    public void sedMsg(ChatMessageTest beanMsg) {
        //重新发送图片
        String type = beanMsg.getMsgType();
        if (Constant.MSG_TYPE_PIC.equals(type) && beanMsg.is7NError) {
            //图片重新发送
            return;
        }
        if (Constant.MSG_TYPE_TEXT.equals(type)) {
            //文本发送
            return;
        }
        if (Constant.MSG_TYPE_PIC.equals(type)) {
            //图片 发送
            return;
        }
        if (Constant.MSG_TYPE_AUDIO.equals(type) && beanMsg.is7NError) {
            //语音 重新发送
            return;
        }
        if (Constant.MSG_TYPE_AUDIO.equals(type)) {
            //语音发送
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ImageEntity> images = photoManager.onActivityResult(requestCode, resultCode, data);
        if (images == null || images.size() == 0) {
            return;
        }
        for (int i = 0; i < images.size(); i++) {
            String imagePath = images.get(i).imagePath;
            File file = new File(imagePath);
            if (!file.exists()) {
                DLog.e("照片不存在", "" + imagePath);
                continue;
            }
            ChatMessageTest beanMsg = adapter.getSendMsg(Constant.MSG_TYPE_PIC, imagePath, 0);
            adapter.setAddData(beanMsg);
            //上传照片
        }
        setSelectLast();
    }


    class LoadingListener implements OnLoadingListener {
        @Override
        public void onLoading(boolean isRefresh) {
            //刷新加载历史消息
        }
    }

    //键盘监听
    class KeyboardListener implements ChatKeyboardLayout.OnKeyboardListener {

        @Override
        public void onOther(int type) {
            //1:发送图片 2:发送图片-拍照
            switch (type) {
                case 1:
                    //图片
                    photoManager.getMoreConfig(1, null);
                    break;
                case 2:
                    //拍照
                    photoManager.getSinglePhotoConfig();
                    break;

            }
        }

        @Override
        public void onSendMsg(String msg) {
            if (TextUtils.isEmpty(msg)) {
                ToastUtile.showToast("发送信息不能为空");
                return;
            }
            ChatMessageTest beanMsg = adapter.getSendMsg(Constant.MSG_TYPE_TEXT, msg, 0);
            adapter.setAddData(beanMsg);
            setSelectLast();
            sedMsg(beanMsg);
        }

        @Override
        public void onVisibilityChanged(boolean isOpen, int keyboardHeight) {
            if (!isOpen) {
                return;
            }
            setSelectLast();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void onCompleteSound(String path, int length) {
            if (TextUtils.isEmpty(path)) {
                return;
            }
            File file = new File(path);
            if (!file.exists()) {
                return;
            }
            //录音完成
            ChatMessageTest beanMsg = adapter.getSendMsg(Constant.MSG_TYPE_AUDIO, path, length);
            adapter.setAddData(beanMsg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.getInstance().setMediaWork(3);
        MediaPlayerManager.getInstance().onDestroy();
    }
}
