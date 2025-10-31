package test.app.ui.pages.frg;



import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import test.app.ui.activity.R;
import test.app.ui.adapter.ViewPagerStringAdapter;
import test.app.ui.pages.BaseFragmentViewPage;
import test.app.ui.pages.BaseViewPage;
import test.app.ui.pages.main.TestPager;
import test.app.utiles.other.DLog;

/**
 * Created by Administrator on 2016/9/7.
 */
public class TestTabFragement extends BaseFragmentViewPage {
    private ViewPager viewPager;


    @Override
    protected void onViewCreated() {
        setContentView(R.layout.test_three_tab);
        TabLayout indicator = (TabLayout) findViewById(R.id.view_pager_indicator);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerStringAdapter adapter = new ViewPagerStringAdapter(getViews(), getTitles());
        viewPager.setAdapter(adapter);
        indicator.setupWithViewPager(viewPager);

    }
    //  获取tab页

    private ArrayList<BaseViewPage> getViews() {
        ArrayList<BaseViewPage> listPager = new ArrayList<BaseViewPage>();
        listPager.add(new TestPager(context));
        listPager.add(new TestPager(context));
        listPager.add(new TestPager(context));
        return listPager;
    }

    private ArrayList<String> getTitles() {
        ArrayList<String> titls = new ArrayList<String>();
        titls.add("未解答");
        titls.add("已解答");
        titls.add("已逾期");
        return titls;
    }

    @Override
    protected void onLoadDataSatrt(boolean isPause) {
        DLog.e("TestTabFragement_3:加载数据");
    }

    @Override
    public void onLoadDataStop(boolean isPause) {
        DLog.e("TestTabFragement_3:停止加载");
        super.onStop();
    }
}
