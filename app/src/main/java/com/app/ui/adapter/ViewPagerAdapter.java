package com.app.ui.adapter;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.app.ui.pager.BaseViewPager;

import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter {
	public ArrayList<BaseViewPager> basePagers = new ArrayList<BaseViewPager>();

	public ViewPagerAdapter(ArrayList<BaseViewPager> basePagers) {
		this.basePagers = basePagers;
	}

	public void setData(ArrayList<BaseViewPager> basePagers) {
		this.basePagers = basePagers;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return basePagers.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (position < getCount()) {
			View view = basePagers.get(position).getView();
			container.removeView(view);
		}
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		View view = basePagers.get(position).getView();
		container.addView(view);
		return view;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
