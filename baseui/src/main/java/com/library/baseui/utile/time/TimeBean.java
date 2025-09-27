package com.library.baseui.utile.time;

import retrofit2.http.PUT;

public class TimeBean {
    public String time;//标准时间（UTC）
    public long timeStamp;//标准时间戳（UTC）

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
