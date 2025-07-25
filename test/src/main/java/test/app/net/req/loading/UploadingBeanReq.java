package test.app.net.req.loading;


import com.fasterxml.jackson.annotation.JsonInclude;

import test.app.net.req.BaseReq;

/**
 * 上传文件
 * Created by Administrator on 2016/9/7.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadingBeanReq extends BaseReq {
    public String service = "smarthos.system.file.upload";

    //SYS("SYS", "系统"), // 医院、科室
    // BASE("BASE", "基础"), // 广告、字典、意见建议、标准科室等其他
    // DOC("DOC", "医生"),
    //PAT("PAT", "患者"),
    //TEAM("TEAM", "团队"),
    //FOLLOW("FOLLOW", "医患关系"),
    //CONSULT("CONSULT", "咨询"),
    //APPOINTMENT("APPOINTMENT", "预约"),
    //MEDICAL("MEDICAL", "病历档案"),
    //SNS("SNS", "社交");
    public String module = "";
    //IMAGE("IMAGE", "图片"),
    //AUDIO("AUDIO", "音频文件"),
    // VIDEO("VIDEO", "视频文件"),
    // ZIP("ZIP", "压缩文件"),
    // DOC("DOC", "文档文件"),
    //TEXT("TEXT", "文本文件"), // word excel等文件
    //OTHER("OTHER", "其他文件");
    public String fileType = "";

}
