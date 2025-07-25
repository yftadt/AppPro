package com.library.baseui.utile.time;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * 小时，分钟选择
 * Created by 郭敏 on 2017/11/21 0021.
 */

public class DateTime {
    private TimePickerDialog dialog;

    public DateTime(Context context) {
        createTimePickerDialog(context, 0);
    }

    public DateTime(Context context, int style) {
        createTimePickerDialog(context, style);
    }

    public DateTime(Context context, int hour, int minute) {
        createPickerDialog(context, 0, hour, minute);
    }

    public DateTime(Context context, int style, int hour, int minute) {
        createPickerDialog(context, style, hour, minute);
    }

    private void createTimePickerDialog(Context context, int style) {
        Calendar calendar = Calendar.getInstance();
        int H = calendar.get(Calendar.HOUR_OF_DAY);
        int M = calendar.get(Calendar.MINUTE);
        createPickerDialog(context, style, H, M);
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

    private void createPickerDialog(Context context, int style, int hour, int minute) {
        //设置主题
        if (style == 0) {
            dialog = new TimePickerDialog(context, new TimeSetListener(), hour, minute, true);
            return;
        }
        dialog = new TimePickerDialog(context, style, new TimeSetListener(), hour, minute, true);

    }

    class TimeSetListener implements TimePickerDialog.OnTimeSetListener {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (onPickerDialogListener == null) {
                return;
            }
            onPickerDialogListener.onPickerTime(type, hourOfDay, minute);
        }
    }

    //监听回调
    private OnPickerDialogListener onPickerDialogListener;

    public void setOnPickerDialogListener(OnPickerDialogListener onPickerDialogListener) {
        this.onPickerDialogListener = onPickerDialogListener;
    }
}
