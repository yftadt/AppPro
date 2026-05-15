package test.app.ui.web;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

import media.library.images.config.entity.MediaEntity;

/**
 * Created by Administrator on 2016/4/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImgRes implements Serializable {
    public int index;
    public ArrayList<MediaEntity> datas;

    public void setMediaEntity(MediaEntity bean) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas.add(bean);
    }
}
