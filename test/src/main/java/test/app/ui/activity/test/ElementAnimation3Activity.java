
package test.app.ui.activity.test;

import android.app.Instrumentation;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityElementAnimation3Binding;
import test.app.ui.pages.frg.FragmentTest3;
import test.app.ui.pages.frg.FragmentTest4;


//元素动画 （frg）
public class ElementAnimation3Activity extends NormalActionBar {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityElementAnimation3Binding binding = ActivityElementAnimation3Binding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "元素动画3");

        setView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void setView() {

        FragmentTest3 frg = new FragmentTest3();
        //
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.rl_content, frg);
        transaction.commit();

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
