package test.app.ui.event;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */

public class BaseEvent {
    public Class<?> cls;
    public List<String> clsNames;

    public boolean toCompareTag(Object c) {
        return toCompareTag(c.getClass().getName());
    }
    public boolean toCompareTag(Class<?> c) {
        return toCompareTag(c.getName());
    }

    public boolean toCompareTag(String name) {
        boolean isTag = false;
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (cls != null) {
            isTag = name.equals(cls.getName());
        }
        if (!isTag && clsNames != null) {
            isTag = clsNames.contains(name);
        }
        return isTag;
    }

    public void setClsName(Class<?>... c) {
        if (clsNames == null) {
            clsNames = new ArrayList<>();
        }
        for (int i = 0; i < c.length; i++) {
            clsNames.add(c[i].getName());
        }
    }
}
