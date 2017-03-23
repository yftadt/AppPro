package com.app.ui.view.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.app.ui.activity.R;

/**
 * 选择
 * Created by Administrator on 2016/9/14.
 */
public class OptionPopupWindow extends PopupWindow implements AdapterView.OnItemClickListener
        , PopupWindow.OnDismissListener {

    public OptionPopupWindow(Context context, ListAdapter adapter) {
        View view = LayoutInflater.from(context).inflate(R.layout.option_popup_list, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //点击空白处时，隐藏掉pop窗口
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        ListView listLv = (ListView) view.findViewById(R.id.lv);
        listLv.setAdapter(adapter);
        listLv.setOnItemClickListener(this);
        setOnDismissListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (listener != null) {
            listener.onPopupSelectListener(adapterView, view, i, l);
        }
        dismiss();
    }

    @Override
    public void onDismiss() {
        if (activity != null) {
            backgroundAlpha(activity, 1f);
            activity = null;
        }
    }

    private Activity activity;

    public void showDown(Activity activity, View view) {
        showAsDropDown(view);
        if (activity != null) {
            this.activity = activity;
            backgroundAlpha(activity, 0.5f);
        }
    }


    public void showLocation(Activity activity, View view, int location) {
        showAtLocation(view, location, 0, 0);
        if (activity != null) {
            this.activity = activity;
            backgroundAlpha(activity, 0.5f);
        }
    }

    private OnPopupSelectListener listener;

    public void setOnPopupSelectListener(OnPopupSelectListener listener) {
        this.listener = listener;
    }

    public interface OnPopupSelectListener {
        public void onPopupSelectListener(AdapterView<?> adapterView, View view, int i, long l);
    }

    private void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }
}
