package com.library.baseui.view.tick;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

//倒计时的view
public class CountDownView extends TextView {
    private CountDown countDown;
    private boolean isRun;

    public CountDownView(Context context) {
        super(context);
        init();
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        countDown = new CountDown(60 * 1000, 1000);
        setGravity(Gravity.CENTER);
        setOnClickListener(new OnClick());
        setTextSize(14);
    }

    private boolean isStart = false;
    private boolean isEnd = false;
    //文字颜色 0：未开始颜色 1：倒计时中的颜色
    private int[] textColors = new int[]{0xff939699, 0xff939699};

    //背景颜色 0：未开始颜色 1：倒计时中的颜色
    private int[] bgColors = new int[]{0, 0};
    private int[] bgIcon = new int[]{0, 0};

    public void setTextColors(int[] textColors) {
        this.textColors = textColors;
    }

    public void setBgColors(int[] bgColors) {
        this.bgColors = bgColors;
        bgIcon = new int[]{0, 0};
    }

    public void setBgIcons(int[] bgIcon) {
        this.bgIcon = bgIcon;
        bgColors = new int[]{0, 0};
    }

    //开始计时
    public void timeStart() {
        if (isRun) {
            return;
        }
        isRun = true;
        countDown.start();
        if (onCountDownListener == null) {
            return;
        }
        onCountDownListener.onCountDownStart();
    }

    //停止倒计时
    public void timeStop() {
        if (!isRun) {
            return;
        }
        countDown.cancel();
        countDown.onFinish();
    }

    //倒计时重置
    public void timeRest() {
        setText("获取验证码");
        setTextColor(textColors[0]);
        //设置背景色
        if (bgIcon[0] != 0) {
            setBackgroundResource(bgIcon[0]);
        }
        if (bgColors[0] != 0) {
            setBackgroundColor(bgColors[0]);
        }
        //
        if (isStart) {
            setTimeStart();
        }
        if (isEnd) {
            setTimeEnd();
        }
        isRun = false;
    }

    //开始倒计时颜色
    private void setTimeStart() {
        if (textColors[0] != 0) {
            setTextColor(textColors[0]);
        }
        isStart = true;
        isEnd = false;
    }

    //结束倒计时颜色
    private void setTimeEnd() {
        if (textColors[1] != 0) {
            setTextColor(textColors[1]);
        }
        isEnd = true;
        isStart = false;
    }

    //到计时中
    protected void setTimeNumber(String time) {
        setTextColor(textColors[1]);
        if (bgIcon[1] != 0) {
            setBackgroundResource(bgIcon[1]);
        }
        if (bgColors[1] != 0) {
            setBackgroundColor(bgColors[1]);
        }
        if (onCountDownListener == null) {
            setText(time);
            return;
        }
        onCountDownListener.onCountDownTick(time);
    }


    //倒计时
    class CountDown extends CountDownTimer {
        public CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            isRun = false;
            timeRest();
            if (onCountDownListener == null) {
                return;
            }
            onCountDownListener.onCountDownFinish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String time = "";
            long millis = millisUntilFinished / 1000;
            if (millis < 10) {
                time = "0";
            }
            time += millis;
            setTimeNumber(time);
        }
    }

    class OnClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (isRun) {
                return;
            }
            if (onRequestCode == null) {
                return;
            }
            onRequestCode.onCodeRequest(CountDownView.this);
        }
    }

    protected OnRequestCode onRequestCode;

    public void setOnRequestCode(OnRequestCode onRequestCode) {
        this.onRequestCode = onRequestCode;
    }

    public interface OnRequestCode {
        //  发送请求，获取验证码
        void onCodeRequest(CountDownView view);

        //发送请求错误
        void onCodeFailed(int what);

        //验证码,验证完成 isSucceed:true 验证成功
        void onCodeComplete(boolean isSucceed, Object obj);
    }

    protected OnCountDownListener onCountDownListener;

    public void setOnCountDownListener(OnCountDownListener onCountDownListener) {
        this.onCountDownListener = onCountDownListener;
    }

    //倒计时监听
    public interface OnCountDownListener {

        //倒计时开始
        void onCountDownStart();

        //倒计时中
        void onCountDownTick(String tick);

        //倒计时结束
        void onCountDownFinish();
    }
}