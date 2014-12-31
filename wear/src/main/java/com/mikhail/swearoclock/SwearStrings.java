package com.mikhail.swearoclock;

import android.text.format.Time;

/**
 * Created by Mikhail on 28/12/2014.
 */
public final class SwearStrings {

    public static final String IT_IS = "IT IS";

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

        return minuteToFucking(minute);
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

    private static String hourToWord(int hour) {
        switch(hour) {
            case 1: return "ONE";
            case 2: return "TWO";
            case 3: return "THREE";
            case 4: return "FOUR";
            case 5: return "FIVE";
            case 6: return "SIX";
            case 7: return "SEVEN";
            case 8: return "EIGHT";
            case 9: return "NINE";
            case 10: return "TEN";
            case 11: return "ELEVEN";
            case 12: return "TWELVE";
            default: return "MEHH";
        }
    }

    private static String minuteToFucking(float minute) {
        if (minute <= 2.5f)
            return "O'FUCKING";
        else
            return "FUCKING PAST";
    }

    private static String minuteToWords(float minute) {
        if (minute <= 2.5f)
            return "CLOCK";
        else if (minute <= 7.5f)
            return "FIVE";
        else if (minute <= 12.5f)
            return "TEN";
        else if (minute <= 17.5f)
            return "FIFTEEN";
        else if (minute <= 22.5f)
            return "TWENTY";
        else if (minute <= 27.5f)
            return "TWENTY FIVE";
        else if (minute <= 32.5f)
            return "THIRTY";
        else if (minute <= 37.5f)
            return "THIRTY FIVE";
        else if (minute <= 42.5f)
            return "FORTY";
        else if (minute <= 47.5f)
            return "FORTY FIVE";
        else if (minute <= 52.5f)
            return "FIFTY";
        else if (minute <= 57.5f)
            return "FIFTY FIVE";
        else
            return "MEHH";
    }
}
