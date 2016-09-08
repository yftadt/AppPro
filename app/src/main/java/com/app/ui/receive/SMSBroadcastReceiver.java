package com.app.ui.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 短信监听
 *
 * @author
 */
public class SMSBroadcastReceiver extends BroadcastReceiver {

    private MessageListener mMessageListener;
    public final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public SMSBroadcastReceiver() {
        super();
    }

    private String patternCoder4 = "\\d{4}";
    private String patternCoder6 = "\\d{6}";
    private String patternCoder8 = "\\d{8}";

    public String getNumbers(String content, String tag) {
        String code = "";
        Pattern pattern = Pattern.compile(tag);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            code = matcher.group(0);
        }
        return code;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            for (Object pdu : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                String sender = smsMessage.getDisplayOriginatingAddress();
                //短信内容
                String content = smsMessage.getDisplayMessageBody();
                if (!content.contains("点点医生")) {
                    return;
                }
                if (!content.contains("验证码")) {
                    return;
                }
                String[] msg = content.split("验证码");
                if (msg.length < 2) {
                    return;
                }
                String code = "";
                code = getNumbers(msg[1], patternCoder4);
                if (TextUtils.isEmpty(code)) {
                    code = getNumbers(msg[1], patternCoder6);
                }
                if (TextUtils.isEmpty(code)) {
                    code = getNumbers(msg[1], patternCoder8);
                }
                if (mMessageListener != null && !TextUtils.isEmpty(code)) {
                    mMessageListener.onReceived(code);
                }
            }
        }

    }

    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private static SMSBroadcastReceiver mSMSBroadcastReceiver;

    //注册广播
    public static void registerReceiver(Context context, MessageListener messageListener) {
        //生成广播处理
        mSMSBroadcastReceiver = new SMSBroadcastReceiver();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter(ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册广播
        context.registerReceiver(mSMSBroadcastReceiver, intentFilter);

        mSMSBroadcastReceiver.setOnReceivedMessageListener(messageListener);
    }

    protected static void unregisterReceiver(Context context) {
        if (mSMSBroadcastReceiver == null) {
            return;
        }
        //注销短信监听广播
        context.unregisterReceiver(mSMSBroadcastReceiver);
    }

    //回调接口
    public interface MessageListener {
        void onReceived(String message);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }
}

