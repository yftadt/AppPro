package test.app.ui.pages.main;

import android.content.Context;
import android.widget.TextView;

import test.app.ui.pages.BaseViewPage;


/**
 * Created by Administrator on 2016/9/7.
 */
public class TestPager extends BaseViewPage {
    public TestPager(Context context) {
        super(context, false);
    }

    @Override
    protected void onViewCreated() {
        TextView tv = new TextView(context);
        setContentView(tv);
        tv.setText("123456");
    }

    @Override
    public void onInitData() {
        setLoadingViewHeight(400);
    }
}
