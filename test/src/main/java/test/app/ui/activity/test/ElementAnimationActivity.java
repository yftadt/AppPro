package test.app.ui.activity.test;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import com.library.baseui.utile.app.ActivityUtile;

import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityElementAnimationBinding;
import test.app.ui.activity.databinding.ActivityTest3dBinding;


//元素动画
public class ElementAnimationActivity extends NormalActionBar {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityElementAnimationBinding binding = ActivityElementAnimationBinding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "元素动画");
        findViewById(R.id.iv_img).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startAnimation(v);
            }
        });
    }

    //共享元素动画
    private void startAnimation(View view) {
        ActivityUtile.startActivityElement(this,ElementAnimation3Activity.class,view
                ,"shared_iv");
        /*Intent intent = new Intent(this, ElementAnimation4Activity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                this, view, "shared_iv"
        ).toBundle();
        //var dataBundle = ActivityUtile.getBundle(videosRes);
        //intent.putExtras(dataBundle)
        startActivity(intent, bundle);
        ViewCompat.setTransitionName(view, "shared_iv");*/

    }

}
