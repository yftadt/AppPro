package com.app.utiles.other;

import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.app.ui.activity.base.BaseApplication;

/**
 * Created by Admin on 2016/8/15.
 */
public class TextViewUtile {

  /**
   * @param tv
   * @param iconId
   * @param text
   * @param iconLocation 位置
   */
  public static void setText(TextView tv, int iconId, String text, int iconLocation) {
    if (iconId != 0) {
      Drawable drawable = BaseApplication.context.getResources().getDrawable(iconId);
      drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
      switch (iconLocation) {
        case 0:
          tv.setCompoundDrawables(drawable, null, null, null);
          break;
        case 1:
          tv.setCompoundDrawables(null, drawable, null, null);
          break;
        case 2:
          tv.setCompoundDrawables(null, null, drawable, null);
          break;
        case 3:
          tv.setCompoundDrawables(null, null, null, drawable);
          break;
      }
    }
    tv.setText(text);
  }
}
