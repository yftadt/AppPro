package test.app.ui.activity.test;

import android.os.Bundle;



import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityTest3dBinding;


//测试3D
public class Test3DActivity extends NormalActionBar {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTest3dBinding binding = ActivityTest3dBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "view3d");
    }


}
