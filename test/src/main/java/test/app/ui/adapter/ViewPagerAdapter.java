package test.app.ui.adapter;


 import android.view.View;
import android.view.ViewGroup;

 import androidx.viewpager.widget.PagerAdapter;



import java.util.ArrayList;

 import test.app.ui.pages.BaseViewPage;


public class ViewPagerAdapter extends PagerAdapter {
	public ArrayList<BaseViewPage> basePagers = new ArrayList<BaseViewPage>();

	public ViewPagerAdapter(ArrayList<BaseViewPage> basePagers) {
		this.basePagers = basePagers;
	}

	public void setData(ArrayList<BaseViewPage> basePagers) {
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
