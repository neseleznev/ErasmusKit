package com.cooldevs.erasmuskit;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
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
}
