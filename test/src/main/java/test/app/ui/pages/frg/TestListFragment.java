package test.app.ui.pages.frg;

import android.widget.ArrayAdapter;


import com.list.library.view.refresh.head.RefreshCustomList;

import java.util.ArrayList;

import test.app.ui.pages.BaseFragmentViewPage;
import test.app.utiles.other.DLog;

/**
 * Created by Administrator on 2016/9/7.
 */
public class TestListFragment extends BaseFragmentViewPage {

    @Override
    protected void onViewCreated() {
        RefreshCustomList lv = new RefreshCustomList(context);
        setContentView(lv);
        lv.setHeadType(1);
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        // 这里ListView的适配器选用ArrayAdapter，ListView中每一项的布局选用系统的simple_list_item_1。
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);
     }

    @Override
    public void onInitData() {
        /*NestedScrollView nestedScrollView = new NestedScrollView(context);
        nestedScrollView.addView(rootView);
        setContentView(nestedScrollView);
        setLoadingViewPadding(60,60);*/

    }

    @Override
    protected void onLoadDataSatrt(boolean isPause) {
        DLog.e("TestListFragment_2:加载数据");
    }

    @Override
    public void onLoadDataStop(boolean isPause) {
        DLog.e("TestListFragment_2:停止加载");
        super.onStop();
    }
}
