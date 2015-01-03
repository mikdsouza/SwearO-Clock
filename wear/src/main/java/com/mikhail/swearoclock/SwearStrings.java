package com.mikhail.swearoclock;

import android.text.format.Time;

import java.util.Random;

/**
 * Created by Mikhail on 28/12/2014.
 */
public final class SwearStrings {

    public static final String IT_IS = "It is";

    public static String getHour(Time time) {
        int hour = time.hour;
        float minute = time.minute + (float)time.second/60;

        if (minute > 57.5f)
            hour++;

        if (hour > 24)
            hour -= 24;

        if(hour > 12)
            hour -= 12;
        else if(hour < 1)
            hour += 12;

        return hourToWord(hour);
    }

    public static String getFucking(Time time) {
        float minute = time.minute + (float)time.second/60;

        if (minute > 57.5f)
            minute = 0;

        return minuteToFucking(minute, time.hour, time.monthDay, time.month, time.year);
    }

    public static String getMinute(Time time) {
        float minute = time.minute + (float)time.second/60;

        if (minute > 57.5f)
            minute = 0;

        return minuteToWords(minute);
    }

    public static boolean hasMinutes(Time time) {
        float minute = time.minute + (float)time.second/60;

        if (minute > 57.5f)
            minute = 0;

        return minute > 2.5f;
    }

    public static String getDateString(Time time) {
        return dayToWord(time.weekDay) + ", " + time.monthDay + " " + monthToWord(time.month) + " "
                + time.year;
    }

    private static String hourToWord(int hour) {
        switch(hour) {
            case 1: return "One";
            case 2: return "Two";
            case 3: return "Three";
            case 4: return "Four";
            case 5: return "Five";
            case 6: return "Six";
            case 7: return "Seven";
            case 8: return "Eight";
            case 9: return "Nine";
            case 10: return "Ten";
            case 11: return "Eleven";
            case 12: return "Twelve";
            default: return "Err";
        }
    }

    private static String minuteToFucking(float minute, int hour, int day, int month, int year) {
        String fucking;

        Random rand = new Random((long)((int)minute + hour + day + month + year));

        switch(rand.nextInt(3)) {
            case 0: fucking = "Freaking";
                break;
            case 1: fucking = "Bloody";
                break;
            case 2: fucking = "Shitting";
                break;
            default: fucking = "Fucking";
                break;
        }

        if (minute <= 2.5f)
            return "O'" + fucking;
        else {
            return fucking + " Past";
        }
    }

    private static String minuteToWords(float minute) {
        if (minute <= 2.5f)
            return "Clock";
        else if (minute <= 7.5f)
            return "Five";
        else if (minute <= 12.5f)
            return "Ten";
        else if (minute <= 17.5f)
            return "Fifteen";
        else if (minute <= 22.5f)
            return "Twenty";
        else if (minute <= 27.5f)
            return "Twenty Five";
        else if (minute <= 32.5f)
            return "Thirty";
        else if (minute <= 37.5f)
            return "Thirty Five";
        else if (minute <= 42.5f)
            return "Forty";
        else if (minute <= 47.5f)
            return "Forty Five";
        else if (minute <= 52.5f)
            return "Fifty";
        else if (minute <= 57.5f)
            return "Fifty Five";
        else
            return "MEHH";
    }

    private static String dayToWord(int day) {
        switch (day) {
            case Time.SUNDAY: return "Sun";
            case Time.MONDAY: return "Mon";
            case Time.TUESDAY: return "Tue";
            case Time.WEDNESDAY: return "Wed";
            case Time.THURSDAY: return "The";
            case Time.FRIDAY: return "Fri";
            case Time.SATURDAY: return "Sat";
            default: return "Err";
        }
    }

    private static String monthToWord(int month) {
        switch(month) {
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "March";
            case 3: return "April";
            case 4: return "May";
            case 5: return "June";
            case 6: return "July";
            case 7: return "Aug";
            case 8: return "Sept";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default: return "Err";
        }
    }
}
