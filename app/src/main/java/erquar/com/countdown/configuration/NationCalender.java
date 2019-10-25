package erquar.com.countdown.configuration;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 7/23/2016.
 */
public class NationCalender {
    public final static String[][] nation_list = new String[][] {
            {"Australian Holidays", "en.australian#holiday@group.v.calendar.google.com"},
            {"Austrian Holidays", "en.austrian#holiday@group.v.calendar.google.com"},
            {"Brazilian Holidays", "en.brazilian#holiday@group.v.calendar.google.com"},
            {"Canadian Holidays", "en.canadian#holiday@group.v.calendar.google.com"},
            {"China Holidays", "en.china#holiday@group.v.calendar.google.com"},
            {"Christian Holidays", "en.christian#holiday@group.v.calendar.google.com"},
            {"Danish Holidays", "en.danish#holiday@group.v.calendar.google.com"},
            {"Dutch Holidays", "en.dutch#holiday@group.v.calendar.google.com"},
            {"Finnish Holidays", "en.finnish#holiday@group.v.calendar.google.com"},
            {"French Holidays", "en.french#holiday@group.v.calendar.google.com"},
            {"German Holidays", "en.german#holiday@group.v.calendar.google.com"},
            {"Greek Holidays", "en.greek#holiday@group.v.calendar.google.com"},
            {"Hong Kong (C) Holidays", "en.hong_kong_c#holiday@group.v.calendar.google.com"},
            {"Hong Kong Holidays", "en.hong_kong#holiday@group.v.calendar.google.com"},
            {"Indian Holidays", "en.indian#holiday@group.v.calendar.google.com"},
            {"Indonesian Holidays", "en.indonesian#holiday@group.v.calendar.google.com"},
            {"Iranian Holidays", "en.iranian#holiday@group.v.calendar.google.com"},
            {"Irish Holidays", "en.irish#holiday@group.v.calendar.google.com"},
            {"Islamic Holidays", "en.islamic#holiday@group.v.calendar.google.com"},
            {"Italian Holidays", "en.italian#holiday@group.v.calendar.google.com"},
            {"Japanese Holidays", "en.japanese#holiday@group.v.calendar.google.com"},
            {"Jewish Holidays", "en.jewish#holiday@group.v.calendar.google.com"},
            {"Malaysian Holidays", "en.malaysia#holiday@group.v.calendar.google.com"},
            {"Mexican Holidays", "en.mexican#holiday@group.v.calendar.google.com"},
            {"New Zealand Holidays", "en.new_zealand#holiday@group.v.calendar.google.com"},
            {"Norwegian Holidays", "en.norwegian#holiday@group.v.calendar.google.com"},
            {"Philippines Holidays", "en.philippines#holiday@group.v.calendar.google.com"},
            {"Polish Holidays", "en.polish#holiday@group.v.calendar.google.com"},
            {"Portuguese Holidays", "en.portuguese#holiday@group.v.calendar.google.com"},
            {"Russian Holidays", "en.russian#holiday@group.v.calendar.google.com"},
            {"Singapore Holidays", "en.singapore#holiday@group.v.calendar.google.com"},
            {"South Africa Holidays", "en.sa#holiday@group.v.calendar.google.com"},
            {"South Korean Holidays", "en.south_korea#holiday@group.v.calendar.google.com"},
            {"Spain Holidays", "en.spain#holiday@group.v.calendar.google.com"},
            {"Swedish Holidays", "en.swedish#holiday@group.v.calendar.google.com"},
            {"Taiwan Holidays", "en.taiwan#holiday@group.v.calendar.google.com"},
            {"Thai Holidays", "en.thai#holiday@group.v.calendar.google.com"},
            {"UK Holidays", "en.uk#holiday@group.v.calendar.google.com"},
            {"US Holidays", "en.usa#holiday@group.v.calendar.google.com"},
            {"Vietnamese Holidays", "en.vietnamese#holiday@group.v.calendar.google.com"},
            {"アイルランドの祝日", "ja.irish#holiday@group.v.calendar.google.com"},
            {"アメリカの祝日", "ja.usa#holiday@group.v.calendar.google.com"},
            {"イギリスの祝日", "ja.uk#holiday@group.v.calendar.google.com"},
            {"イスラム教の祝日", "ja.islamic#holiday@group.v.calendar.google.com"},
            {"イタリアの祝日", "ja.italian#holiday@group.v.calendar.google.com"},
            {"インドの祝日", "ja.indian#holiday@group.v.calendar.google.com"},
            {"インドネシアの祝日", "ja.indonesian#holiday@group.v.calendar.google.com"},
            {"オランダの祝日", "ja.dutch#holiday@group.v.calendar.google.com"},
            {"オーストラリアの祝日", "ja.australian#holiday@group.v.calendar.google.com"},
            {"オーストリアの祝日", "ja.austrian#holiday@group.v.calendar.google.com"},
            {"カナダの祝日", "ja.canadian#holiday@group.v.calendar.google.com"},
            {"キリスト教の祝日", "ja.christian#holiday@group.v.calendar.google.com"},
            {"ギリシャの祝日", "ja.greek#holiday@group.v.calendar.google.com"},
            {"シンガポールの祝日", "ja.singapore#holiday@group.v.calendar.google.com"},
            {"スウェーデンの祝日", "ja.swedish#holiday@group.v.calendar.google.com"},
            {"スペインの祝日", "ja.spain#holiday@group.v.calendar.google.com"},
            {"タイの祝日", "ja.thai#holiday@group.v.calendar.google.com"},
            {"デンマークの祝日", "ja.danish#holiday@group.v.calendar.google.com"},
            {"ドイツの祝日", "ja.german#holiday@group.v.calendar.google.com"},
            {"ニュージーランドの祝日", "ja.new_zealand#holiday@group.v.calendar.google.com"},
            {"ノルウェイの祝日", "ja.norwegian#holiday@group.v.calendar.google.com"},
            {"フィリピンの祝日", "ja.philippines#holiday@group.v.calendar.google.com"},
            {"フィンランドの祝日", "ja.finnish#holiday@group.v.calendar.google.com"},
            {"フランスの祝日", "ja.french#holiday@group.v.calendar.google.com"},
            {"ブラジルの祝日", "ja.brazilian#holiday@group.v.calendar.google.com"},
            {"ベトナムの祝日", "ja.vietnamese#holiday@group.v.calendar.google.com"},
            {"ポルトガルの祝日", "ja.portuguese#holiday@group.v.calendar.google.com"},
            {"ポーランドの祝日", "ja.polish#holiday@group.v.calendar.google.com"},
            {"マレーシアの祝日", "ja.malaysia#holiday@group.v.calendar.google.com"},
            {"メキシコの祝日", "ja.mexican#holiday@group.v.calendar.google.com"},
            {"ユダヤ教の祝日", "ja.jewish#holiday@group.v.calendar.google.com"},
            {"ロシアの祝日", "ja.russian#holiday@group.v.calendar.google.com"},
            {"中国の祝日", "ja.china#holiday@group.v.calendar.google.com"},
            {"韓国の祝日", "ja.south_korea#holiday@group.v.calendar.google.com"},
            {"南アフリカの祝日", "ja.sa#holiday@group.v.calendar.google.com"},
            {"台湾の祝日", "ja.taiwan#holiday@group.v.calendar.google.com"},
            {"日本の祝日", "ja.japanese#holiday@group.v.calendar.google.com"},
            {"香港(C)の祝日", "ja.hong_kong_c#holiday@group.v.calendar.google.com"},
            {"香港の祝日", "ja.hong_kong#holiday@group.v.calendar.google.com"}
    };

    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();

    // get current date
    public static String getCurrentDate () {
        String currentDateTimeString = getDateFormat().format(new Date());
        return currentDateTimeString;
    }

    public static Date convertStringToDate (String strDate) {
        Calendar c = Calendar.getInstance();
        DateFormat format = NationCalender.getDateFormat();
        try {
            c.setTime(format.parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date(c.getTimeInMillis());
        return date;
    }

    public static String convetDateToString (Date date) {
        String strDate = DateFormat.getDateInstance().format(date);
        return  strDate;
    }
    public static ArrayList<String> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent.add(cursor.getString(1));
            startDates.add(getDate(Long.parseLong(cursor.getString(3))));
            endDates.add(getDate(Long.parseLong(cursor.getString(4))));
            descriptions.add(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();

        }
        return nameOfEvent;
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("E, dd/MM/yy");
    }
    public static String getDate(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return getDateFormat().format(calendar.getTime());
    }

    public static int getWorkingDaysBetweenTwoDates(Date startDate, int size) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = (Calendar) startCal.clone();
        DateFormat format = NationCalender.getDateFormat();
        endCal.add(Calendar.DATE, size);
        Date endDate = new Date(endCal.getTimeInMillis());

        endCal.setTime(endDate);

        int unworkDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                ++unworkDays;
                endCal.add(Calendar.DATE, 1);
            } else {
                Date datetemp = new Date(startCal.getTimeInMillis());
                if(isFestival(datetemp)) {
                    ++unworkDays;
                    endCal.add(Calendar.DATE, 1);
                }
            }

        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return unworkDays + size;
    }

    public static boolean isFestival (Date date) {
        String strDate = NationCalender.getDateFormat().format(date);
        if(startDates.contains(strDate))
            return true;

        return false;
    }

    public static int getDaysBetweenDates (Date firstDate, Date secondDate) {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.clear();
        date1.setTime(firstDate);
        date2.clear();
        date2.setTime(secondDate);
        long diff = date1.getTimeInMillis() - date2.getTimeInMillis();
        int result = (int) ((float) diff / (24 * 60 * 60 * 1000)) + 1;
        return result;
    }
}
