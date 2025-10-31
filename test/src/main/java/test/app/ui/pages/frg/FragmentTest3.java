package test.app.ui.pages.frg;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.library.baseui.utile.app.ActivityUtile;

import test.app.ui.activity.R;
import test.app.ui.activity.test.ElementAnimation2Activity;
import test.app.ui.pages.BaseFragmentViewPage;
import test.app.utiles.other.DLog;


public class FragmentTest3 extends BaseFragmentViewPage {
    private TextView tvMsg;

    @Override
    protected void onViewCreated() {
        setContentView(R.layout.frg_element_animation3);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                FragmentActivity activity = getActivity();

                ActivityUtile.finishActivityElement(activity);
            }
        });
        //

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
