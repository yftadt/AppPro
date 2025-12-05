package test.app.ui.activity.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.library.baseui.adapter.page.PagerAdapter2;

import java.util.ArrayList;

import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.pages.frg.FragmentTest4;
import test.app.ui.pages2.adapter.PagerAdapter3;
import test.app.ui.pages2.main.TestMain1Frg;
import test.app.ui.pages2.main.TestMain2Frg;

public class TestPageActivity extends NormalActionBar {

    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_page);
        /*setBarColor();
        setBarTvText(0, "返回1");
        setBarTvText(1, "开始1");*/
        viewPager = findViewById(R.id.view_pager);
        setView();
    }

    private void setView() {
        addFrg();
        PagerAdapter3 adapter3 = new PagerAdapter3(this);
        adapter3.setFrgs(frgs);
        viewPager.setAdapter(adapter3);
        viewPager.setOffscreenPageLimit(frgs.size());
        viewPager.setUserInputEnabled(true);
    }

    private ArrayList<Fragment> frgs = new ArrayList<>();

    private void addFrg() {
        frgs.add(new TestMain1Frg());
        frgs.add(new TestMain2Frg());
    }
}
