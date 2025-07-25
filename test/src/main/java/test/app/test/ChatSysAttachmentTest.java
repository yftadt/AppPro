package test.app.test;

import java.io.Serializable;
import java.util.Date;

public class ChatSysAttachmentTest implements Serializable {
    /** 附件ID */
    public String attaId;
    /** 附件名称 */
    public String attaFileName;
    /** 上传时间 */
    public Date createTime;
    /** 网络地址*/
    public String url;

    //-------------------------------
    /**是否选择*/
    public boolean isSelect;
}
