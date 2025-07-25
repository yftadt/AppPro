package test.app.ui.adapter;




import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.library.baseui.page.BaseCompatPageFragment;

import java.util.ArrayList;


public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    public ArrayList<BaseCompatPageFragment> basePagers = new ArrayList();

    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentViewPagerAdapter(FragmentManager fm, ArrayList<BaseCompatPageFragment> basePagers) {
        super(fm);
        this.basePagers = basePagers;
    }

    public void setData(ArrayList<BaseCompatPageFragment> basePagers) {
        this.basePagers = basePagers;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return basePagers.get(position);
    }

    @Override
    public int getCount() {
        return basePagers.size();
    }

}
