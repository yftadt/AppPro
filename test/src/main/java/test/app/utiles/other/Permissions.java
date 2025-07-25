package test.app.utiles.other;

import android.Manifest;
import android.text.TextUtils;

/**
 * Created by 郭敏 on 2018/1/3 0003.
 */

public class Permissions {
    public static final String permission_record = Manifest.permission.RECORD_AUDIO;
    public static final String permission_location = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String permission_camera = Manifest.permission.CAMERA;

    //解释权限的使用
    public static String getPermissionHint(String permission) {
        String hint = "";
        if (TextUtils.isEmpty(permission)) {
            return hint;
        }
        switch (permission) {
            case permission_camera:
                hint = "没有此权限，相机无法启动";
                break;
            case permission_record:
                hint = "没有此权限，无法使用语音";
                break;
        }
        return hint;
    }
}
