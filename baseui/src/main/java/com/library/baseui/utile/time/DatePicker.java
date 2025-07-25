package com.library.baseui.utile.time;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;

/**
 * 年月日选择
 * Created by 郭敏 on 2017/11/21 0021.
 */

public class DatePicker {
    private DatePickerDialog dialog;
    public DatePicker(Context context) {
        createPickerDialog(context,0);
    }

    public DatePicker(Context context,int style) {
        createPickerDialog(context,style);
    }
    public DatePicker(Context context,  int Y, int M, int D) {
        createPickerDialog(context, 0,Y, M, D);
    }
    public DatePicker(Context context,int style, int Y, int M, int D) {
        createPickerDialog(context, style,Y, M, D);
    }

    // 创建获取年月日的dialog
    private void createPickerDialog(Context context,int style) {
        Calendar calendar = Calendar.getInstance();
        int Y = calendar.get(Calendar.YEAR);
        int M = calendar.get(Calendar.MONTH);
        int D = calendar.get(Calendar.DAY_OF_MONTH);
        createPickerDialog(context, style, Y, M, D);
    }

    private void createPickerDialog(Context context, int style, int Y, int M, int D) {
        //跟随主题
        if (style == 0) {
            dialog = new DatePickerDialog(context, new DateSetListener(), Y, M, D);
            return;
        }
        //设置主题
        dialog = new DatePickerDialog(context, style, new DateSetListener(), Y, M, D);

    }

    //可能要多个地方获取值，加个type标识
    private int type = 0;

    public void show() {
        show(type);
    }

    public void show(int type) {
        if (dialog == null) {
            return;
        }
        this.type = type;
        dialog.show();
    }

    //设置监听
    class DateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            if (onPickerDialogListener == null) {
                return;
            }
            onPickerDialogListener.onPickerDate(type, year, month + 1, dayOfMonth);
        }
    }

    //监听回调
    private OnPickerDialogListener onPickerDialogListener;

    public void setOnPickerDialogListener(OnPickerDialogListener onPickerDialogListener) {
        this.onPickerDialogListener = onPickerDialogListener;
    }
}
