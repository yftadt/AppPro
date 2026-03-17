package test.app.ui.activity.test;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.library.baseui.utile.app.ActivityUtile;
import com.library.baseui.utile.img.ImageLoadingUtile;
import com.library.baseui.utile.other.StatusBarUtile;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import media.library.images.config.entity.MediaEntity;
import test.app.ui.activity.action.BaseBarActivity1;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityTestCanvasBinding;
import test.app.ui.activity.databinding.ActivityTestDelineateBinding;
import test.app.ui.activity.test.img.ImgOptAct;
import test.app.ui.event.EventImgData;

//获取 画布上的坐标
public class DelineateActivity extends BaseBarActivity1 {


    private ActivityTestDelineateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreen();
        //StatusBarUtile.setStatusBarColor(this,true, Color.parseColor("#00000000"),false);
        binding = ActivityTestDelineateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //重置
        binding.tvReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                binding.delineateView.setReset();
            }
        });
        //获取位置
        binding.tvWl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = binding.delineateView.getLastX();
                float y = binding.delineateView.getLastY();
                binding.tvWl.setText("x=" + x + " y=" + y);
            }
        });
        binding.tvImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ActivityUtile.startActivityString(ImgOptAct.class, "3", "1");
            }
        });
        //
        setEventBusRegistered();

    }

    private void initViews() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventImgData eventType) {
        ArrayList<MediaEntity> imgs = eventType.imgs;
        if (imgs != null && imgs.size() > 0) {
            MediaEntity bean = imgs.get(0);
            ImageLoadingUtile.loading(this, bean.mediaPathSource, binding.ivImg);
        }
    }


}
