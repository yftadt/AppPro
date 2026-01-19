package test.app.ui.activity.refresh5;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.library.baseui.utile.toast.ToastUtile;

import java.util.ArrayList;

import test.app.ui.activity.R;
import test.app.ui.adapter.test.TestRefreshAdapter;

//
public class TestRefreshActivity5 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //5
        //setContentView(R.layout.activity_test_refresh_5);
        //51
        /* setContentView(R.layout.activity_test_refresh_51);
        setListView51();*/
        //52
        setContentView(R.layout.activity_test_refresh_52);
        setListView52();
    }

    private MyNestedScrollParent51 scrollParent51;

    private void setListView51() {
        RecyclerView mRecyclerView = findViewById(R.id.recyler_view);
        if (mRecyclerView == null) {
            return;
        }
        TestRefreshAdapter adapter = new TestRefreshAdapter(getItems());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        scrollParent51 = findViewById(R.id.scroll_parent51);
        findViewById(R.id.tv_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollParent51.setRefreshingEnd();
            }
        });
        scrollParent51.setOnRefreshListener(new MyNestedScrollParent51.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtile.showToast("刷新回调");
            }
        });
        View view = LayoutInflater.from(this).inflate(R.layout.head_test_refresh, null);
        int headViewHeight = getResources().getDimensionPixelSize(R.dimen.dp_100);
        int loadViewHeight = getResources().getDimensionPixelSize(R.dimen.dp_50);
       // scrollParent51.setHeadView(view, headViewHeight, loadViewHeight);
    }

    private MyNestedScrollParent52 scrollParent52;

    private void setListView52() {
        scrollParent52 = findViewById(R.id.scroll_parent52);
        findViewById(R.id.tv_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollParent52.setRefreshingEnd();
            }
        });
        scrollParent52.setOnRefreshListener(new MyNestedScrollParent52.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToastUtile.showToast("刷新回调");
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
