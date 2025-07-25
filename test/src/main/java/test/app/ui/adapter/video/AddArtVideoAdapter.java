package test.app.ui.adapter.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.library.baseui.utile.img.ImageLoadingUtile;
import com.library.baseui.utile.other.NumberUtile;
import com.library.baseui.utile.time.DateUtile;
import com.list.library.adapter.recycler.AbstractRecyclerAdapter;
import com.list.library.adapter.recycler.BaseHolder;

import test.app.ui.activity.R;
import test.app.ui.bean.VideoData;

/**
 * 选择视频
 * Created by 郭敏 on 2018/3/5 0005.
 */

public class AddArtVideoAdapter extends AbstractRecyclerAdapter<VideoData, AddArtVideoAdapter.ViewHodler> {
    private Context context;
    private ImageView videoIv;
    private TextView videoTimeTv;

    public AddArtVideoAdapter(Context context) {
        this.context = context;
    }

    //获取视频
    public VideoData getVideo(String videoPath) {
        VideoData video = null;
        for (int i = 0; i < list.size(); i++) {
            VideoData bean = list.get(i);
            if (videoPath.equals(bean.videoPath)) {
                video = bean;
                break;
            }
        }
        return video;
    }

    @Override
    protected ViewHodler onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_art_video, null);
        return new ViewHodler(view);
    }


    @Override
    protected void onCreateData(ViewHodler holder, int position) {
        VideoData video = list.get(position);
        //单位（s）
        int videoLength = NumberUtile.getStringToInt(video.videoLength, 0) / 1000;
        holder.videoTimeTv.setText(DateUtile.getTime(videoLength));
        ImageLoadingUtile.loading(context, video.videoImagePath, holder.videoIv);
    }


    public class ViewHodler extends BaseHolder {
        ImageView videoIv;
        TextView videoTimeTv;

        public ViewHodler(View itemView) {
            super(itemView);
            videoIv = (ImageView) findViewById(R.id.video_iv);
            videoTimeTv = (TextView) findViewById(R.id.video_time_tv);

        }
    }

}
