package test.app.ui.activity.refresh5;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import test.app.ui.activity.R;

//
public class TestRefreshActivity5 extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh_5);
        //View childScroll = findViewById(R.id.child_scroll);
        //View childScroll = findViewById(R.id.child_scroll);
        //initView();
    }

    private ArrayList<String> getItems() {
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            datas.add("item+" + i);
        }
        return datas;
    }
}
