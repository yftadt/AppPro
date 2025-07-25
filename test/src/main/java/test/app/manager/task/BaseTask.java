package test.app.manager.task;

import android.os.AsyncTask;


public class BaseTask extends AsyncTask<BeanTask, Long, Integer> {


    @Override
    protected Integer doInBackground(BeanTask... beans) {
       BeanTask bean = beans[0];
        switch (bean.taskType) {
            case TaskManager.TASK_TYPE_RN_UPDATE:
                //rn更新下载
                onDownloadRn(bean);
                break;
            case TaskManager.TASK_TYPE_CLEAR_CACHE:
                //缓存清理
                onClearCache(bean);
                break;

        }
        return getTaskState();
    }

    protected int getTaskState() {
        return 0;
    }

    protected void onDownloadRn(BeanTask bean) {
    }

    protected void onClearCache(BeanTask bean) {
    }

}
