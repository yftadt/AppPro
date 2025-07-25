package test.app.ui.activity.test;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import test.app.manager.clear.ClearTask;
import test.app.manager.task.TaskManager;
import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;


//清理缓存
public class TestSettingActivity extends NormalActionBar {


    public TextView mTest1;
    public TextView mTest2;
    public TextView mTest3;
    public TextView mTest4;
    public TextView mTest5;
    public TextView mTest6;
    public TextView mTest7;
    public TextView mTest8;
    public TextView mTest9;
    public TextView mTest10;
    public TextView mTest11;
    public TextView mTest12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rn_update);
        initViews();
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "开始");
        initView();
        mTest1.setOnClickListener(this);
        mTest2.setOnClickListener(this);
        mTest3.setOnClickListener(this);
        mTest4.setOnClickListener(this);
        mTest5.setOnClickListener(this);
        mTest6.setOnClickListener(this);
        mTest7.setOnClickListener(this);
        mTest8.setOnClickListener(this);
        mTest9.setOnClickListener(this);
        mTest10.setOnClickListener(this);
        mTest11.setOnClickListener(this);
        mTest12.setOnClickListener(this);
    }

    private void initViews() {
        mTest1 = findViewById(R.id.test_1);
        mTest2 = findViewById(R.id.test_2);
        mTest3 = findViewById(R.id.test_3);
        mTest4 = findViewById(R.id.test_4);
        mTest5 = findViewById(R.id.test_5);
        mTest6 = findViewById(R.id.test_6);
        mTest7 = findViewById(R.id.test_7);
        mTest8 = findViewById(R.id.test_8);
        mTest9 = findViewById(R.id.test_9);
        mTest10 = findViewById(R.id.test_10);
        mTest11 = findViewById(R.id.test_11);
        mTest12 = findViewById(R.id.test_12);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.test_1) {
            TaskManager.getInstance().onInClearRed(new ClearTask(), 1, this);

            return;
        }
        if (id == R.id.test_2) {
            TaskManager.getInstance().onInClearDel(new ClearTask(), 1, this);
            return;
        }
        if (id == R.id.test_3) {
            TaskManager.getInstance().onInClearRed(new ClearTask(), 2, this);
            return;
        }
        if (id == R.id.test_4) {
            TaskManager.getInstance().onInClearDel(new ClearTask(), 2, this);
            return;
        }
        if (id == R.id.test_5) {
            //  /data/user/0/com.osdhc.yfbc
            TaskManager.getInstance().onInClearRootRed(new ClearTask(), this);
            return;
        }
        if (id == R.id.test_6) {
            TaskManager.getInstance().onInClearRootDel(new ClearTask(), this);
            return;
        }
        if (id == R.id.test_7) {
            //  /storage/emulated/0/Android/data/com.osdhc.yfbc
            TaskManager.getInstance().onExClearRootRed(new ClearTask(), this);

            return;
        }
        if (id == R.id.test_8) {
            TaskManager.getInstance().onExClearRootDel(new ClearTask(), this);
            return;
        }
        if (id == R.id.test_9) {
            TaskManager.getInstance().onInClearImgRed(new ClearTask(), this);

            return;
        }
        if (id == R.id.test_10) {
            TaskManager.getInstance().onInClearImgDel(new ClearTask(), this);
            return;
        }
        if (id == R.id.test_11) {
            TaskManager.getInstance().onExClearCacheRed(new ClearTask(), this);
            return;
        }
        if (id == R.id.test_12) {
            TaskManager.getInstance().onExClearCacheDel(new ClearTask(), this);

            return;
        }


    }


}
