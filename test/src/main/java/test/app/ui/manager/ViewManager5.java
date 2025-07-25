package test.app.ui.manager;

import android.graphics.Outline;
import android.os.Build;

import android.view.View;
import android.view.ViewOutlineProvider;

/**
 * 5.0的一些功能
 * Created by 郭敏 on 2018/3/29 0029.
 */
public class ViewManager5 {
    public static void seto(View view, int radius) {
        //将需要控件重写设置形状
        view.setOutlineProvider(new OutlineProvider(radius));
    }

    //设置按钮圆角
    static class OutlineProvider extends ViewOutlineProvider {
        int radius;

        public OutlineProvider(int radius) {
            this.radius = radius;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            //修改outLine的形状，这里是设置分别设置左上右下，以及Radius
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
        }
    }
}
