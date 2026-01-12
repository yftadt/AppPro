package test.app.ui.activity.refresh6.qrefreshlayoutdemo;

import android.os.Handler;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import test.app.ui.activity.R;
import test.app.ui.activity.refresh6.qrefreshlayout.QRefreshLayout;
import test.app.ui.activity.refresh6.qrefreshlayout.listener.RefreshHandler;


public class TestRefreshActivity63 extends AppCompatActivity {
    private QRefreshLayout refreshLayout;
    private TextView textView;
    private String mDatas;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh_63);
        refreshLayout = (QRefreshLayout) findViewById(R.id.refreshlayout);
        textView = (TextView) findViewById(R.id.textview);
        mDatas = "初始数据";
        textView.setText(mDatas);
        refreshLayout.setLoadMoreEnable(true);
        refreshLayout.setRefreshHandler(new RefreshHandler() {
            @Override
            public void onRefresh(QRefreshLayout refresh) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDatas = "下拉刷新增加的数据\n" + mDatas;
                        textView.setText(mDatas);
                        refreshLayout.refreshComplete();
                    }
                }, 5000);
            }

            @Override
            public void onLoadMore(QRefreshLayout refresh) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDatas = mDatas + "\n上拉增加的数据";
                        textView.setText(mDatas);
                        refreshLayout.loadMoreComplete();
                    }
                }, 5000);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(textView.getContext(),
                        textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
