package test.app.ui.activity.refresh;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sj.mblog.Logx;
import test.app.ui.activity.R;
import test.app.ui.adapter.test.TestRefreshAdapter;

//刷新  (有效 可以使用)
public class TestRefreshActivity1 extends AppCompatActivity {
    private RecyclerView mRecyclerView;
     private RefreshLayoutRl refreshLayoutRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh_1);
        View swipeLoadView = findViewById(R.id.swipe_load_layout);

        if (swipeLoadView instanceof RefreshLayoutRl) {
            refreshLayoutRl = (RefreshLayoutRl) swipeLoadView;
        }
        mRecyclerView = findViewById(R.id.recyler_view);
        //
        TestRefreshAdapter adapter = new TestRefreshAdapter(getItems());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        //
         setRefreshLayout();

    }


    private void setRefreshLayout() {
        //下拉刷新
        if (refreshLayoutRl == null) {
            return;
        }
        // 设置下拉刷新的监听器
        refreshLayoutRl.setOnRefreshListener(new RefreshLayoutRl.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayoutRl.setRefreshing(false);
                        Logx.d("刷新-->刷新数据 完成");
                    }
                }, 10 * 1000);
            }

        });
    }

    private ArrayList<String> getItems() {
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            datas.add("item+" + i);
        }
        return datas;
    }
}
