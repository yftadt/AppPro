package test.app.ui.activity.base;

import android.content.Intent;
import android.text.TextUtils;


import com.images.config.entity.ImageEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import test.app.net.manager.loading.UploadingManager;
import test.app.net.res.loading.AttaRes;
import test.app.ui.activity.R;
import test.app.ui.activity.action.NormalActionBar;
import test.app.ui.view.images.ImageLoadingView;
import test.app.ui.view.images.ImagePath;
import test.app.ui.view.images.ImagesLayout;
import test.app.utiles.other.DLog;

/**
 * 选择多张照片
 * Created by junfengwang on 2017/1/5.
 */

public class PhotoMoreActivity extends NormalActionBar {
    protected ImagesLayout imagesView;

    public void initPhotoView() {
        initPhotoView(false);
    }

    public void initPhotoView(boolean isOnlyShow) {
        imagesView = (ImagesLayout) findViewById(R.id.lmages_view);
        imagesView.setImageUpload(this, isOnlyShow);
    }

    //显示附件图片
    protected void setImageShow(List<AttaRes> attaList) {
        if (attaList == null) {
            attaList = new ArrayList<>();
        }
        ArrayList<ImagePath> urls = new ArrayList();
        for (int i = 0; i < attaList.size(); i++) {
            AttaRes a = attaList.get(i);
            ImagePath image = new ImagePath();
            image.setUrl(a.id, a.attaFileUrl);
            urls.add(image);
        }
        imagesView.setImages(urls);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ImageEntity> iamges = imagesView.onActivityResult(requestCode, resultCode, data);
        setImagesShow(iamges);
    }

    //上传文件
    private UploadingManager uploadingManager;

    protected void setImagesShow(List<ImageEntity> iamges) {
        if (iamges == null) {
            return;
        }
        //上一次获取的图片
        HashMap<String, ImagePath> uploadingPaths = imagesView.getImages();
        imagesView.setImagesPath(iamges);
        //本次获取的图片
        HashMap<String, ImagePath> paths = imagesView.getImages();
        //上一次获取得图片，在本次获取得图片中存在，删除
        ArrayList<String> stopUploading = new ArrayList<>();
        Iterator iter = uploadingPaths.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            ImagePath image = paths.get(key);
            if (image != null) {
                continue;
            }
            stopUploading.add(key);
        }
        for (int i = 0; i < paths.size(); i++) {
            if (uploadingPaths.size() == 0) {
                break;
            }
            uploadingPaths.remove(paths.get(i));
        }
        if (uploadingManager == null) {
            uploadingManager = new UploadingManager(this);
        }
        //停止,删除的图片的继续上传
        uploadingManager.stopTask(stopUploading);
        //
        for (int i = 0; i < paths.size(); i++) {
            ImageLoadingView imageLoadingView = imagesView.getImageLoadingView(i);
            ImagePath image = imageLoadingView.getImage();
            String path = image.path;
            if (TextUtils.isEmpty(path) || !TextUtils.isEmpty(image.url)) {
                imageLoadingView.setUpLodingComplete();
                continue;
            }
            boolean isRun = uploadingManager.getLoadingPath(path);
            if (isRun) {
                imageLoadingView.setUpLodingComplete();
                continue;
            }
            File file = new File(path);
            if (file == null || !file.exists()) {
                imageLoadingView.setUpLodingComplete();
                continue;
            }
            imageLoadingView.setUpLodingShow();
            uploadingManager.setDataFile(file);
            uploadingRestRes(uploadingManager);
            uploadingManager.doRequest();
        }

    }

    //添加或者重置 请求参数
    protected void uploadingRestRes(UploadingManager uploadingManager) {

    }

    protected ArrayList<String> getUrls() {
        List<ImagePath> images = imagesView.getImagePaths();
        DLog.e("图片", "------------------");
        ArrayList<String> urls = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImagePath image = imagesView.getImageLoadingView(i).getImage();
            String url = image.url;
            if (TextUtils.isEmpty(url)) {
                continue;
            }
            DLog.e("图片id", url);
            urls.add(url);
        }
        return urls;
    }

    protected ArrayList<String> getIds() {
        List<ImagePath> images = imagesView.getImagePaths();
        //ImageLoadingView[] ivs = imagesView.getIvs();
        DLog.e("图片", "------------------");
        ArrayList<String> urlId = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            ImagePath image = imagesView.getImageLoadingView(i).getImage();
            String id = image.urlId;
            if (TextUtils.isEmpty(id)) {
                continue;
            }
            DLog.e("图片id", id);
            urlId.add(id);
        }
        return urlId;
    }

    protected int getLoadingCount() {
        return imagesView.getLoadingCount();
    }

    @Override
    public void onBack(int what, Object obj, String msg, String other) {
        switch (what) {
            case UploadingManager.UPLOADING_WHAT_SUCCEED:
                AttaRes bean = (AttaRes) obj;
                //上传图片成功
                imagesView.setUpLodingComplete(other, bean.id, bean.getUrl());
                DLog.e("上传图片成功", "" + obj);
                break;
            case UploadingManager.UPLOADING_WHAT_FAILED:
                //修改个人信息成功
                DLog.e("上传图片成功", "");
                imagesView.setUpLodingComplete(other, "", "");
                break;
        }
        super.onBack(what, obj, msg, "");
    }
}
