package com.app.utiles.time;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * 选择时间的dialog
 * Created by Administrator on 2016/5/23.
 */
public class DateTimeDialog {
    public DateTimeDialog(Context context) {

    }

    // 创建获取年月日的dialog
    public void createPickerDialog(Context context) {
        Calendar calendar = Calendar.getInstance();
        int Y = calendar.get(Calendar.YEAR);
        int M = calendar.get(Calendar.MONTH);
        int D = calendar.get(Calendar.DAY_OF_MONTH);
        getTime(context, Y, M, D);
    }

    public void createDatePickerDialog(Context context, int Y,
                                       int M, int D) {
        getTime(context, Y, M, D);
    }

    private DatePickerDialog dataTime;

    public void DateDatePickerDialog() {
        if (dataTime == null) {
            return;
        }
        dataTime.show();
    }

    // 获取时间
    private void getTime(Context context, int Y,
                         int M, int D) {
        DatePickerDialog dataTime = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (onPickerDialogListener == null) {
                            return;
                        }
                        onPickerDialogListener.onPickerDate(year, monthOfYear + 1, dayOfMonth);

                    }
                }, Y, M, D);

    }

    private TimePickerDialog timePickerDialog;

    public void timePickerDialogShow() {
        if (timePickerDialog == null) {
            return;
        }
        timePickerDialog.show();
    }

    /**
     * 时间选择
     */
    public void createTimePickerDialog(Context context) {
        Calendar calendar = Calendar.getInstance();
        int H = calendar.get(Calendar.HOUR_OF_DAY);
        int M = calendar.get(Calendar.MINUTE);
        getTimeDate(context, H, M);
    }

    public void createTimePickerDialog(Context context, int H, int M) {
        getTimeDate(context, H, M);
    }

    public void getTimeDate(Context context, int hour, int minute) {
        timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (onPickerDialogListener == null) {
                            return;
                        }
                        onPickerDialogListener.onPickerTime(hourOfDay, minute);
                    }
                }, hour, minute, true);

    }

    private OnPickerDialogListener onPickerDialogListener;

    public void setOnPickerDialogListener(OnPickerDialogListener onPickerDialogListener) {
        this.onPickerDialogListener = onPickerDialogListener;
    }

    public interface OnPickerDialogListener {
        public void onPickerDate(int y, int m, int d);

        public void onPickerTime(int h, int m);
    }
}
