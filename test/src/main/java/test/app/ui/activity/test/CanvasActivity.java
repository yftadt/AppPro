package test.app.ui.activity.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

import sj.mblog.Logx;
import test.app.lang.LangManager;
import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.activity.databinding.ActivityTestCanvasBinding;
import test.app.ui.activity.databinding.ActivityTestLangBinding;



public class CanvasActivity extends NormalActionBar {


    private ActivityTestCanvasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestCanvasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBarColor();
        setBarTvText(0, "返回");
        setBarTvText(1, "画布");

    }

    private void initViews() {

    }




 }
