package test.app.manager.rn;

import android.content.Context;
import android.text.TextUtils;


import com.library.baseui.utile.file.DataSave;
import com.library.baseui.utile.other.NumberUtile;


import java.io.File;
import java.util.HashMap;

import test.app.manager.task.TaskManager;

public class RNUpdateManager {
    private String tag = "RNUpdateManager";
    private String runPath = "";

    //获取RN你版本号
    public String getRnVersionName(Context context) {

        String rnVersion = DataSave.stringGet(DataSave.rn_version_name);
        if (TextUtils.isEmpty(rnVersion)) {
            // rnVersion = AppUtils.getAppVersionName(context);
        }
        return rnVersion;
    }

    //获取RN你版本号
    public int getRnVersionCode(Context context) {
        String temp = DataSave.stringGet(DataSave.rn_version_code);
        int rnVersion = NumberUtile.getStringToInt(temp, -1);
        if (rnVersion == -1) {
            //rnVersion = AppUtils.getAppVersionCode(context);
        }
        return rnVersion;
    }

    //true 获取都的版本>本地版本
    public boolean isVersionUpdate(String rnCode, Context context) {
        RNLog.d(tag, "rnCode=" + rnCode);

        int nowVersion = getRnVersionCode(context);
        RNLog.d(tag, "nowVersion=" + nowVersion);
        int updateCode = getVersionCode(rnCode);
        RNLog.d(tag, "nowVersion=" + nowVersion + " updateCode=" + updateCode);
        return updateCode > nowVersion;
    }

    private int getVersionCode(String rnCode) {
        if (TextUtils.isEmpty(rnCode)) {
            return 0;
        }
        rnCode = rnCode.replace("v", "");
        rnCode = rnCode.replace("V", "");
        rnCode = rnCode.replace(".", "");
        rnCode = rnCode.trim();
        int code = NumberUtile.getStringToInt(rnCode);
        return code;
    }

    public String getRunPath(String defPath) {
        if (TextUtils.isEmpty(runPath)) {
            String temp = DataSave.stringGet(DataSave.rn_entrance);
            File file = new File(temp, "index.bundlejs");
            boolean isExists = file.exists();
            RNLog.d(tag, "加载入口：" + file.toString() + " 是否存在：" + isExists);
            if (isExists) {
                runPath = file.toString();
            }
        }

        if (TextUtils.isEmpty(runPath)) {
            runPath = defPath;
            RNLog.d(tag, "加载入口 默认：" + runPath);
        }

        return runPath;
    }


    private HashMap listTask = new HashMap<String, String>();

    public void startDownloadPPK(Context context, String rnCode, String rnName, String ppkUrl, DoTaskListener listener) {
        String name = rnName;//MD5Utils.getMD5(ppkUrl);
        if (listTask.containsKey(name)) {
            return;
        }
        RNBean rnBean = new RNBean();
        rnBean.md5Name = name;
        rnBean.urlPPk = ppkUrl;
        rnBean.rnCode = getVersionCode(rnCode);
        rnBean.rnName = rnName;
        rnBean.decompressingFile = new File(RnFileUtils.getRootFile(context), name + ".ppk");
        rnBean.decompressToDirectory = new File(RnFileUtils.getRootFile(context), name);
        //
        String temp = DataSave.stringGet(DataSave.rn_entrance);
        if (rnBean.decompressToDirectory.toString().equals(temp)) {
            listener.onError(rnBean, 1, "请不要重复更新");
            RNLog.d(tag, "不要重复下载");
            return;
        }
        //
        if (!TextUtils.isEmpty(temp)) {
            rnBean.discardDirectory = new File(temp);
        }
        //
        listTask.put(name, listener);
        TaskManager.getInstance().onDownloadRn(new DoTask(new OnDoTaskListener()), rnBean);
    }

    public static void clearData() {
        if (manager != null) {
            manager.listTask = null;
            manager = null;
        }
    }

    private static RNUpdateManager manager;

    public static RNUpdateManager getInstance() {
        if (manager == null) {
            manager = new RNUpdateManager();
        }
        return manager;
    }

    class OnDoTaskListener implements DoTaskListener {


        @Override
        public void onDownloadState(RNBean rnBean, int state) {
            Object listener = listTask.get(rnBean.md5Name);
            if (listener != null && listener instanceof DoTaskListener) {
                ((DoTaskListener) listener).onDownloadState(rnBean, state);
            }
        }


        @Override
        public void onDownloadProgress(RNBean rnBean, long pro, long length) {
            Object listener = listTask.get(rnBean.md5Name);
            if (listener != null && listener instanceof DoTaskListener) {
                ((DoTaskListener) listener).onDownloadProgress(rnBean, pro, length);
            }
        }

        @Override
        public void onDecompressionStart(RNBean rnBean) {
            Object listener = listTask.get(rnBean.md5Name);
            RNLog.d("onDecompressionStart", "解压开始 md5Url=" + rnBean.md5Name + " listener=null?" + (listener == null));
            if (listener != null && listener instanceof DoTaskListener) {
                ((DoTaskListener) listener).onDecompressionStart(rnBean);
            }
        }

        @Override
        public void onDecompressionComplete(RNBean rnBean) {
            //保存入口
            RNLog.d("onDecompressionComplete", "解压完成 ");
            DataSave.stringSave(DataSave.rn_entrance,rnBean.decompressToDirectory.toString());
            RNLog.d("onDecompressionComplete", "解压完成 保存解压目录");

            DataSave.stringSave(DataSave.rn_version_name,rnBean.rnName );

            RNLog.d("onDecompressionComplete", "解压完成 保存rn名称：" + rnBean.rnName);
            DataSave.stringSave(DataSave.rn_version_code,rnBean.rnCode );
            RNLog.d("onDecompressionComplete", "解压完成 保存rn版本：" + rnBean.rnCode);
            runPath = "";
            //
            Object listener = listTask.get(rnBean.md5Name);
            RNLog.d("onDecompressionComplete", "解压完成 listener=null?" + (listener == null));
            if (listener != null && listener instanceof DoTaskListener) {
                ((DoTaskListener) listener).onDecompressionComplete(rnBean);
            }
        }

        @Override
        public void onEnd(RNBean rnBean) {
            Object listener = listTask.get(rnBean.md5Name);
            RNLog.d("onEnd", "结束 listener=null?" + (listener == null));
            if (listener != null && listener instanceof DoTaskListener) {
                ((DoTaskListener) listener).onEnd(rnBean);
            }
            listTask.remove(rnBean.md5Name);
            if (rnBean.decompressingFile.exists()) {
                rnBean.decompressingFile.delete();
            }
        }

        @Override
        public void onError(RNBean rnBean, int code, String msg) {
            Object listener = listTask.get(rnBean.md5Name);
            if (listener != null && listener instanceof DoTaskListener) {
                ((DoTaskListener) listener).onError(rnBean, code, msg);
            }
        }
    }
}
