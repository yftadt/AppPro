package test.app.utiles.permissi;

import com.hjq.permissions.OnPermissionCallback;

public interface OnPMCallback extends OnPermissionCallback {
    //已获取
    void onAcquiredAll();
    void onError();
}
