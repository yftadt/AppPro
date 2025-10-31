package test.app.ui.activity.test;

import android.app.ActivityOptions;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityElementAnimation2Binding;
import test.app.ui.activity.databinding.ActivityElementAnimationBinding;


//元素动画
public class ElementAnimation2Activity extends NormalActionBar {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityElementAnimation2Binding binding = ActivityElementAnimation2Binding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "元素动画2");
        findViewById(R.id.tv_msg).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //调用this.finish()不会有共享元素转场退出效果
                //onBackPressed();
                finishAfterTransition();
            }
        });
    }

    //https://blog.csdn.net/u013077428/article/details/126484571
    @Override
    protected void onStop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !isFinishing()) {
            new Instrumentation().callActivityOnSaveInstanceState(this, new Bundle());
        }
        super.onStop();
    }
}
