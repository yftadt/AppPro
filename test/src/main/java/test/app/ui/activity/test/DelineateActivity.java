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
        binding = ActivityTestDelineateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtile.setStatusBarColor(this,true, Color.parseColor("#00000000"),false);
        binding.tvReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                binding.delineateView.setReset();
            }
        });

    }

    private void initViews() {

    }


}
