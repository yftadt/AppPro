package test.app.ui.pages.test;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


import androidx.recyclerview.widget.RecyclerView;



import com.list.library.able.OnLoadingListener;

import java.util.ArrayList;
import java.util.List;

import test.app.ui.adapter.test.TestCardAdapter;
import test.app.ui.pages.BaseViewPage;

/**
 * 医生名片 医生文章和咨询
 * Created by Administrator on 2017/8/14.
 */

public class TestCardPager extends BaseViewPage {

    public TestCardAdapter adapter;


    public TestCardPager(Context context) {
        super(context);
    }

    @Override
    protected void onViewCreated() {
        //
        RecyclerView lv = new RecyclerView(context);
        adapter = new TestCardAdapter();
        adapter.setOnItemClickListener(true);
        adapter.setRecyclerView(lv);
        adapter.setRecyclerViewType(context, 1);
        adapter.setOpenRefresh( );
        lv.setAdapter(adapter);
        //
        adapter.setLoadMore(true);
        adapter.setOnLoadingListener(new LoadingListener());
        adapter.setData(getItem());
        setContentView(lv);

    }

    private List<String> getItem() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            items.add(String.valueOf(i + 1));
        }
        return items;
    }

    class LoadingListener implements OnLoadingListener {
        private UiHandler uiHandler;

        public LoadingListener() {
            uiHandler = new UiHandler();
        }

        @Override
        public void onLoading(boolean isRefresh) {
            uiHandler.sendEmptyMessageDelayed(1, 1 * 1000);
        }
    }

    class UiHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.addData("加载更多");
            adapter.onRenovationComplete();
        }
    }
}
