package test.app.ui.event;


import java.util.ArrayList;

import media.library.images.config.entity.MediaEntity;

public class EventImgData extends BaseEvent {
    private Object obj;

    public String type;

    public EventImgData(Object obj) {
        this.obj = obj;
    }

    public ArrayList<MediaEntity> imgs;

}
