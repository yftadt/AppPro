package test.app.ui.adapter.test;

import android.widget.TextView;

import androidx.annotation.NonNull;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import test.app.ui.activity.R;
import test.app.ui.bean.Stock;

public class SlideRightAdapter extends BaseQuickAdapter<Stock, BaseViewHolder> {


    public SlideRightAdapter() {
        super(R.layout.item_slide_data);

    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Stock stock) {
        TextView textView1 = (TextView) baseViewHolder.getView(R.id.right_item_textview0);
        TextView textView2 = (TextView) baseViewHolder.getView(R.id.right_item_textview1);
        TextView textView3 = (TextView) baseViewHolder.getView(R.id.right_item_textview2);
        TextView textView4 = (TextView) baseViewHolder.getView(R.id.right_item_textview3);
        TextView textView5 = (TextView) baseViewHolder.getView(R.id.right_item_textview4);
        TextView textView6 = (TextView) baseViewHolder.getView(R.id.right_item_textview5);
        TextView textView7 = (TextView) baseViewHolder.getView(R.id.right_item_textview6);
        //
        textView1.setText(stock.getTxt1());
        textView2.setText(stock.getTxt2());
        textView3.setText(stock.getTxt3());
        textView4.setText(stock.getTxt4());
        textView5.setText(stock.getTxt5());
        textView6.setText(stock.getTxt6());
        textView7.setText(stock.getTxt7());

    }


}
