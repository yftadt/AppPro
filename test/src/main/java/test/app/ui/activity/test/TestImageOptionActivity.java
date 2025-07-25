package test.app.ui.activity.test;

import android.os.Bundle;


import test.app.ui.activity.R;
import test.app.ui.activity.base.PhotoMoreActivity;


//9张图
public class TestImageOptionActivity extends PhotoMoreActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image_option);
        setBarTvText(1, "9张图");
        initPhotoView();
    }


}
