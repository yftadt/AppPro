package test.app.ui.activity.test;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.library.baseui.utile.app.ActivityUtile;
import com.library.baseui.utile.file.DataSave;
import com.library.baseui.utile.other.StringUtile;

import test.app.net.manager.account.LoginManager;
import test.app.ui.activity.MainActivity;
import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.view.errorlayout.AccountEditLayout;


public class TestLoginActivity extends NormalActionBar implements View.OnClickListener {
    AccountEditLayout loginPhoneView;
    AccountEditLayout loginPwdView;
    TextView loginTv;
    private TextChangeListener textChangeListener = new TextChangeListener();
    private LoginManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);
        loginPhoneView = (AccountEditLayout) findViewById(R.id.login_phone_view);
        loginPwdView = (AccountEditLayout) findViewById(R.id.login_pwd_view);
        loginTv = (TextView) findViewById(R.id.login_tv);
        setBarTvText(1, "登录");

        //
        loginPhoneView.setTypeInput(1);
        loginPwdView.setTypeInput(2);
        //
        String phone = DataSave.stringGet(DataSave.LOGIN_NAME);
        loginPhoneView.setEditText(phone);
        loginPhoneView.setOnTextChangeListener(textChangeListener);
        //
        String pwd = DataSave.stringGet(DataSave.LOGIN_PWD);
        loginPwdView.setEditText(pwd);
        loginPwdView.setOnTextChangeListener(textChangeListener);
        onTextChanged("", 0, 0, 0);
        loginTv.setOnClickListener(this);
    }

    @Override
    protected void onClick(int id) {
        super.onClick(id);
        if(id==R.id.login_forget_pwd_tv){
            //忘记密码
            return;
        }
        if(id==R.id.login_tv){
            if (!loginTv.isSelected()) {
                return;
            }
            if (manager == null) {
                manager = new LoginManager(this);
            }
            String phone = loginPhoneView.getEditText();
            String pwd = loginPwdView.getEditText();
            boolean isOk = true;
            if (!StringUtile.isPhone(phone)) {
                loginPhoneView.setErrorMsg(1);
                isOk = false;
            }
            if (TextUtils.isEmpty(pwd) || pwd.length() < 6) {
                loginPwdView.setErrorMsg(2);
                isOk = false;
            }
            if (!isOk) {
                return;
            }
            manager.setData("18868714254", "123456");
            dialogShow();
            manager.doRequest();
            return;
        }

    }


    @Override
    public void onBack(int what, Object obj, String msg, String other) {
        switch (what) {
            case LoginManager.WHAT_DEAL_SUCCEED:
                //登录成功
                ActivityUtile.startActivityCommon(MainActivity.class);
                finish();
                break;
        }
        dialogDismiss();
        super.onBack(what, obj, msg, "");
    }

    @Override
    protected void onTextChanged(CharSequence s, int start, int before, int count) {
        //按钮高亮
        String phone = loginPhoneView.getEditText();
        String pwd = loginPwdView.getEditText();
        boolean isPwdOk = !TextUtils.isEmpty(pwd) && pwd.length() >= 6;
        boolean isPhone = StringUtile.isPhone(phone);
        if (isPwdOk && isPhone) {
            loginTv.setSelected(true);
        } else {
            loginTv.setSelected(false);
        }
    }


}
