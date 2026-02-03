package test.app.ui.activity.test;

import android.os.Bundle;

import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityTestCanvasBinding;
import test.app.ui.activity.databinding.ActivityTestViewCustomBinding;


public class TestViewCustomActivity extends NormalActionBar {


    private ActivityTestViewCustomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestViewCustomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "画布");
        binding.view.setRound();
    }

    private void initViews() {

    }




 }
