package test.app.manager.rn;

import android.text.TextUtils;


import com.library.baseui.utile.SafeZipFile;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import test.app.manager.task.BaseTask;
import test.app.manager.task.BeanTask;


class DoTask extends BaseTask {
    private String tag = "下载_";
    private DoTaskListener listener;
    private RNBean rnBean;
    private String msg;
    public DoTask(DoTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDownloadRn(BeanTask bean) {
        if (bean instanceof RNBean) {
            rnBean = (RNBean) bean;
        }
        if (rnBean == null) {
            RNLog.d(tag, "下载失败：rnBean=null");
            return;
        }
        doTask();
    }

    private void doTask() {
        try {
            RNLog.d(tag, "下载开始:" + rnBean.urlPPk);
            publishProgress(-1L,1L);
            downloadFile(rnBean);
            publishProgress(-1L,2L);
            RNLog.d(tag, "下载完成");

        } catch (Exception e) {
            RNLog.d(tag, "doInBackground 发生错误" + e.getMessage());
            msg= e.getMessage();
            publishProgress(-1L,3L);
            return;
        }
        //
        try {
            publishProgress(-1L,5L);
            doPPKUnDecompression(rnBean);
            publishProgress(-1L,6L);
        } catch (Exception e) {
            RNLog.d(tag, "doInBackground 发生错误" + e.getMessage());
            msg= e.getMessage();
            publishProgress(-1L,7L);
            return;
        }
        //
        publishProgress(-1L,8L);
    }

    //解压缩
    private void doPPKUnDecompression(RNBean param) throws IOException {
        RNLog.d(tag, "删除文件夹 unzipDirectory=" + param.decompressToDirectory.toString());
        if (param.discardDirectory != null && param.discardDirectory.exists()) {
            RnFileUtils.removeDirectory(param.discardDirectory);
        }
        RnFileUtils.removeDirectory(param.decompressToDirectory);
        param.decompressToDirectory.mkdirs();
        RNLog.d(tag, "创建解压文件夹 unzipDirectory=" + param.decompressToDirectory.toString());
        SafeZipFile zipFile = new SafeZipFile(param.decompressingFile);
        RNLog.d(tag, "解压文件 param.targetFile=" + param.decompressingFile.toString());
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry ze = entries.nextElement();

            zipFile.unzipToPath(ze, param.decompressToDirectory);
        }

        zipFile.close();
        if (param.decompressToDirectory.exists()) {
            File[] listFiles = param.decompressToDirectory.listFiles();
            String temp = "";
            for (int i = 0; i < listFiles.length; i++) {
                if (TextUtils.isEmpty(temp)) {
                    temp = listFiles[i].getName();
                    continue;
                }
                temp += "," + listFiles[i].getName();
            }
            RNLog.d(tag, "解压文件夹 unzipDirectory=" + param.decompressToDirectory.toString() + " 解压之后文件名称=" + temp);
        }

        RNLog.d(tag, "创建解压文件完成 解压文件：" + param.decompressingFile.toString() + " 解压目录：" + param.decompressToDirectory.toString());

    }

    private final int DOWNLOAD_CHUNK_SIZE = 4096;

    private void downloadFile(RNBean rnBean) throws IOException {
        String url = rnBean.urlPPk;
        File writePath = rnBean.decompressingFile;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        RNLog.d(tag, "response：" + "code:" + response.code() + " " + response.message());
        if (response.code() > 299) {
            RNLog.d(tag, "下载链接失败");
            return;
        }
        ResponseBody body = response.body();
        long contentLength = body.contentLength();
        BufferedSource source = body.source();

        if (writePath.exists()) {
            writePath.delete();
        }
        BufferedSink sink = Okio.buffer(Okio.sink(writePath));
        long bytesRead = 0;
        long received = 0;
        int currentPercentage = 0;
        while ((bytesRead = source.read(sink.buffer(), DOWNLOAD_CHUNK_SIZE)) != -1) {
            received += bytesRead;
            sink.emit();
            //listener.onDownloadProgress(rnBean, received, contentLength);
            publishProgress(received, contentLength);
            int percentage = (int) (received * 100.0 / contentLength + 0.5);
            if (percentage > currentPercentage) {
                currentPercentage = percentage;
                publishProgress(received, contentLength);
            }
        }
        if (received != contentLength) {
            RNLog.d(tag, "下载失败：" + "Unexpected eof while reading downloaded update");
            return;
        }
        publishProgress(received, contentLength);
        sink.writeAll(source);
        sink.close();
        RNLog.d(tag, "下载完成 url=" + rnBean.urlPPk + " 保存地址 writePath=" + rnBean.decompressingFile + " 解压目录=" + rnBean.decompressToDirectory);
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        Long type = values[0];
        if (type >= 0) {
            //更新下载进度
            listener.onDownloadProgress(rnBean, values[0], values[1]);
            return;
        }
        if (type == -1) {
            long state = values[1];
            switch ((int) state) {
                case 1:
                    //开始下载
                    listener.onDownloadState(rnBean, 1);
                    break;
                case 2:
                    //下载完成
                    listener.onDownloadState(rnBean, 2);
                    break;
                case 3:
                    //下载出错
                    listener.onError(rnBean, 2,msg);
                    break;
                case 4:
                    //下载更新与当前版本相同
                    break;
                case 5:
                    //开始解压
                    listener.onDecompressionStart(rnBean);
                    break;
                case 6:
                    //解压完成
                    listener.onDecompressionComplete(rnBean);
                    break;
                case 7:
                    //解压出错
                    listener.onError(rnBean, 3, msg);
                    break;
                case 8:
                    //操作顺利完成
                    listener.onEnd(rnBean);
                    break;
            }
        }

    }

    @Override
    protected int getTaskState() {
        return 0;
    }

}
