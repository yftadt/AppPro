package test.app.ui.view.images;

import android.content.Context;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;


import com.library.baseui.utile.app.APKInfo;
import com.library.baseui.utile.img.ImageLoadingUtile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import test.app.ui.activity.R;
import test.app.utiles.other.DLog;


/**
 * Created by Administrator on 2017/7/4.
 */

public class ImagesBaseLayout extends LinearLayout {
    //所有图片
    protected ArrayList<ImagePath> imagePaths = new ArrayList<>();
    //没有图片时的选择
    private View viewImageAdd;
    //所有图片view
    private ImageLoadingView[] ivs = new ImageLoadingView[12];
    //图片行
    private LinearLayout[] ivLls = new LinearLayout[3];
    //只显示图片
    private boolean isOnlyShow = true;
    //true 视图已经初始化
    protected boolean isViewInit;
    //每行显示的图片数量
    private int rowIamgeCount;
    //true 可以点击，false 不可以点击
    private boolean isImageClick;

    public ImagesBaseLayout(Context context) {
        super(context);
        init();
    }

    public ImagesBaseLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImagesBaseLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
    }

    //添加图片的视图
    protected void imageAddView(Context context) {
        viewImageAdd = LayoutInflater.from(context).inflate(R.layout.item_view_image_add, null);
        addView(viewImageAdd);
        viewImageAdd.findViewById(R.id.image_add_iv).setOnClickListener(getClickListener());
    }

    /***
     * @param count 每行显示的数量 3 或者 4
     * @param row 最多总共几行
     * @param m 左右两边的距离 dp
     * @param space 图片之间的间隔 dp
     */
    protected void setImageParameter(Context context, int count, int row, int m, int space) {
        isViewInit = true;
        rowIamgeCount = count;
        //图片数
        ivs = new ImageLoadingView[row * count];
        //图片行
        ivLls = new LinearLayout[row];
        int width = (int) APKInfo.getInstance().getScreenWidthPixels();
        int margin = (int) APKInfo.getInstance().getDIPTOPX(m);
        int marginspace = (int) APKInfo.getInstance().getDIPTOPX(space);
        switch (count) {
            case 3:
                //图片总数
                int ivCount = row * count;
                //行数
                int rowCount = 0;
                //照片宽度
                float imageWidth = (width - margin * 2 - 2 * marginspace) / 3;
                LinearLayout rowLl = null;
                for (int i = 0; i < ivCount; i++) {
                    if (i % 3 == 0) {
                        rowLl = new LinearLayout(context);
                        rowLl.setOrientation(LinearLayout.HORIZONTAL);
                        rowLl.setPadding(margin, 0, margin, 0);
                    }
                    ImageLoadingView ivLayout = new ImageLoadingView(context);
                    LayoutParams layoutParams = new LayoutParams((int) imageWidth, (int) imageWidth);
                    if ((i + 1) % 3 != 0) {
                        layoutParams.rightMargin = marginspace;
                        layoutParams.bottomMargin = (rowCount + 1 == row) ? 0 : marginspace;
                    }
                    rowLl.addView(ivLayout, layoutParams);

                    ivLayout.setId(i);
                    if (isImageClick) {
                        ivLayout.setOnClickListener(getClickListener());
                    }
                    ivs[i] = ivLayout;
                    if (rowLl.getChildCount() == 3) {
                        addView(rowLl);
                        ivLls[rowCount] = rowLl;
                        rowCount++;
                    }
                    if (rowCount == row) {
                        break;
                    }
                }
                break;
        }
    }

    private ClickListener listener;

    //获取图片的点击事件
    protected OnClickListener getClickListener() {
        if (listener == null) {
            listener = new ClickListener();
        }
        return listener;
    }

    protected void setIsImageClick(boolean isImageClick) {
        this.isImageClick = isImageClick;
    }

    protected void showImage() {
        showImage(isOnlyShow);
    }

    protected void showImage(boolean isOnlyShow) {
        this.isOnlyShow = isOnlyShow;
        showImageRow();
        showImages();
    }

    //设置图片的显示
    private void showImages() {
        //现有图片数量
        int imageCount = imagePaths.size();
        if (!isOnlyShow && viewImageAdd != null && imagePaths.size() == 0) {
            viewImageAdd.setVisibility(View.VISIBLE);
        }
        if (imageCount == 0) {
            return;
        }
        if (viewImageAdd != null) {
            viewImageAdd.setVisibility(View.GONE);
        }
        //最多显示图片的数量
        int ivsSize = ivs.length;
        //显示的图片数量
        int imageShowCount = imageCount;
        if (!isOnlyShow && imageShowCount < ivsSize) {
            imageShowCount += 1;
        }
        for (int i = 0; i < ivsSize; i++) {
            if (i >= imageShowCount) {
                ivs[i].setVisibility(View.INVISIBLE);
                continue;
            }
            ivs[i].setVisibility(View.VISIBLE);
            ImageView iv = ivs[i].getImageView();
            if (i < imageCount) {
                ImagePath image = imagePaths.get(i);
                ivs[i].setImage(image);
                String url = image.getShowUrl();
                ImageLoadingUtile.loadingCenterCrop(getContext(), url, R.mipmap.default_image, iv);
                continue;
            }
            //显示添加图片
            iv.setImageResource(R.mipmap.default_image_add);
        }

    }

    //设置显示几排图片
    private void showImageRow() {
        //现有图片数量
        int imageCount = imagePaths.size();
        int imageShowCount = imageCount;
        if (!isOnlyShow && imageCount < ivs.length) {
            imageShowCount += 1;
        }
        for (int i = 0; i < ivLls.length; i++) {
            LinearLayout rowLl = ivLls[i];
            if (rowLl == null) {
                continue;
            }
            boolean isShow = false;
            int size = (i + 1) * rowIamgeCount;
            int count = size - imageShowCount;
            if (count <= 0) {
                isShow = true;
            }
            if (!isShow && count < rowIamgeCount && imageCount > 0) {
                isShow = true;
            }
            rowLl.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }

    }

    public void setImages(ArrayList<ImagePath> imagePaths) {
        this.imagePaths = imagePaths;
        if (isOnlyShow && imagePaths.size() == 0) {
            this.setVisibility(View.GONE);
            return;
        }
        if (imagePaths.size() == 0) {
            return;
        }
        showImage(isOnlyShow);
    }

    public List<ImagePath> getImagePaths() {
        return imagePaths;
    }


    public ImageLoadingView getImageLoadingView(int index) {
        return ivs[index];
    }

    //获取的图片
    public HashMap<String, ImagePath> getImages() {
        HashMap<String, ImagePath> map = new HashMap<>();
        for (int i = 0; i < imagePaths.size(); i++) {
            ImagePath image = imagePaths.get(i);
            map.put(image.getShowSource(), image);
        }
        return map;
    }

    //获取图片的url
    protected ArrayList<String> getSourceIamgePaths() {
        ArrayList paths = new ArrayList();
        for (int i = 0; i < imagePaths.size(); i++) {
            ImagePath image = imagePaths.get(i);
            paths.add(image.getShowSource());
        }
        return paths;
    }

    //图片上传完成
    public void setUpLodingComplete(String path, String urlId, String url) {
        for (int i = 0; i < ivs.length; i++) {
            ImageLoadingView imageLoadingView = ivs[i];
            ImagePath image = imageLoadingView.getImage();
            if (image == null) {
                continue;
            }
            if (!path.equals(image.path)) {
                continue;
            }
            imageLoadingView.setUpLodingComplete();
            image.setUrl(urlId, url);

        }
        DLog.e("============", "===============");
    }

    //获取正在上传的图片数量
    public int getLoadingCount() {
        int count = 0;
        int size = imagePaths.size() - 1;
        for (int i = 0; i < ivs.length; i++) {
            if (i > size) {
                break;
            }
            count += ivs[i].isUploading() ? 1 : 0;
        }
        return count;
    }

    //图片点击事件
    private void imageClick(int index) {
        //type 1：只显示 2：选择添加图片方式 3：可以删除
        int type = 0;
        if (isOnlyShow) {
            type = 1;
        }
        if (!isOnlyShow && index >= imagePaths.size()) {
            type = 2;
        }
        if (!isOnlyShow && index < imagePaths.size()) {
            type = 3;
        }
        imageClick(index, type);
    }

    //type 1：只显示 2：可以添加，可以删除 3：可以删除
    protected void imageClick(int index, int type) {

    }


    class ClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.image_add_iv) {
                //添加图片
                imageClick(0, 2);
                return;
            }
            if (isOnlyShow && id >= imagePaths.size()) {
                return;
            }
            //点击了图片
            imageClick(id);

        }
    }


}
