package test.app.ui.view.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;


import com.library.baseui.view.page.ViewPagerScroller;

import java.util.ArrayList;
import java.util.List;

import test.app.ui.activity.R;
import test.app.utiles.other.DLog;

/**
 * Created by Administrator on 2017/3/14.
 */

public class BannerRl extends RelativeLayout {
    private ViewPager vp;
    private ViewPagerScroller viewPagerScroller;
    private BannerAdapter adapter;
    private LinearLayout ll;

    public BannerRl(Context context) {
        super(context);
        init();
    }

    public BannerRl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BannerRl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //在xml里引用要设置高度160dp，直接new 要调用setBannerParams（）方法
    public void init() {
        Context context = getContext();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        //
        int vHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, dm);
        //setBannerParams(vHeight);
        //
        vp = new ViewPager(context);
        LayoutParams lpv = new LayoutParams(LayoutParams.MATCH_PARENT, vHeight);
        vp.setLayoutParams(lpv);
        adapter = new BannerAdapter(context);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new PagerChange());
        this.addView(vp);
        //
        viewPagerScroller = new ViewPagerScroller(context);
        viewPagerScroller.initViewPagerScroll(vp);
        vp.setOnTouchListener(new TouchListener());
        //
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, dm);
        ll = new LinearLayout(context);
        LayoutParams lpll = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        lpll.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lpll.bottomMargin = margin;
        lpll.leftMargin = margin;
        lpll.rightMargin = margin;
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setLayoutParams(lpll);
        this.addView(ll);
        //

    }


    public void setBannerParams() {
        Context context = getContext();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, dm);
        setBannerParams(height);
        ViewGroup.LayoutParams lpv = vp.getLayoutParams();
        lpv.height = height;
    }

    private void setBannerParams(int height) {
        int type = -1;
        ViewParent view = getParent();
        if (view instanceof LinearLayout) {
            type = 1;
        }
        if (view instanceof RelativeLayout) {
            type = 2;
        }
        if (view instanceof AbsListView) {
            type = 3;
        }
        switch (type) {
            case 1:
                LinearLayout.LayoutParams linearLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
                setLayoutParams(linearLp);
                break;
            case 2:
                LayoutParams setRelativeLp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
                setLayoutParams(setRelativeLp);
                break;
            case 3:
                AbsListView.LayoutParams abslistLp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
                setLayoutParams(abslistLp);
                break;
        }
    }

    private SwipeRefreshLayout swipeLayout;

    public void setSwipeLayout(SwipeRefreshLayout swipeLayout) {
        this.swipeLayout = swipeLayout;
    }

    private int imagesSize;

    /*private List<Binner> images;

    public void setImages(List<Binner> images) {
        this.images = images;
        if (images == null) {
            return;
        }
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String image = images.get(i).image;
            urls.add(image);
        }
        setData(urls);
    }*/
    public void setIamges() {
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add("http://pic.58pic.com/58pic/16/62/63/97m58PICyWM_1024.jpg");
        imageUrls.add("http://up.qqjia.com/z/17/tu17742_2.jpg");
        imageUrls.add("http://t1.niutuku.com/960/24/24-617747.jpg");
        setData(imageUrls);
    }

    //设置数据
    private void setData(List<String> urls) {
        if (urls == null) {
            return;
        }
        imagesSize = urls.size();
        if (slideHandler != null) {
            slideHandler.removeMessages(slide);
        }
        adapter.setImages(urls);
        createCircle();
        if (imagesSize == 0) {
            return;
        }
        if (slideHandler == null) {
            slideHandler = new SlideHandler();
        }
        slideHandler.sendEmptyMessageDelayed(slide, slideTime);
    }

    private List<ImageView> ivs = new ArrayList<>();

    private void createCircle() {
        Context context = getContext();
        int selctIndex = vp.getCurrentItem();
        ll.removeAllViews();
        ivs.clear();
        if (imagesSize < 2) {
            return;
        }
        for (int i = 0; i < imagesSize; i++) {
            ImageView iv = new ImageView(context);
            iv.setId(i);
            ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
            iv.setScaleType(localScaleType);
            LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
                    24, 24);
            iv.setLayoutParams(localLayoutParams);
            iv.setPadding(5, 5, 5, 5);
            iv.setImageResource(R.drawable.banner_circle_icon);
            ll.addView(iv);
            ivs.add(iv);
            if (selctIndex == i) {
                iv.setSelected(true);
            }
        }
    }

    private long startTime;
    private float x1;

    class TouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    startTime = System.currentTimeMillis();
                    x1 = event.getX();
                    if (slideHandler == null) {
                        break;
                    }
                    slideHandler.removeMessages(slide);
                    swipeLayout.setEnabled(false);
                    viewPagerScroller.setScrollDuration(500);
                    break;
                case MotionEvent.ACTION_HOVER_MOVE:
                    float x2 = event.getX();
                    float x = x2 - x1;
                    if (Math.abs(x) >= 5) {
                        startTime = 0;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    onClick(System.currentTimeMillis());
                    if (slideHandler == null) {
                        break;
                    }
                    if (imagesSize < 2) {
                        break;
                    }
                    slideHandler.sendEmptyMessageDelayed(slide, slideTime);
                    swipeLayout.setEnabled(true);
                    break;
            }
            return false;
        }
    }

    class PagerChange implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            startTime = 0;
        }

        @Override
        public void onPageSelected(int position) {
            if (imagesSize < 2) {
                return;
            }
            for (int i = 0; i < imagesSize; i++) {
                if (i == position) {
                    ivs.get(i).setSelected(true);
                    continue;
                }
                ivs.get(i).setSelected(false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private final int slide = 100;
    private final int slideTime = 3 * 1000;
    private SlideHandler slideHandler;

    class SlideHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int item = vp.getCurrentItem();
            int index = (item + 1) % imagesSize;
            viewPagerScroller.setScrollDuration(2000);
            vp.setCurrentItem(index);
            sendEmptyMessageDelayed(slide, slideTime);
        }
    }


    public void onClick(long endTime) {
        long time = endTime - startTime;
        if (time > 100) {
            return;
        }
        int index = vp.getCurrentItem();
        DLog.e("点击事件", "=========" + index);

    }

}
