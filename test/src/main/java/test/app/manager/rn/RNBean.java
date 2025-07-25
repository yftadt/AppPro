package test.app.manager.rn;





import java.io.File;

import test.app.manager.task.BeanTask;

public class RNBean extends BeanTask {
    public File decompressToDirectory;//要解压到的文件夹
    public File decompressingFile;//要解压的文件

    public String urlPPk;//下载地址
    public String md5Name;//

    public File discardDirectory;//要删除的目录

    public int rnCode;//rn版本号
    public String rnName;//rn名称
}
