package test.app.net.req;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.File;

/**上传文件
 * Created by Administrator on 2016/9/7.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
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
