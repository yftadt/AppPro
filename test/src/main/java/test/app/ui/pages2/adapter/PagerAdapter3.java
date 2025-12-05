package test.app.ui.pages2.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;


public class PagerAdapter3 extends FragmentStateAdapter {
    private ArrayList<Fragment> frgs = new ArrayList<>();
    //数据数量
    private int dataSize;
    //true 无限翻页
    private boolean isInfinite;

    public PagerAdapter3(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

    }

    public PagerAdapter3(@NonNull FragmentActivity fragmentActivity, int dataSize) {
        super(fragmentActivity);
        this.dataSize = dataSize;
    }

    public PagerAdapter3(@NonNull Fragment fragment) {
        super(fragment);
    }

    public PagerAdapter3(@NonNull Fragment fragment, int dataSize) {
        super(fragment);
        this.dataSize = dataSize;
    }

    public void setDataSize(int dataSize) {
        if (this.dataSize == dataSize) {
            return;
        }
        setDataCount(dataSize, false);
    }

    //设置循环
    public void setDataLoop() {
        if (isInfinite) {
            return;
        }
        setDataCount(dataSize, true);
    }

    public void setDataCount(int dataSize, boolean isInfinite) {
        if (this.dataSize == dataSize && this.isInfinite == isInfinite) {
            return;
        }
        this.dataSize = dataSize;
        this.isInfinite = isInfinite;
        notifyDataSetChanged();
    }

    public void setFrgs(ArrayList<Fragment> frgs) {
        this.frgs = frgs;
        this.dataSize = frgs.size();
    }

    public void setData(ArrayList<Fragment> frgs) {
        this.frgs = frgs;
        this.dataSize = frgs.size();
        notifyDataSetChanged();
    }

    public void updateItem(int position) {
        notifyItemChanged(position);
    }


    private Fragment getFrg(int position) {
        int index = position % frgs.size();
        Fragment frg = frgs.get(index);
        return frg;
    }

    public Fragment getIndexAtFrg(int index) {
        Fragment frg = getFrg(index);
        return frg;
    }

    public int getFrgSize() {
        return frgs.size();
    }

    public int getDataSize() {
        return dataSize;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment frg = getFrg(position);
        return frg;
    }

    @Override
    public long getItemId(int position) {
        Fragment frg = getFrg(position);
        return frg.hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        for (int i = 0; i < frgs.size(); i++) {
            Fragment frg = frgs.get(i);
            if (frg.hashCode() == itemId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        if (frgs == null || frgs.size() == 0) {
            return 0;
        }
        if (isInfinite) {
            return Integer.MAX_VALUE;
        }
        if (dataSize < 0) {
            dataSize = 0;
        }
        return dataSize;
    }

    //true 页面无限
    public boolean isInfinite() {
        return isInfinite;
    }
}
