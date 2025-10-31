package test.app.ui.pages.frg;

import android.widget.TextView;

import test.app.ui.pages.BaseFragmentViewPage;
import test.app.utiles.other.DLog;


public class FragmentTest extends BaseFragmentViewPage {
    @Override
    protected void onViewCreated() {
        TextView tv = new TextView(context);
        setContentView(tv,true);
        tv.setText("123456");
    }

    @Override
    protected void onLoadDataSatrt(boolean isPause) {
        DLog.e("FragmentTest_1:加载数据");
    }


    @Override
    protected void onLoadDataStop(boolean isPause) {
        DLog.e("FragmentTest_1:停止加载");
    }


}
