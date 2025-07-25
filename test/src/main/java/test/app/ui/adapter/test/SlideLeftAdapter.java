package test.app.ui.adapter.test;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import test.app.ui.activity.R;


public class SlideLeftAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


	public SlideLeftAdapter() {
		super(R.layout.item_slide_data_fixed);
	}

	@Override
	protected void convert(@NonNull BaseViewHolder baseViewHolder, String data) {
		TextView textView = (TextView) baseViewHolder.getView(R.id.left_container_textview0);
		 textView.setText(data);
	}



}
