package com.cooldevs.erasmuskit;

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
final class Utils {

    static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    static String getDateString(long timestamp) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date date = new Date(timestamp);

        return df.format(date);
    }

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

    static String toPossessive(String noun) {
        String possessive = (noun.charAt(noun.length() - 1) == 's') ? "'" : "'s";
        return noun + possessive;
    }
}
