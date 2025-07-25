package test.app.manager.clear;




import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import test.app.manager.task.BeanTask;

public class ClearBean extends BeanTask {
    //1 读取目录 2 清除文件
    public int type;
    public File[] files;
    //目录名称
    private ArrayList directorys = null;
    //文件名称
    private ArrayList filePaths = null;

    @Override
    public String toString() {
        return "ClearBean{" +
                "type=" + type +
                ", files=" + Arrays.toString(files) +
                '}';
    }

    public ArrayList getDirectorys() {
        if (directorys == null) {
            directorys = new ArrayList();
            directorys.add("shared_prefs");
            directorys.add("databases");
            directorys.add("_update_rn");
            directorys.add("code_cache");

        }
        return directorys;
    }

    public ArrayList getFilePaths() {
        if (filePaths == null) {
            filePaths = new ArrayList();
        }
        return filePaths;
    }
}
