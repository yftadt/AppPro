
package test.app.ui.activity.test;

import android.app.Instrumentation;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.library.baseui.adapter.page.PagerAdapter2;
import com.library.baseui.page.BaseFragmentOld;

import java.util.ArrayList;

import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityElementAnimation4Binding;
import test.app.ui.pages.frg.FragmentTest2;


//元素动画 （viewPage2）
public class ElementAnimation4Activity extends NormalActionBar {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityElementAnimation4Binding binding = ActivityElementAnimation4Binding
                .inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "元素动画5");
        setPage();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }

    private ViewPager2 viewPager;

    private void setPage() {
        viewPager = findViewById(R.id.view_pager);
        addView();
        PagerAdapter2 adapter = new PagerAdapter2(this, frgs);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(frgs.size());
    }

    private ArrayList<Fragment> frgs = new ArrayList();

    private void addView() {
        FragmentTest2 frg = new FragmentTest2();
        frgs.add(frg);
        //frg = new FragmentTest2();
        //frgs.add(frg);
    }

    public int getIndex() {
        return viewPager.getCurrentItem();
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
