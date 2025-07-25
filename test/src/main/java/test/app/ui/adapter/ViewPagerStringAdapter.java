package test.app.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;



import java.util.ArrayList;

import test.app.ui.pages.BaseViewPage;

public class ViewPagerStringAdapter extends PagerAdapter {
	public ArrayList<BaseViewPage> listPager = new ArrayList<BaseViewPage>();
	public ArrayList<String> titls;

	public ViewPagerStringAdapter(ArrayList<BaseViewPage> listPager,
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
