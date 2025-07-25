package test.app.ui.activity.test;

import android.os.Bundle;


import test.app.ui.activity.R;
import test.app.ui.activity.action.BaseBarActivity1;


//
public class TestGestureBackActivity extends BaseBarActivity1 {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setGestureBack(true);
        setContentView(R.layout.activity_test_gesture_back);

    }


    @Override
    public void onBackPressed() {
        finish();
        activityTranslucentLeftIn();
    }

}
