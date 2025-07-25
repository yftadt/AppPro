package test.app.ui.view.images;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/1.
 */

public class ImagePath implements Serializable{
    //图片path
    public String path;
    //图片url
    public String pathSource;
    //图片id
    public String urlId;
    //图片url
    public String url;

    public ImagePath() {

    }

    public ImagePath(String pathSource, String path) {
        this.pathSource = pathSource;
        if (path == null) {
            path = "";
        }
        if (path.startsWith("http://") || path.startsWith("https://")) {
            this.url = path;
            return;
        }
        this.path = path;
    }


    public void setUrl(String urlId, String url) {
        this.urlId = urlId;
        this.url = url;
    }

    public String getShowUrl() {
        String p = url;
        if (TextUtils.isEmpty(p)) {
            p = path;
        }
        return p;
    }

    public String getShowSource() {
        String p = pathSource;
        if (TextUtils.isEmpty(p)) {
            p = path;
        }
        if (TextUtils.isEmpty(p)) {
            p = url;
        }
        return p;
    }
}
