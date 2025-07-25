package test.app.manager.clear;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sj.mblog.Logx;
import test.app.manager.rn.RNLog;
import test.app.manager.task.BaseTask;
import test.app.manager.task.BeanTask;

public class ClearTask extends BaseTask {
    private String tag = "清理_";
    private ClearBean clearBean = null;

    @Override
    protected void onClearCache(BeanTask bean) {
        ClearBean temp = null;
        if (bean instanceof ClearBean) {
            clearBean = (ClearBean) bean;
        }
        Logx.d(tag, "开始执行：temp=null? " + temp == null);
        if (clearBean == null) {
            return;
        }
        Logx.d(tag, "开始执行： " + clearBean.toString());
        if (clearBean.files == null || clearBean.files.length == 0) {
            return;
        }
        for (int i = 0; i < clearBean.files.length; i++) {
            File file = clearBean.files[i];
            setFileExecute(clearBean.type, file);
        }
    }


    private void setFileExecute(int type, File file) {
        if (type == 1) {
            redFiles(file);
        }
        if (type == 2) {
            onDel(file);
        }
    }

    private void onDel(File file) {
        try {
            removeDirectory(file);
            Logx.d(tag, "删除完成 " + file.getPath());
        } catch (Exception e) {
            Logx.d(tag, "删除失败 " + file.getPath());
        }
    }

    private void removeDirectory(File file) throws IOException {
        ArrayList directorys = clearBean.getDirectorys();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String name = f.getName();
                if (f.isDirectory() && directorys.contains(name)) {
                    RNLog.d(tag, "要保护的文件：" + name);
                    continue;
                }
                if (name.equals(".") || name.equals("..")) {
                    RNLog.d(tag, "隐藏文件：" + file);
                }
                removeDirectory(f);
            }
        }
        if (file.exists() && !file.delete()) {
            RNLog.d(tag, "文件存在 删除 file=" + file.toString());
        }
    }

    private void redFiles(File file) {
        Logx.d(tag, "file_name=" + file.getName() + " path=" + file.getPath());
        File[] list = file.listFiles();
        if (list == null) {
            Logx.d(tag, "file_name=" + file.getName() + " path=" + file.getPath() + " 遍历完成");
            return;
        }
        long fileSize = 0;
        for (int i = 0; i < list.length; i++) {
            File temp = list[i];
            long size = getSize(temp);
            fileSize += size;
            Logx.d(tag, "index=" + i + " file_name=" + list[i].getName() + " size=" + toUnit(size));
        }
        Logx.d(tag, "file_name=" + file.getName() + " path=" + file.getPath() + " 遍历完成:" + toUnit(fileSize));
    }

    private long getSize(File file) {
        long fileSize = 0;
        if (file.exists() && file.isFile()) {
            fileSize = file.length();
            Logx.d(tag, "文件1：" + file.getPath() + " size：" + toUnit(fileSize));
        } else {
            fileSize = getfoldersSize(file);
        }
        return fileSize;
    }

    private String toUnit(long fileSize) {
        long kb = fileSize / 1024;
        long mb = fileSize / (1024 * 1024);
        long gb = fileSize / (1024 * 1024 * 1024);
        String temp = "";
        if (gb > 0) {
            temp += gb + "G" + (mb - (gb * 1024)) + "M";
        } else if (mb > 0) {
            temp += mb + "M" + (kb - (mb * 1024)) + "kb";
        } else if (kb > 0) {
            temp += kb + "kb";
        } else {
            temp = fileSize + "byte";
        }
        return temp;
    }

    private long getfoldersSize(File file) {
        long length = 0;
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    File fil = files[i];
                    if (fil.isDirectory()) {
                        length += getfoldersSize(fil);// 递归调用计算子文件夹大小
                    } else {
                        long size = fil.length();
                        length += size; // 累加文件大小
                        Logx.d(tag, "文件2：" + fil.getPath() + " size：" + toUnit(size));
                    }
                }

            }
        }
        return length;
    }
}
