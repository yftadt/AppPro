package test.app.ui.pages.main;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import android.widget.ArrayAdapter;

import androidx.core.widget.NestedScrollView;


import com.list.library.view.refresh.head.RefreshCustomList;

import java.util.ArrayList;

import test.app.ui.pages.BaseViewPage;

/**
 * Created by Administrator on 2016/9/7.
 */
public class TestListPager extends BaseViewPage {
    public TestListPager(Context context) {
        super(context, true);
    }

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
        NestedScrollView nestedScrollView = new NestedScrollView(context);
        nestedScrollView.addView(rootView);
        setContentView(nestedScrollView);
        setLoadingViewPadding(60, 60);
        new Han().sendEmptyMessageDelayed(1, 2 * 1000);
    }

    class Han extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadingFailed();
        }
    }
}
