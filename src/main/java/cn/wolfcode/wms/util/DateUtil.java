package cn.wolfcode.wms.util;

import java.util.Calendar;
import java.util.Date;

public abstract class DateUtil {
    //获取当前的最后时间
    public static Date getEndDate(Date d) {
        if (d == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR,23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND,59);
        return c.getTime();
    }
}
