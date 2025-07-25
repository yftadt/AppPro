package test.app.ui.activity.test;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import test.app.ui.activity.R;
import test.app.ui.adapter.test.SlideLeftAdapter;
import test.app.ui.adapter.test.SlideRightAdapter;
import test.app.ui.bean.Stock;
import test.app.ui.view.SlideHorizontalScrollView;

//列表联动（类似以股票列表）
public class ListSlideActivity extends AppCompatActivity {
    //左侧固定一列数据适配
    private RecyclerView rvDataFixed;
    private List<String> leftlList;

    //右侧数据适配
    private RecyclerView rvDataSlide;
    private List<Stock> stockList;

    private SlideHorizontalScrollView titleSlide;
    private SlideHorizontalScrollView svDataSlide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_slide);
        findView();
        initData();
        initView();


    }

    private void findView() {
        titleSlide = (SlideHorizontalScrollView) findViewById(R.id.sv_title_slide);
        rvDataFixed = (RecyclerView) findViewById(R.id.rv_data_fixed);
        svDataSlide = (SlideHorizontalScrollView) findViewById(R.id.sv_data_slide);
        rvDataSlide = (RecyclerView) findViewById(R.id.rv_data_slide);
    }

    private void initView() {
        // 设置两个水平控件的联动
        titleSlide.setScrollView(svDataSlide);
        svDataSlide.setScrollView(titleSlide);
        //添加左侧数据
        SlideLeftAdapter adapter = new SlideLeftAdapter();
        adapter.setList(leftlList);
        rvDataFixed.setLayoutManager(new LinearLayoutManager(this));
        rvDataFixed.setAdapter(adapter);
        calculateRvHeight(rvDataFixed);
        // 添加右边内容数据
        SlideRightAdapter myRightAdapter = new SlideRightAdapter();
        myRightAdapter.setList(stockList);
        rvDataSlide.setLayoutManager(new LinearLayoutManager(this));
        rvDataSlide.setAdapter(myRightAdapter);
        calculateRvHeight(rvDataSlide);
    }

    /**
     * 如果不用NestedScrollView 那就要计算
     * ListView的高度
     *
     * @param listView
     */
    public void calculateRvHeight(RecyclerView listView) {
        if (true) {
            //目前用的就是 NestedScrollView
            return;
        }
        RecyclerView.LayoutManager layoutManager = listView.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        int totalHeight = 0;
        layoutManager.getItemCount();
        for (int i = 0, len = layoutManager.getItemCount(); i < len; i++) {
            //item的高度可以在xml里写死，然后可以计算出来总高度
            //总高度
            //totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //真正的高度需要加上分割线的高度
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

    //初始化数据源
    private void initData() {

        stockList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            stockList.add(new Stock("风华基金", "222", "333", "444", "555", "666", "dsd", "sdsd"));
        }
        leftlList = new ArrayList<>();
        for (int i = 0; i < stockList.size(); i++) {
            leftlList.add(stockList.get(i).getName());
        }

    }
}
