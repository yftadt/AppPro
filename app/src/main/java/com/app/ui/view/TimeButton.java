package com.app.ui.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;


public class TimeButton extends Button {
    private boolean isRun;

    public TimeButton(Context context) {
        super(context);
        init();
    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private CountDown countDown;
    private GetCode getCode;

    public void setListener(GetCode getCode) {
        this.getCode = getCode;
    }

    //0:开始，1：倒计时中，2：完成
    private String[] txts;
    private int[] textColors, backgroundIds;

    public void setText(String[] txts, int[] textColors, int[] backgroundIds) {
        this.txts = txts;
        this.textColors = textColors;
        this.backgroundIds = backgroundIds;
        setData(0);
    }

    public void setText(String str, int textColor, int backgroundId) {
        setText(new String[]{str, "", str},
                new int[]{textColor, textColor, textColor},
                new int[]{backgroundId, backgroundId, backgroundId});

    }


    private void setData(int type) {
        setData(type, "");
    }

    private void setData(int type, String step) {
        String text = txts[type];
        int color = textColors[type];
        int id = backgroundIds[type];
        switch (type) {
            case 0:
            case 2:
                break;
            case 1:
                text = step + text;
                break;
        }
        setText(text);
        setTextColor(color);
        setBackgroundResource(id);
    }

    public void init() {
        countDown = new CountDown(60 * 1000, 1000);
        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isRun) {
                    return;
                }

                if (!TimeButton.this.getCode.doRequest()) {
                    return;
                }
                startTime();
            }
        });
    }

    /**
     * 开始计时
     */
    private void startTime() {
        if (isRun) {
            return;
        }
        isRun = true;
        countDown.start();
    }

    class CountDown extends CountDownTimer {
        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            isRun = false;
            setData(2);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String time = "";
            long millis = millisUntilFinished / 1000;
            if (millis < 10) {
                time = "0";
            }
            time += millis;
            setData(1, time);
        }
    }


    public interface GetCode {
        /**
         * 发送请求，获取验证码
         */
        public boolean doRequest();
    }
}