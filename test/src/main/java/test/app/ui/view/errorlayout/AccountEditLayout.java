package test.app.ui.view.errorlayout;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;





import com.library.baseui.activity.BaseActivity;
import com.library.baseui.activity.BaseCompatActivity;
import com.library.baseui.utile.app.APKInfo;
import com.library.baseui.utile.other.IdCardUtil;
import com.library.baseui.utile.other.StringUtile;

import test.app.ui.activity.R;
import test.app.ui.view.down.VerificationCodeView;


/**提示错误信息，有一个向上移动的效果
 * setTypeInput(1)
 * setErrorMsg(1)
 * 账号 密码，等填写
 * Created by Administrator on 2017/9/8.
 */

public class AccountEditLayout extends LinearLayout {


    public AccountEditLayout(Context context) {
        super(context);
        init(context);
    }

    public AccountEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AccountEditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private ImageView tagLeftIv, tagRightIv;
    private VerificationCodeView verificationCodeView;
    private EditText et;
    private AccountRrrorView errorTv;

    private void init(Context context) {
        setOrientation(VERTICAL);
        int tagHigh = (int) APKInfo.getInstance().getDIPTOPX(45);
        //设置左侧图标
        RelativeLayout rl = new RelativeLayout(context);
        rl.setBackgroundResource(R.drawable.error_layout_bg);
        tagLeftIv = new ImageView(context);
        RelativeLayout.LayoutParams tagLeftIvLp = new RelativeLayout.LayoutParams(tagHigh, tagHigh);
        tagLeftIv.setLayoutParams(tagLeftIvLp);
        tagLeftIv.setScaleType(ImageView.ScaleType.CENTER);
        rl.addView(tagLeftIv);
        //设置左侧图标和输入框之间的分割想
        TextView line = new TextView(context);
        RelativeLayout.LayoutParams lineLp = new RelativeLayout.LayoutParams(2, tagHigh);
        line.setLayoutParams(lineLp);
        lineLp.leftMargin = tagHigh;
        line.setBackgroundColor(0xffffffff);
        rl.addView(line);
        //设置输入框
        et = new EditText(context);
        RelativeLayout.LayoutParams etLp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                tagHigh);
        etLp.leftMargin = tagHigh + 2;
        et.setPadding(30, 0, 0, 0);
        et.setLayoutParams(etLp);
        et.setBackgroundColor(0x00ff);
        et.setMaxLines(1);
        et.setTextColor(0xff333333);
        et.setHintTextColor(0xffcccccc);
        et.setTextSize(15);
        et.addTextChangedListener(new OnTetxgChange());
        rl.addView(et);
        //设置右侧tag
        tagRightIv = new ImageView(context);
        RelativeLayout.LayoutParams tagRightIvLp = new RelativeLayout.LayoutParams(tagHigh, tagHigh);
        tagRightIvLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tagRightIv.setLayoutParams(tagRightIvLp);
        tagRightIv.setScaleType(ImageView.ScaleType.CENTER);
        tagRightIv.setOnClickListener(click);
        tagRightIv.setVisibility(View.GONE);
        rl.addView(tagRightIv);
        //倒计时按钮
        verificationCodeView = new VerificationCodeView(context);
        RelativeLayout.LayoutParams timeDownLp = new RelativeLayout.LayoutParams(tagHigh * 2, tagHigh);
        timeDownLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        verificationCodeView.setLayoutParams(timeDownLp);
        verificationCodeView.setTextColors(new int[]{0xff939699, 0xff939699});
        verificationCodeView.setBgIcons(new int[]{R.drawable.error_layout_verification_code_bg, R.drawable.error_layout_verification_code_bg});
        verificationCodeView.timeRest();
        verificationCodeView.setVisibility(View.GONE);
        rl.addView(verificationCodeView);
        //
        addView(rl);
        //错误提示view
        LayoutParams errorLp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        errorTv = new AccountRrrorView(context);
        errorTv.setTextSize(12);
        errorTv.setTextColor(0xfff4888c);
        errorTv.setGravity(Gravity.CENTER_VERTICAL);
        errorTv.setCompoundDrawablePadding(20);
        errorTv.setLayoutParams(errorLp);
        addView(errorTv);
    }


    //获取输入内容
    public String getEditText() {
        return et.getText().toString();
    }

    //设置输入内容
    public void setEditText(String msg) {
        if (msg == null) {
            msg = "";
        }
        et.setText(msg);
        et.setSelection(msg.length());
    }

    public void setEditTextFixation(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        et.setText(msg);
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
        tagRightIv.setVisibility(View.INVISIBLE);
    }

    //设置图标
    public void setTagLeftIcon(int icon) {
        tagLeftIv.setImageResource(icon);
    }

    //设置图标
    public void setTagRightIcon(int icon) {
        tagRightIv.setImageResource(icon);
        tagRightIv.setVisibility(View.VISIBLE);
        verificationCodeView.setVisibility(View.GONE);
    }

    //返回倒计时对象
    public VerificationCodeView getVerificationCodeView() {
        return verificationCodeView;
    }


    //输入框类型 1：手机号 2:密码 3:验证码
    private int typeInput;
    //默认的错误提示内容
    private String errorDefaultMsg;

    public void setErrorDefaultMsg(String errorDefaultMsg) {
        this.errorDefaultMsg = errorDefaultMsg;
    }

    //设置显示错误类型 errorType:0隐藏 非0 显示
    public void setErrorMsg(int errorType) {
        errorTv.setErrorMsg(errorType, errorDefaultMsg);
    }

    // errorType:0隐藏 非0 显示
    public void setErrorMsg(String errorMsg) {
        errorTv.setErrorMsg(1, errorMsg);
    }

    //设置输入框的类型
    public void setTypeInput(int typeInput) {
        setTypeInput(typeInput, "");
    }

    public void setTypeInput(int typeInput, String hint) {
        this.typeInput = typeInput;
        String hintMsg = "";
        switch (typeInput) {
            case 1:
                //输入手机号码
                hintMsg = "输入手机号码";
                et.setInputType(InputType.TYPE_CLASS_PHONE);
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                setTagLeftIcon(R.mipmap.error_layout_phone);
                setTagRightIcon(R.mipmap.error_layout_et_delete);
                errorDefaultMsg = "请填写手机号码";
                break;
            case 2:
                //输入密码
                hintMsg = "输入密码";
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                et.setKeyListener(new DigitsKeyListener() {
                    String digits = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM!@#$%&*~";

                    @Override
                    public int getInputType() {
                        return InputType.TYPE_TEXT_VARIATION_PASSWORD;
                    }

                    @Override
                    protected char[] getAcceptedChars() {
                        return digits.toCharArray();
                    }
                });
                //在三星手机上无效，因为全数字键盘不能切换英文键盘
                //  et.setKeyListener(DigitsKeyListener.getInstance("1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM!@#$%&*~"));
                setTagLeftIcon(R.mipmap.error_layout_pwd);
                setTagRightIcon(R.mipmap.error_layout_eye);
                setShowText(true);
                errorDefaultMsg = "请输入6-20位密码";
                break;
            case 3:
                //验证码
                hintMsg = "输入验证码";
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                setTagLeftIcon(R.mipmap.error_layout_code);
                verificationCodeView.setVisibility(View.VISIBLE);
                tagRightIv.setVisibility(View.GONE);
                errorDefaultMsg = "请输入验证码";
                break;
            case 4:
                //真实姓名
                hintMsg = "输入真实姓名";
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                setTagLeftIcon(R.mipmap.error_layout_name);
                setTagRightIcon(R.mipmap.error_layout_et_delete);
                errorDefaultMsg = "请输入真实姓名";
                break;
            case 5:
                //输入身份证
                hintMsg = "输入身份证";
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
                setTagLeftIcon(R.mipmap.error_layout_number);
                setTagRightIcon(R.mipmap.error_layout_et_delete);
                errorDefaultMsg = "请输入身份证号码";
                break;
        }
        if (TextUtils.isEmpty(hint)) {
            hint = hintMsg;
        }
        et.setHint(hint);
    }

    //设置密码是否显示
    private void setShowText(boolean isShow) {
        isPwdShow = isShow;
        if (isPwdShow) {
            //密码显示明文
            et.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            //密码不显示明文
            et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        int index = et.getText().length();
        et.setSelection(index);
    }

    private BaseCompatActivity.TextChangeListener textChangeListener;

    public void setOnTextChangeListener(BaseCompatActivity.TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }

    class OnTetxgChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int length = s.length();
            String text = s.toString();
            switch (typeInput) {
                case 1:
                    //手机号码
                    boolean isPhone = StringUtile.isPhone(text);
                    if (!isPhone && length == 11) {
                        setErrorMsg(1);
                    }
                    if (isPhone) {
                        setErrorMsg(0);
                    }
                    break;
                case 2:
                    tagRightIv.setVisibility(length == 0 ? View.GONE : View.VISIBLE);
                    if (length >= 6) {
                        setErrorMsg(0);
                    }
                    break;
                case 3:
                case 4:
                    if (length > 0) {
                        setErrorMsg(0);
                    }
                    break;
                case 5:
                    //验证身份证
                    if (IdCardUtil.validateCard(text)) {
                        setErrorMsg(0);
                    }
                    break;
            }
            if (textChangeListener != null) {
                textChangeListener.onTextChanged(s, start, before, count);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private OnClick click = new OnClick();
    //true 显示明文
    private boolean isPwdShow = false;

    class OnClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (typeInput) {
                case 1:
                case 4:
                case 5:
                    et.setText("");
                    break;
                case 2:
                    setShowText(!isPwdShow);
                    break;

            }
        }
    }


}
