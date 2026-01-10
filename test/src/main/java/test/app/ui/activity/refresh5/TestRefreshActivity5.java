package test.app.ui.activity.refresh5;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import test.app.ui.activity.R;
import test.app.ui.adapter.test.TestRefreshAdapter;

//
public class TestRefreshActivity5 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh_51);
        //View childScroll = findViewById(R.id.child_scroll);
        //View childScroll = findViewById(R.id.child_scroll);
        //initView();
        setListView();
    }

    private void setListView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyler_view);
        if (mRecyclerView == null) {
            return;
        }
        TestRefreshAdapter adapter = new TestRefreshAdapter(getItems());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    private ArrayList<String> getItems() {
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            datas.add("item+" + i);
        }
        return datas;
    }
}
