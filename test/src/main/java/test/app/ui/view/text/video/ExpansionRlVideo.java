package test.app.ui.view.text.video;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.library.baseui.utile.other.StringUtile;

import sj.mblog.Logx;
import test.app.ui.activity.R;


/**
 * 可以展开的查看更多的msg
 */

public class ExpansionRlVideo extends RelativeLayout {


    public ExpansionRlVideo(Context context) {
        super(context);
        init(context);
    }

    public ExpansionRlVideo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpansionRlVideo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private MeasureTextViewVideo tvMsgM;
    private TextView tvMsgText;
    private TextView tvShowPar, tvShowAll;


    private void init(Context context) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_expansion_rl_2, this);

        tvMsgM = view.findViewById(R.id.tv_msg_m);
        //tvMsgM = getMeasureTextView();
        tvMsgText = view.findViewById(R.id.tv_msg_txt);
        //
        tvShowPar = view.findViewById(R.id.tv_text_show_part);
        tvShowAll = view.findViewById(R.id.tv_text_show_all);
        //
        tvMsgM.setOnTextChange(new OnTextChangeListener() {
            @Override
            public void onChangeComplete(String txt, int lastLineIndex, float txtWidth) {
                Logx.d("文案  回调 ：" + "txt=" + txt + " lastLineIndex=" + lastLineIndex + " txtWidth=" + txtWidth);
                if (lastLineIndex > 1) {
                    String text = tvMsgM.getLineText(1);
                    Logx.d("文案  回调 ：" + "第二行文案=" + text);
                }
                if (lastLineIndex <= 1) {
                    tvMsgText.setText(tvMsgM.getText().toString());
                } else {
                    //第二行
                    float textWidth2 = tvMsgM.getLineWidth(1);
                    String text2 = tvMsgM.getLineText(1);
                    text2 = StringUtile.setDelLine(text2);
                    //
                    int tvWidth = getParW();
                    int viewRootW = getWidth();
                    //
                    String text1 = tvMsgM.getLineText(0);
                    Logx.d("文案  回调 ：" + "第一行文案=" + text1);
                    if (tvWidth + textWidth2 < viewRootW) {
                        parTextWidth = textWidth2;
                        parText = text1 + text2;
                    } else {
                        setTagView1(text1, text2, textWidth2, tvWidth, viewRootW);
                    }
                    //
                    allTextWidth = txtWidth;
                    setPar();
                    setTagLocation(lastLineIndex + 1);
                }

            }

        });
        tvShowPar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean temp = !isExpand;
                if (temp) {
                    int line = tvMsgM.getLayout().getLineCount();

                    if (line > 5) {
                        if (onExpandClick != null) {
                            onExpandClick.onExpandClick();
                        } else {
                            isExpand = true;
                            setAll();
                        }
                    } else {
                        isExpand = true;
                        setAll();
                    }

                }
            }
        });
        tvShowAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpand = false;
                setPar();
            }
        });
    }

    private MeasureTextViewVideo getMeasureTextView() {
        Context con = getContext();
        MeasureTextViewVideo tv = new MeasureTextViewVideo(con);
        tv.setTextColor(Color.parseColor("#ffffff"));
        tv.setTextSize(con.getResources().getDimensionPixelSize(R.dimen.dp_15));
        return tv;
    }

    private void setTagView1(String text1, String text2, float textWidth2, int tvWidth, int viewRootW) {
        int length = text2.length();
        float poorW = (tvWidth + textWidth2) - viewRootW;
        float poorSize = 0;
        int poorIndex = 0;
        for (int i = (length - 1); i >= 0; i--) {
            String t = text2.charAt(i) + "";
            float w = tvMsgM.getStrWidth(t);
            poorSize += w;
            poorIndex = i;
            if (poorSize >= poorW) {
                break;
            }
        }
        //
        parTextWidth = textWidth2 - poorW;
        text2 = text2.substring(0, poorIndex);
        parText = text1 + "\n" + text2;
    }

    public void setExtra(String extraPart, String extraAll) {
        tvShowPar.setText("... " + extraPart);
        tvShowAll.setText(extraAll);
    }

    private String parText;
    private float parTextWidth;
    private int parW = 0;

    //设置展示部分文本
    private void setPar() {
        tvMsgText.setText(parText);
        tvShowAll.setVisibility(View.GONE);
        tvShowPar.setVisibility(View.VISIBLE);
    }


    private int getParW() {
        if (parW == 0) {
            parW = tvShowPar.getWidth();
        }
        return parW;
    }

    //设置展示全部文本
    private float allTextWidth;
    private int allW = 0;

    private void setAll() {
        tvShowPar.setVisibility(View.GONE);
        tvShowAll.setVisibility(View.VISIBLE);
        String text = tvMsgM.getText().toString();
        tvMsgText.setText(text);
    }


    private int getAllW() {
        if (allW == 0) {
            allW = tvShowAll.getWidth();
        }
        return allW;
    }

    //设置位置
    private void setTagLocation(int lines) {
        LayoutParams paramsPar = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        paramsPar.addRule(RelativeLayout.ALIGN_BOTTOM, tvMsgText.getId());
        paramsPar.leftMargin = (int) parTextWidth;
        tvShowPar.setLayoutParams(paramsPar);
        //
        LayoutParams paramsAll = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        int tvAllWidth = getAllW();
        int viewWidth = getWidth();
        if ((allTextWidth + tvAllWidth) > viewWidth) {
            paramsAll.addRule(RelativeLayout.BELOW, tvMsgText.getId());
            paramsAll.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            paramsAll.addRule(RelativeLayout.ALIGN_BOTTOM, tvMsgText.getId());
            paramsAll.leftMargin = (int) allTextWidth;
        }
        tvShowAll.setLayoutParams(paramsAll);
        tvShowAll.setVisibility(View.GONE);
    }

    private String msg;
    private boolean isExpand;

    private void setReset() {
        parW = 0;
        allW = 0;
        isExpand = false;
        parText = "";
        parTextWidth = 0f;
        allTextWidth = 0f;
        // textLine = 0;
        tvMsgText.setText("");
        tvShowPar.setVisibility(View.INVISIBLE);
        tvShowPar.setLayoutParams(getTvResetLp());
        tvShowAll.setVisibility(View.INVISIBLE);
        tvShowAll.setLayoutParams(getTvResetLp());
        tvMsgM.setMaxLines(6);
        //initTvPar();
    }

    private boolean initTv = false;

    private void initTvPar() {
        if (initTv) {
            return;
        }
        initTv = true;
        tvMsgM.setTextSize(tvMsgText.getTextSize());
        LayoutParams params1 = new LayoutParams(
                getWidth(), LayoutParams.WRAP_CONTENT);
        tvMsgM.setLayoutParams(params1);

    }

    private LayoutParams getTvResetLp() {
        LayoutParams params1 = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //params1.addRule(RelativeLayout.BELOW, llMsg.getId());
        params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        return params1;
    }

    public void setMsg(String msg) {
        if (msg.equals(this.msg)) {
            return;
        }
        this.msg = msg;
        Logx.d("文案：" + msg);
        setReset();
        Spanned html2 = Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY);
        tvMsgM.setText(html2);
        tvMsgM.post(new Runnable() {
            @Override
            public void run() {
                int count = tvMsgM.getLayout().getLineCount();
                Logx.d("文案  post 行数：" + count);
                if (count <= 2) {
                } else {
                    tvMsgM.setMaxLines(2);
                }
            }
        });
    }


    private OnExpandClick onExpandClick;

    public void setOnExpandClick(OnExpandClick onExpandClick) {
        this.onExpandClick = onExpandClick;
    }

    public interface OnExpandClick {
        void onExpandClick();
    }

    private void test(){
        SpannableString spannableString = new SpannableString("你的文本内容");
        StaticLayout staticLayout = new StaticLayout(spannableString,
                new TextPaint(), tvMsgText.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f,
                false);

        int lineCount = staticLayout.getLineCount();
        if (lineCount > 0) {
            int lastLineStart = staticLayout.getLineStart(lineCount - 1);
            int lastLineEnd = staticLayout.getLineEnd(lineCount - 1);
            String lastLineText = spannableString.toString().substring(lastLineStart, lastLineEnd);
            Logx.d("LastLine", lastLineText);
        }

    }
}