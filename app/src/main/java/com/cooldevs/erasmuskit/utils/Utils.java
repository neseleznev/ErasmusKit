package com.cooldevs.erasmuskit.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class
 */
public final class Utils {

    /**
     * Hides the software keyboard
     * @param activity calling Activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Gets a date in a string format that corresponds to the provided timestamp
     * @param timestamp the timestamp we want to convert to string date
     * @return string date
     */
    public static String getDateString(long timestamp) {
        DateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        Date date = new Date(timestamp);

        return df.format(date);
    }

    /**
     * Gets the timestamp for a given date in string format
     * @param dateString date (string) that we want to convert
     * @return the corresponding date in timestamp format
     */
    static long getTimestamp(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Some basic string concatanation operation
     * @param noun parameter
     * @return concatenated string
     */
    public static String toPossessive(String noun) {
        String possessive = (noun.charAt(noun.length() - 1) == 's') ? "'" : "'s";
        return noun + possessive;
    }
}
