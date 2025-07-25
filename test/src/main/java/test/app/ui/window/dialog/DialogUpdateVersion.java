package test.app.ui.window.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import test.app.ui.activity.R;


/**
 * Created by Administrator on 2016/8/22.
 */
public class DialogUpdateVersion extends BaseDialog {

    TextView versionMsgTv;
    private Spanned msg;

    public DialogUpdateVersion(Context context) {
        super(context, R.style.WaitingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_version);

        versionMsgTv = (TextView) findViewById(R.id.version_msg_tv);
        findViewById(R.id.version_confirm_tv).setOnClickListener(this);
    }

    public void setMsg(String version) {
        String html = "<html><body>" + "当前版本号：" + "<font color=#30CFD0>" + version + "</font>" + "<br/>" + "已是最新版本" + "</body></html>";
        msg = Html.fromHtml(html);
    }

    @Override
    public void show() {
        super.show();
        versionMsgTv.setText(msg);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
