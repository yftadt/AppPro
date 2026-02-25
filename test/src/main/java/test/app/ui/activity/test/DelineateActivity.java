package test.app.ui.activity.test;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.library.baseui.utile.other.StatusBarUtile;

import test.app.ui.activity.action.BaseBarActivity1;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityTestCanvasBinding;
import test.app.ui.activity.databinding.ActivityTestDelineateBinding;


public class DelineateActivity extends BaseBarActivity1 {


    private ActivityTestDelineateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreen();
        //StatusBarUtile.setStatusBarColor(this,true, Color.parseColor("#00000000"),false);
        binding = ActivityTestDelineateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //重置
        binding.tvReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                binding.delineateView.setReset();
            }
        });
        //获取位置
        binding.tvWl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = binding.delineateView.getLastX();
                float y = binding.delineateView.getLastY();
                binding.tvWl.setText("x=" + x + " y=" + y);
            }
        });

    }

    private void initViews() {

    }


}
