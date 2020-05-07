package com.netoperation.util;

import android.content.Context;
import android.widget.Toast;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AppDateUtil {

    public static final String MMM_dd_yyyy_hh_mm_a = "MMM dd, yyyy hh:mm a";
    public final static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static final DateTime getDateTime(String source) {
        return DateTime.parse(source, ISODateTimeFormat.dateTimeParser());
    }

    public static final boolean isToday(DateTime dateTime) {
        if(dateTime == null) {
            return false;
        }
        return DateUtils.isToday(dateTime);
    }

    public static final boolean isYesterday(DateTime dateTime) {
        if(dateTime == null) {
            return false;
        }
        return DateUtils.isToday(dateTime.minusDays(1));
    }

    public static final String onlyDate_ddMMyyyy(DateTime dateTime) {
        if(dateTime == null) {
            return "";
        }
        DateTimeFormatter builder = DateTimeFormat.forPattern( "dd/MM/yyyy" );
        return  builder.print(dateTime);
    }

    public static final String onlyTime_HHmm(DateTime dateTime) {
        if(dateTime == null) {
            return "";
        }
        int hourOfDay = dateTime.getHourOfDay();
        String srt = "";
        if (hourOfDay == 00) {
            DateTimeFormatter builder = DateTimeFormat.forPattern( "hh:mm' AM" );
            srt =  builder.print(dateTime);
        } else if (0 < hourOfDay && hourOfDay < 12) {
            DateTimeFormatter builder = DateTimeFormat.forPattern( "hh:mm' AM" );
            srt =  builder.print(dateTime);
        } else if (hourOfDay > 12 || hourOfDay == 12) {
            DateTimeFormatter builder = DateTimeFormat.forPattern( "hh:mm' PM" );
            srt =  builder.print(dateTime);
        }
        return srt;
    }

    public static final String onlyTime_hhmm(DateTime dateTime) {
        if(dateTime == null) {
            return "";
        }
        DateTimeFormatter builder = DateTimeFormat.forPattern( "hh:mm" );
        String val = builder.print(dateTime);
        String[] spl = val.split(":");

        return  builder.print(dateTime);
    }

    public static String getTopNewsFormattedDate(long date_updated) {
        String result = "";
        try {
            Date time_updated = new Date(date_updated);
            result = "Updated: "
                    + new SimpleDateFormat(MMM_dd_yyyy_hh_mm_a).format(time_updated) + " IST";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getPlaneTopNewsFormattedDate(long date_updated) {
        String result = "";
        try {
            Date time_updated = new Date(date_updated);
            result = new SimpleDateFormat(MMM_dd_yyyy_hh_mm_a).format(time_updated) + " IST";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long changeStringToMillis(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        try {
            Date date = formatter.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long changeStringToMillisGMT(String dateInString) {
        SimpleDateFormat formatter = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        try {
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = formatter.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDurationFormattedDate(long date_created, Locale locale) {
        String result = "";
        long currentTime = System.currentTimeMillis();
        Date time_created = new Date(date_created);

        String hrAgo = " hr ago";
        String hrsAgo = " hrs ago";
        String secAgo = " sec ago";
        String minAgo = " min ago";


        if (currentTime - date_created < 60 * 1000) {
            result = ((currentTime - date_created) / 1000)
                    + secAgo;
        } else if (currentTime - date_created < 60 * 60 * 1000) {
            result = ((currentTime - date_created) / (60 * 1000))
                    + minAgo;
        } else if (currentTime - date_created < 24 * 60 * 60 * 1000) {
            long t = (currentTime - date_created) / (60 * 60 * 1000);
            if (t == 1)
                result = t + hrAgo;
            else
                result = t + hrsAgo;
        } else {
            result = new SimpleDateFormat("MMM dd, yyyy hh:mm a", locale).format(time_created)+ " IST";
        }
        return result;
    }

    public static long strToMlsForSearchedArticle(String dateInString) {
        // 2019-09-21 19:00:00
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long strToMlsForNonBriefing(String dateInString) {
        // May 23, 2019 8:44:42 PM
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        try {
            Date date = formatter.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long strToMlsForBriefing(String dateInString) {
        // Thu, 6 Jun 2019 12:17:30 +0530 // OLD
        // 2019-09-20 09:55:19
//        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(dateInString);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String getDetailScreenPublishDate(long date_updated, Locale locale) {
        String result = "";
        try {
            Date time_updated = new Date(date_updated);
//            result = "" + new SimpleDateFormat("MMM dd, yyyy hh:mm a").format(time_updated) + " IST";
            result = "" + new SimpleDateFormat("dd MMMM, yyyy | hh:mm a", locale).format(time_updated);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String millisToMinAndSec(long milliseconds) {
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        String secondsStr = Long.toString(seconds);
        String secs;
        if (secondsStr.length() >= 2) {
            secs = secondsStr.substring(0, 2);
        } else {
            secs = "0" + secondsStr;
        }

        return minutes + " minute " + secs+" Sec";
    }

    public static long millisToSecs(long milliseconds) {
        return  TimeUnit.MILLISECONDS.toSeconds(milliseconds);
    }

    public static String getCurrentDateFormatted(String patternDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(patternDate, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String calculateDurationAndUnit(long seconds) {
        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
        if (day >= 1) { //Days
            if (day == 1)
            return day + " day";
            else if (day < 30) return day+" days";
            else if (day == 30 || day == 31) return "1 month";
            else return (day / 30) + " months";
        } else if (hours >= 1 && hours < 24) { //Hours
            if (hours == 1)
                return hours +" hour";
            else return hours + " hours";
        } else if (minute >= 1 && minute < 60) { //Minutes
            if (minute == 1)
                return minute +" minute";
            else return minute +" minutes";
        } else if (second >= 1 && minute < 60) { //Seconds
            if (second == 1)
                return second +" second";
            else return second +" seconds";
        } else {
            return "";
        }
    }

    public static String BL_getDateFormateChange(String dates){

        String lastUpdateDate= null;
        try {
            Date d = new SimpleDateFormat("dd-MMM-yyyy HH:mm").parse(dates);
            lastUpdateDate = new SimpleDateFormat("dd-MMM-yyyy").format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return lastUpdateDate;
    }

}
