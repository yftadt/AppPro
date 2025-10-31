package com.library.baseui.adapter.page;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.library.baseui.page.BaseFragmentOld;

import java.util.List;

public class PagerAdapter2 extends FragmentStateAdapter {
    private List<Fragment> list;

    public PagerAdapter2(@NonNull FragmentActivity fragmentActivity, List<Fragment> list) {
        super(fragmentActivity);
        this.list = list;
    }

    public void setData(List<Fragment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public PagerAdapter2(@NonNull Fragment fragment, List<Fragment> list) {
        super(fragment);
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }

    public void updateItem(int position) {
        notifyItemChanged(position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

   /* @Override
    public long getItemId(int position) {
        long id = list.get(position).getId(position);
        // Logx.d("====> position="+position+" id="+id)
        return id;
    }

    @Override
    public boolean containsItem(long itemId) {
        for (int i = 0; i < list.size(); i++) {
            long id = list.get(i).getId(i);
            if (id == itemId) {
                return true;
            }
        }
        return false;
    }*/
}
