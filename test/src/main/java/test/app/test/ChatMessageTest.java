package test.app.test;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/7.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessageTest {

    /**
     * 回复内容
     */
    public String replyContent;

    /**
     * 回复时间
     */
    public Date replyTime;
    /**
     * 回复人类型 DOC-医生ASS-助理PAT-患者ADM-后台用户
     */
    public String replierType;

    /**
     * 附件
     */
    public List<ChatSysAttachmentTest> attaList;
    /**
     * 回复人名称
     */
    public String replierName;
    /**
     * 回复人名称
     */
    public String replierAvatar;
    //-------------------------------------
    //发送id
    public String sendId;
    // 发送状态-0:发送完成;1:发送中;2:发送失败;
    public int sendType;
    //图片，语音...本地路径
    public String localityPath;
    //上传资源错误
    public boolean is7NError;
    //TXT：文本 IMG：图片 REC：录音
    public String msgType;

    public void addImage(ChatSysAttachmentTest sysAttachment) {
        if (attaList == null) {
            attaList = new ArrayList<>();
        }
        is7NError = false;
        attaList.add(sysAttachment);
    }


    public String getMsgType() {
        return msgType;
    }

    //回复人类型DOC,PAT
    public boolean getMsgSenderType() {
        return "PAT".equals(replierType);
    }

    public String getImageUrl() {
        if (attaList == null || attaList.size() == 0) {
            return "";
        }
        return attaList.get(0).url;
    }

    public String getAttaId() {
        if (attaList == null || attaList.size() == 0) {
            return "";
        }
        return attaList.get(0).attaId;
    }

    public ArrayList<String> getUrls() {
        ArrayList<String> urls = new ArrayList<>();
        if (attaList == null) {
            attaList = new ArrayList<>();
        }
        for (int i = 0; i < attaList.size(); i++) {
            String url = attaList.get(i).url;
            urls.add(url);
        }
        if (urls.size() == 0 && !TextUtils.isEmpty(localityPath)) {
            urls.add(localityPath);
        }
        return urls;
    }
}
