package test.app.ui.view.text;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import test.app.ui.activity.R;


/**
 * 可以展开的查看更多的msg
 * Created by Administrator on 2017/10/25 0025.
 */

public class ExpansionRl extends RelativeLayout {


    public ExpansionRl(Context context) {
        super(context);
        init(context);
    }

    public ExpansionRl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpansionRl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private View llPart, rlAll;
    private MeasureTextView2 tvTextMsg1, tvTextMsg2, tvTextMsg3;
    private TextView tvTextMsg4;

    private void init(Context context) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_expansion_rl, this);
        llPart = view.findViewById(R.id.ll_part);
        tvTextMsg1 = view.findViewById(R.id.tv_text_msg_1);
        tvTextMsg2 = view.findViewById(R.id.tv_text_msg_2);
        tvTextMsg2.setVisibility(View.GONE);
        //
        rlAll = view.findViewById(R.id.rl_all);
        tvTextMsg3 = view.findViewById(R.id.tv_text_msg_all);
        tvTextMsg4 = view.findViewById(R.id.tv_text_msg_tag);
        tvTextMsg4.setVisibility(View.GONE);
        tvTextMsg4.setMovementMethod(LinkMovementMethod.getInstance());
        rlAll.setVisibility(View.GONE);
        //
        tvTextMsg1.setOnTextChange(new MeasureTextView2.OnTextChange() {

            @Override
            public void onChangeComplete(float textWidth) {
                if (parCs != null) {
                    //从不显示到显示 也会走这里  之前计算过了不再计算
                    return;
                }
                Layout layout = tvTextMsg1.getLayout();
                int line = layout.getLineCount();
                if (line == 1) {
                    tvTextMsg2.setVisibility(View.GONE);
                } else {
                    int lineEnd = layout.getLineEnd(0);
                    String newMsg = msg.substring(lineEnd - 1, msg.length());
                    msg2 = newMsg;
                    tvTextMsg2.setMsg(newMsg);
                    tvTextMsg2.setVisibility(View.VISIBLE);
                }
            }
        });
        tvTextMsg2.setOnTextChange(new MeasureTextView2.OnTextChange() {

            @Override
            public void onChangeComplete(float textWidth) {
                if (parCs != null) {
                    //从不显示到显示 也会走这里  之前计算过了不再计算
                    return;
                }
                Layout layout = tvTextMsg2.getLayout();
                int line = layout.getLineCount();
                if (line == 1) {
                    return;
                }
                int lineEnd = layout.getLineEnd(0);
                String newMsg = msg2.substring(0, lineEnd - 10) + "...";
                msg3 = newMsg;
                setPar();
            }
        });
        tvTextMsg3.setOnTextChange(new MeasureTextView2.OnTextChange() {

            @Override
            public void onChangeComplete(float textWidth) {
                if (allCs != null) {
                    //从不显示到显示 也会走这里  之前计算过了不再计算
                    return;
                }
                Paint paint = tvTextMsg3.getPaint();
                float extraWidth = paint.measureText(extraAll);
                int lw = getWidth();
                int w = (int) (lw - textWidth);
                extraTempAll = extraAll;
                //
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlAll.getLayoutParams();
                params = new RelativeLayout.LayoutParams(params.width, params.height);
                if (w >= extraWidth) {
                    //底部对齐+leftMargin
                    params.addRule(RelativeLayout.ALIGN_BOTTOM, tvTextMsg3.getId());
                    params.leftMargin = (int) (textWidth + 1);
                    //params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                } else {
                    //换行
                    params.addRule(RelativeLayout.BELOW, tvTextMsg3.getId());
                }
                setAllExtra();
                tvTextMsg4.setLayoutParams(params);
                tvTextMsg4.setVisibility(View.VISIBLE);
            }
        });
    }

    private String extraPart = "展开";
    private String extraAll = "收缩";
    private String extraTempAll;
    private String msg2, msg3;
    private SpannableString parCs;

    //设置展示部分文本
    private void setPar() {
        if (parCs == null) {
            String newMsg = msg3;
            parCs = getClickableSpan(newMsg, extraPart);
            tvTextMsg2.setText(parCs);
        }
        llPart.setVisibility(View.VISIBLE);
        rlAll.setVisibility(View.GONE);
    }

    private SpannableString allCs;

    //设置展示全部文本
    private void setAll() {
        if (TextUtils.isEmpty(extraTempAll)) {
            //全部从没显示过  要去计算下
            tvTextMsg3.setMsg(msg);
            llPart.setVisibility(View.INVISIBLE);
            rlAll.setVisibility(View.VISIBLE);
            return;
        }
        llPart.setVisibility(View.INVISIBLE);
        rlAll.setVisibility(View.VISIBLE);
    }

    private void setAllExtra() {
        if (allCs == null) {
            allCs = getClickableSpan("", extraTempAll);
            tvTextMsg4.setText(allCs);
        }
    }

    private String msg;

    public void setMsg(String msg) {
        this.msg = msg;
        tvTextMsg1.setMsg(msg);
    }

    private SpannableString getClickableSpan(String text, String extraStr) {
        String temp = text + extraStr;
        SpannableString sp = new SpannableString(temp);
        //
        int start = text.length();
        int end = temp.length();
        //设置字体大小
        sp.setSpan(new RelativeSizeSpan(1.1f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体可点击
        sp.setSpan(new Listener(), start, end, Spanned.SPAN_MARK_MARK);
        //添加前景色为红色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#ff0000"));
        sp.setSpan(foregroundColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;

    }

    private boolean isExpand;

    class Listener extends ClickableSpan implements View.OnClickListener {

        //文字点击事件
        @Override
        public void onClick(@NonNull View widget) {
            isExpand = !isExpand;
            if (isExpand) {
                setAll();
            } else {
                setPar();
            }
        }

        @Override

        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

    }
}