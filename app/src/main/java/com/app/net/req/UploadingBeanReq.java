package com.app.net.req;

import java.io.File;

/**上传文件
 * Created by Administrator on 2016/9/7.
 */
public class UploadingBeanReq extends BaseReq {
    public String service = "yidao.file.upload";
    //文件
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
