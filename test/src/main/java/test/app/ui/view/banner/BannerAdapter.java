package test.app.ui.view.banner;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.library.baseui.utile.img.ImageLoadingUtile;

import java.util.ArrayList;
import java.util.List;

import test.app.ui.activity.R;


class BannerAdapter extends PagerAdapter {
    private List<Image> imageViews = new ArrayList<>();
    private Context context;

    public BannerAdapter(Context context) {
        this.context = context;
    }

    public void setImages(List<String> images) {
        imageViews.clear();
        for (int i = 0; i < images.size(); i++) {
            Image image = new Image(images.get(i),i);
            imageViews.add(image);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = imageViews.get(position).getView();
        container.addView(view);
        return view;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position < getCount()) {
            View view = imageViews.get(position).getView();
            container.removeView(view);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    public class Image {
        public String url;
        private ImageView iv;
        private int id;

        public Image(String url, int id) {
            this.url = url;
            this.id = id;
        }

        public View getView() {
            if (iv != null) {
                return iv;
            }
            iv = new ImageView(context);
            iv.setId(id);
            ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
            iv.setScaleType(localScaleType);
            ViewPager.LayoutParams localLayoutParams = new ViewPager.LayoutParams();
            localLayoutParams.width = 400;
            localLayoutParams.height = 400;
            iv.setLayoutParams(localLayoutParams);
            ImageLoadingUtile.loading(context, url, R.mipmap.default_image, iv);
            return iv;
        }
    }

}
