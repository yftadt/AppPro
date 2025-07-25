package test.app.ui.window.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import test.app.ui.activity.R;


/**
 * 提示信息的dialog
 * Created by Administrator on 2016/8/22.
 */
public class DialogHint extends BaseDialog {
    TextView titleTv;
    TextView msgTv;
    TextView btnRightTv, btnLeftTv, btnAllTv;
    //2个按钮
    View btnTvsRl;
    //设置文本
    private String title, msg, leftHint, rightHint, allHint;
    //设置左右按钮颜色
    private int leftHintColour = 0xff999999, rightHintColour = 0xff0aace9, allHintColour = 0xff0aace9;
    private int msgColour = 0xff000000, msgLocation = Gravity.LEFT, msgTxtSize = 15;
    //true 显示2个按钮
    private boolean isShowOne = false;

    public DialogHint(Context context) {
        super(context, R.style.CommonDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_hint);
        titleTv = (TextView) findViewById(R.id.title_tv);
        msgTv = (TextView) findViewById(R.id.msg_tv);
        btnRightTv = (TextView) findViewById(R.id.btn_right_tv);
        btnLeftTv = (TextView) findViewById(R.id.btn_left_tv);
        btnAllTv = (TextView) findViewById(R.id.btn_all_tv);
        btnTvsRl = findViewById(R.id.btn_tvs_rl);
        btnRightTv.setOnClickListener(this);
        btnLeftTv.setOnClickListener(this);
        btnAllTv.setOnClickListener(this);
    }

    //设置标题
    public void setTitle(int titleId) {
        Context context = getContext();
        this.title = context.getString(titleId);
    }

    public void setTitleMsg(String title, String msg) {
        this.title = title;
        this.msg = msg;
    }

    //设置消息
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setMsgColour(int msgColour) {
        setMsgOther(msgColour, msgLocation, msgTxtSize);
    }

    public void setMsgGravity(int msgLocation) {
        setMsgOther(msgColour, msgLocation, msgTxtSize);
    }

    public void setMsgSize(int msgTxtSize) {
        setMsgOther(msgColour, msgLocation, msgTxtSize);
    }

    /**
     * @param msgColour   消息字体颜色
     * @param msgLocation 消息显示位置
     * @param msgTxtSize  消息字体大小
     */
    public void setMsgOther(int msgColour, int msgLocation, int msgTxtSize) {
        this.msgColour = msgColour;
        this.msgLocation = msgLocation;
        this.msgTxtSize = msgTxtSize;
    }

    //设置2按钮文字
    public void setBtnsHint(int leftHintId, int rightHintId) {
        Context context = getContext();
        setBtnsHint(context.getString(leftHintId), context.getString(rightHintId));

    }

    public void setBtnsHint(String leftHint, String rightHint) {
        this.leftHint = leftHint;
        this.rightHint = rightHint;
    }

    //设置2按钮字体颜色
    public void setBtnColours(int leftHintColour, int rightHintColour) {
        this.leftHintColour = leftHintColour;
        this.rightHintColour = rightHintColour;
    }

    //设置单个按钮文字
    public void setBtnHint(int allHintId) {
        Context context = getContext();
        setBtnHint(context.getString(allHintId));
    }

    public void setBtnHint(String allHint) {
        this.allHint = allHint;
    }

    //设置单个按钮字体颜色
    public void setBtnColour(int allHintColour) {
        this.allHintColour = allHintColour;
    }

    //设置显示一个按钮还是2个按钮 ，默认显示2个按钮
    public void setOneSelectMode(boolean isShowOne) {
        this.isShowOne = isShowOne;
    }


    private void setData() {
        //设置标题
        titleTv.setText(title);
        int isShow = TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE;
        titleTv.setVisibility(isShow);
        //设置信息
        msgTv.setText(msg);
        msgTv.setTextColor(msgColour);
        msgTv.setTextSize(msgTxtSize);
        msgTv.setGravity(msgLocation);
        //设置2个按钮
        btnLeftTv.setText(leftHint);
        btnLeftTv.setTextColor(leftHintColour);
        btnRightTv.setText(rightHint);
        btnRightTv.setTextColor(rightHintColour);
        //设置单个按钮
        btnAllTv.setText(allHint);
        btnAllTv.setTextColor(allHintColour);
        //设置按钮显示类型
        if (isShowOne) {
            //显示一个按钮
            btnTvsRl.setVisibility(View.GONE);
            btnAllTv.setVisibility(View.VISIBLE);
        } else {
            //显示二个按钮
            btnAllTv.setVisibility(View.GONE);
            btnTvsRl.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void show() {
        super.show();
        setData();
    }

    public void onClick(View view) {
        dismiss();
        if (onDialogBackListener == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.btn_left_tv) {
            onDialogBackListener.onDialogBack(DIALOG_TYPE_HINT, 1, "");
            return;
        }
        if (id == R.id.btn_right_tv) {
            onDialogBackListener.onDialogBack(DIALOG_TYPE_HINT, 2, "");
            return;
        }
        if (id == R.id.btn_all_tv) {
            onDialogBackListener.onDialogBack(DIALOG_TYPE_HINT, 3, "");
            return;
        }


    }


}
