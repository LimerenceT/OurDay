package com.day.ourday.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LimerenceT on 19-8-1
 */
public class DateUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);

    public static String format(Date date) {
        return simpleDateFormat.format(date);
    }

    private static Date parse(String date) {
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getDays(String dateString) {
        LocalDate date = parse(dateString).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long until = LocalDate.now().until(date, ChronoUnit.DAYS);
        return (int)until;
    }

}
