package com.day.ourday.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LimerenceT on 19-8-1
 */
public class DateUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);

    public static String format(Date date) {
        return simpleDateFormat.format(new Date());
    }

}
