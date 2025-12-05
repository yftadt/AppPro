package test.app.ui.pages2.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

import test.app.ui.activity.R;
import test.app.ui.pages.frg.FragmentTest4;
import test.app.ui.pages2.adapter.PagerAdapter3;

public class TestMain1Frg extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_page_main_1, container, false);
        return view;
    }

    private ViewPager2 viewPager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.view_pager_vertical);
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
        frgs.add(new TestVertical1Frg());
        frgs.add(new TestVertical2Frg());
    }
}
