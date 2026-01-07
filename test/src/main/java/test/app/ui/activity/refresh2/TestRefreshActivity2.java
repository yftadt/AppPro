package test.app.ui.activity.refresh2;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import test.app.ui.activity.R;
import test.app.ui.adapter.test.TestRefreshAdapter;

//魔兽世界
public class TestRefreshActivity2 extends AppCompatActivity {

    private  RecyclerView rv;
    private  ElemeDetailView edv;
    private View edvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh_2);
        initView();
    }
    private void initView() {
        TestRefreshAdapter adapter = new TestRefreshAdapter(getItems());
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        //监听edv_content的位置改变，并改变edv_title的透明度
        edv = (ElemeDetailView) findViewById(R.id.edv);
        edvTitle = findViewById(R.id.edv_title);
        edv.setListener(new ElemeDetailView.Listener() {
            @Override
            public void onContentPostionChanged(float fraction) {
                edvTitle.setAlpha(1 - fraction);
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
