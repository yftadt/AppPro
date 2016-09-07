package com.app.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.app.ui.pager.BasePager;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/3/29.
 */
public class ViewPagerIconAdapter extends PagerAdapter implements IconPagerAdapter {
    public ArrayList<BasePager> listPager = new ArrayList<BasePager>();
    public ArrayList<Integer> listIcon = new ArrayList<Integer>();

    public ViewPagerIconAdapter(ArrayList<BasePager> listPager, ArrayList<Integer> listIcon) {
        this.listPager = listPager;
        this.listIcon = listIcon;
    }

    public void setIcon(ArrayList<Integer> listIcon) {
        this.listIcon = listIcon;
        notifyDataSetChanged();
    }

    @Override
    public int getIconResId(int index) {
        return listIcon.get(index);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = listPager.get(position).getView();
        container.removeView(view);
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = listPager.get(position).getView();
        container.addView(view);
        return view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return listPager.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}

