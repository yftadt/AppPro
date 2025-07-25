package test.app.net.res.loading;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttaRes implements Serializable {
    /*业务类型*/
    public String attaModelType;
    /*业务id*/
    public String attaModelId;
    /*文件名*/
    public String attaFileName;
    /*文件url*/
    public String attaFileUrl;
    /* 附件ID */
    public String id;
    public String image;
    //---------------------
    /**
     * 0 完成状态
     * 1 上传中
     * 2 出错
     */
    public int upState = 0;
    //上传时间戳加文件本地路径
    public String upId;
    //本地路径
    public String localUrl;

    //原图
    public String imagePathSource;

    public boolean needDetele = true;

    public String getImage() {
        if (!TextUtils.isEmpty(attaFileUrl)) {
            return attaFileUrl;
        }
        return localUrl;
    }

    public String getUrl() {
        if (TextUtils.isEmpty(image)) {
            image = attaFileUrl;
        }
        return image;
    }
}
