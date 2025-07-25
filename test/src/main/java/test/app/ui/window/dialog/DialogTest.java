package test.app.ui.window.dialog;

import android.app.Activity;
import android.os.Bundle;

import test.app.ui.activity.R;


/**
 * 测试 宽度最大化
 */
public class DialogTest extends BaseDialog {
    @Override
    protected void init() {

    }
    public DialogTest(Activity act ) {
        super(act, R.style.dialog_white);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_test);

    }

    @Override
    public void show() {
        super.show();
    }



}
