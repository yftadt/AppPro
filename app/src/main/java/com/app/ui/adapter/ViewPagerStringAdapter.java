package com.app.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.app.ui.pager.BasePager;

import java.util.ArrayList;

public class ViewPagerStringAdapter extends PagerAdapter {
	public ArrayList<BasePager> listPager = new ArrayList<BasePager>();
	public ArrayList<String> titls;

	public ViewPagerStringAdapter(ArrayList<BasePager> listPager,
								  ArrayList<String> titls) {
		this.listPager = listPager;
		this.titls = titls;
	}

	@Override
	public int getCount() {
		return listPager.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
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
		return titls.get(position);
	}

}
