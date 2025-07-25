package com.app.ui.adapter.test;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.library.baseui.utile.img.ImageLoadingUtile;
import com.library.baseui.utile.media.MediaPlayerManager;
import com.library.baseui.utile.time.DateUtile;
import com.library.baseui.utile.toast.ToastUtile;
import com.list.library.adapter.list.AbstractListAdapter;


import java.util.ArrayList;
import java.util.Date;

import test.app.test.ChatMessageTest;
import test.app.test.ChatSysAttachmentTest;
import test.app.ui.activity.R;
import test.app.ui.activity.test.TestChatActivity;
import test.app.utiles.other.Constant;
import test.app.utiles.other.DLog;
import test.app.utiles.photo.ImageSelectManager;


/**
 * 聊天list适配
 * Created by Administrator on 2016/9/21.
 */
public class TestChatAdapter extends AbstractListAdapter<ChatMessageTest> {
    private TestChatActivity activity;
    private String patHead, docHead;
    private int patDefaultIcon, docDefaultIcon;
    private ImageSelectManager imageSelectManager;

    public TestChatAdapter(TestChatActivity activity, ImageSelectManager imageSelectManager) {
        this.activity = activity;
        this.imageSelectManager = imageSelectManager;
        MediaPlayerManager.getInstance().setAddOnMediaListener(new MediaListener());
    }

    public void setHeads(String patHead, int patDefaultIcon, String docHead, int docDefaultIcon) {
        this.patHead = patHead;
        this.patDefaultIcon = patDefaultIcon;
        this.docHead = docHead;
        this.docDefaultIcon = docDefaultIcon;
    }

    //构造消息体
    public ChatMessageTest getSendMsg(String msgType, String msg, int msgLength) {
        ChatMessageTest userMsg = new ChatMessageTest();
        if (Constant.MSG_TYPE_TEXT.equals(msgType)) {
            userMsg.replyContent = msg;
        }
        if (Constant.MSG_TYPE_PIC.equals(msgType)) {
            userMsg.localityPath = msg;
        }
        if (Constant.MSG_TYPE_AUDIO.equals(msgType)) {
            userMsg.localityPath = msg;
        }
        userMsg.msgType = msgType;
        userMsg.replierType = "PAT";
        userMsg.replyTime = new Date();
        userMsg.sendType = 1;
        userMsg.sendId = String.valueOf(userMsg.replyTime.getTime());
        return userMsg;
    }

    public void setMsgSendState(String sendId, int sendState) {
        if (list.size() == 0) {
            return;
        }
        for (int i = (list.size() - 1); i >= 0; i--) {
            ChatMessageTest beanMsg = list.get(i);
            if (sendId.equals(beanMsg.sendId)) {
                beanMsg.sendType = sendState;
                break;
            }
        }
        notifyDataSetChanged();
    }

    //上传图片失败
    public void setUploadImageFailed(String sendId) {
        ChatMessageTest bean = getBean(sendId);
        if (bean == null) {
            return;
        }
        bean.is7NError = true;
        bean.sendType = 2;
        notifyDataSetChanged();
    }

    //上传图片成功
    public void setUploadImageOk(String sendId, String url) {
        ChatMessageTest bean = getBean(sendId);
        if (bean == null) {
            return;
        }
        ChatSysAttachmentTest sysAttachment = new ChatSysAttachmentTest();
        sysAttachment.url = url;
        bean.addImage(sysAttachment);
        notifyDataSetChanged();
    }

    //获取item
    public ChatMessageTest getBean(String sendId) {
        ChatMessageTest resultBean = null;
        int size = list.size();
        for (int i = (size - 1); i > 0; i--) {
            ChatMessageTest bean = list.get(i);
            if (sendId.equals(bean.sendId)) {
                resultBean = bean;
                break;
            }
        }
        return resultBean;
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_chat, null);
            // 左边
            holder.views[0] = convertView.findViewById(R.id.item_chat_msg_left_il);
            holder.timesTv[0] = (TextView) convertView.findViewById(R.id.item_left_send_time_tv);
            holder.heandsIv[0] = (ImageView) convertView.findViewById(R.id.item_left_hend_iv);
            holder.msgsTv[0] = (TextView) convertView.findViewById(R.id.item_left_msg_tv);
            holder.imagesIv[0] = (ImageView) convertView.findViewById(R.id.item_left_msg_iv);
            holder.voicesIv[0] = (ImageView) convertView.findViewById(R.id.item_left_voice_tv);

            // 右边
            holder.views[1] = convertView.findViewById(R.id.item_chat_msg_right_il);
            holder.sendStateView = (RelativeLayout) convertView.findViewById(R.id.item_right_progress_rl);
            holder.sendRestTv = (TextView) convertView.findViewById(R.id.item_right_send_fail_tv);
            holder.sendProgressBar = (ProgressBar) convertView.findViewById(R.id.item_send_pb);
            holder.timesTv[1] = (TextView) convertView.findViewById(R.id.item_right_send_time_tv);
            holder.heandsIv[1] = (ImageView) convertView.findViewById(R.id.item_right_hend_iv);
            holder.msgsTv[1] = (TextView) convertView.findViewById(R.id.item_right_msg_tv);
            holder.imagesIv[1] = (ImageView) convertView.findViewById(R.id.item_right_msg_iv);
            holder.voicesIv[1] = (ImageView) convertView.findViewById(R.id.item_right_voice_tv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChatMessageTest entity = list.get(position);
        boolean isMe = entity.getMsgSenderType();
        int userType = isMe ? 1 : 2;
        holder.setType(userType);
        //头像
        int defaultHead = isMe ? docDefaultIcon : patDefaultIcon;
        String urlHead = isMe ? docHead : patHead;
        ImageLoadingUtile.loadingCircle(activity, urlHead, defaultHead, holder.getHeadView());
        holder.heandsIv[0].setOnClickListener(new OnClick(position));
        // 消息发送 时间
        holder.getTimeView().setText(DateUtile.getDateFormat(entity.replyTime, DateUtile.DATA_FORMAT_YMD_24HMS));
        if (position == 0) {
            holder.getTimeView().setVisibility(View.VISIBLE);
        }
        if (position > 0) {
            Date uptime = list.get(position - 1).replyTime;
            long endTime = entity.replyTime.getTime() - uptime.getTime();
            int show = endTime < 1000 * 60 * 5 ? View.GONE : View.VISIBLE;
            holder.getTimeView().setVisibility(show);
        }
        String type = entity.getMsgType();
        //文本
        if (Constant.MSG_TYPE_TEXT.equals(type)) {
            Spanned spanned = Html.fromHtml(entity.replyContent);
            TextView msgTv = holder.getMsgView();
            msgTv.setText(spanned);
            msgTv.setOnLongClickListener(new OnClick(position));
            //
            msgTv.setVisibility(View.VISIBLE);
            holder.getVoiceView().setVisibility(View.GONE);
            holder.getImageView().setVisibility(View.GONE);
        }

        if (Constant.MSG_TYPE_PIC.equals(type)) {
            ImageView msgIv = holder.getImageView();
            String url = TextUtils.isEmpty(entity.localityPath) ? entity.replyContent : entity.localityPath;
            ImageLoadingUtile.loadImageChat(activity, url, R.mipmap.default_image, msgIv, holder.getType());
            msgIv.setOnClickListener(new OnClick(position));
            //
            msgIv.setVisibility(View.VISIBLE);
            holder.getVoiceView().setVisibility(View.GONE);
            holder.getMsgView().setVisibility(View.GONE);
        }
        if (Constant.MSG_TYPE_AUDIO.equals(type)) {
            ImageView voiceIv = holder.getVoiceView();
            voiceIv.setOnClickListener(new OnClick(position, voiceIv));
            //
            voiceIv.setVisibility(View.VISIBLE);
            holder.getMsgView().setVisibility(View.GONE);
            holder.getImageView().setVisibility(View.GONE);
        }
        // 发送状态
        if (entity.sendType == 0) {
            holder.sendStateView.setVisibility(View.INVISIBLE);
        }
        if (entity.sendType == 1) {
            holder.sendStateView.setVisibility(View.VISIBLE);
            holder.sendProgressBar.setVisibility(View.VISIBLE);
            holder.sendRestTv.setVisibility(View.GONE);
        }
        if (entity.sendType == 2) {
            holder.sendStateView.setVisibility(View.VISIBLE);
            holder.sendProgressBar.setVisibility(View.GONE);
            holder.sendRestTv.setVisibility(View.VISIBLE);
            holder.sendRestTv.setOnClickListener(new OnClick(position));
        }
        return convertView;
    }

    public class ViewHolder {
        /**
         * 右边视图-发送图片语音进度
         */
        public RelativeLayout sendStateView;
        public ProgressBar sendProgressBar;
        /**
         * 右边视图-发送图片语音失败时-显示
         */
        public TextView sendRestTv;
        /**
         * 左边和右边视图 :0 左边，1右边
         */
        public View[] views = new View[2];
        // 时间
        public TextView[] timesTv = new TextView[2];
        // 文本消息
        public TextView[] msgsTv = new TextView[2];

        //语音
        public ImageView[] voicesIv = new ImageView[2];
        // 头像
        public ImageView[] heandsIv = new ImageView[2];
        // 图片消息
        public ImageView[] imagesIv = new ImageView[2];
        //
        //0是别人发来的，1是我自己
        private int type;

        public void setType(int userType) {
            switch (userType) {
                case 1:
                    //我
                    type = 1;
                    views[0].setVisibility(View.GONE);
                    views[1].setVisibility(View.VISIBLE);
                    break;
                case 2:
                    type = 0;
                    views[0].setVisibility(View.VISIBLE);
                    views[1].setVisibility(View.GONE);
                    break;
            }


        }

        public int getType() {
            return type;
        }

        public TextView getTimeView() {
            return timesTv[type];
        }

        public TextView getMsgView() {
            return msgsTv[type];
        }


        public ImageView getVoiceView() {
            return voicesIv[type];
        }

        public ImageView getHeadView() {
            return heandsIv[type];
        }

        public ImageView getImageView() {
            return imagesIv[type];
        }
    }

    private ImageView playVoiceIv;

    class OnClick implements View.OnClickListener, View.OnLongClickListener {
        private int index;
        private ImageView iv;

        public OnClick(int index) {
            this.index = index;
        }

        public OnClick(int index, ImageView iv) {
            this.index = index;
            this.iv = iv;
        }

        @Override
        public void onClick(View arg0) {
            ChatMessageTest bean = list.get(index);
            int id = arg0.getId();
            if (id == R.id.item_right_send_fail_tv) {
                // 重发
                bean.sendType = 1;
                notifyDataSetChanged();
                activity.sedMsg(bean);
                return;
            }
            if (id == R.id.item_left_msg_iv || id == R.id.item_right_msg_iv) {
                // 图片
                if (!Constant.MSG_TYPE_PIC.equals(bean.getMsgType())) {
                    return;
                }
                //点击了图片
                ArrayList<String> urls = bean.getUrls();
                imageSelectManager.previewImage(urls, 0);
                return;
            }
            if (id == R.id.item_right_voice_tv) {
                //播放在右边的语音
                stopAnimation();
                iv.setImageResource(R.drawable.play_voice_right);
                playVoiceIv = iv;
                // 有的手机一加入动画资源就是动的所以要先停止
                stopAnimation();
                MediaPlayerManager.getInstance().setDataSource(bean.replyContent, bean.localityPath);
                MediaPlayerManager.getInstance().setMediaWork(4);
                return;
            }
            if (id == R.id.item_left_voice_tv) {
                //播放在左边的语音
                stopAnimation();
                iv.setImageResource(R.drawable.play_voice_left);
                playVoiceIv = iv;
                // 有的手机一加入动画资源就是动的所以要先停止
                stopAnimation();
                MediaPlayerManager.getInstance().setDataSource(bean.replyContent, bean.localityPath);
                MediaPlayerManager.getInstance().setMediaWork(4);
                return;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            int id = v.getId();
            if (id == R.id.item_left_msg_tv || id == R.id.item_right_msg_tv) {
                ChatMessageTest userMessageVo = list.get(index);
                String msg = userMessageVo.replyContent.trim();
                if (TextUtils.isEmpty(msg)) {

                } else {
                    ClipboardManager cmb = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(msg);
                    ToastUtile.showToast("已经复制到剪贴板");
                }
            }

            return false;
        }
    }

    //停止动画
    private void stopAnimation() {
        if (playVoiceIv == null) {
            return;
        }
        AnimationDrawable animationDrawable = (AnimationDrawable) playVoiceIv.getDrawable();
        if (!animationDrawable.isRunning()) {
            return;
        }
        animationDrawable.stop();
        animationDrawable.selectDrawable(2);
    }

    private void statrtAnimation() {
        AnimationDrawable animationDrawable = (AnimationDrawable) playVoiceIv.getDrawable();
        animationDrawable.start();
    }

    class MediaListener implements MediaPlayerManager.OnMediaPlayerListener {
        //播放视频大小
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        }

        ;

        //播放缓冲
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
        }

        @Override
        public void onPayState(MediaPlayer mp, int state, String url) {
            switch (state) {
                case 100:
                    statrtAnimation();
                    break;
                case 101:
                    stopAnimation();
                    break;
                case 102:
                    stopAnimation();
                    break;
                case 103:
                    break;
                case 104:
                    break;
            }
        }

        //播放错误
        public void onError(MediaPlayer mp, int what, int extra) {
            stopAnimation();
            DLog.e("监听", "onError");
        }


    }
}

