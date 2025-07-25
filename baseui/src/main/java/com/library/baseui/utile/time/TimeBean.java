package com.library.baseui.utile.time;

import retrofit2.http.PUT;

public class TimeBean {
    public String time;
    public long timeStamp;//时间戳

    public String zoneId;//时区

    @Override
    public String toString() {
        return "TimeBean{" +
                "time='" + time + '\'' +
                ", timeStamp=" + timeStamp +
                ", zoneId='" + zoneId + '\'' +
                '}';
    }
}
