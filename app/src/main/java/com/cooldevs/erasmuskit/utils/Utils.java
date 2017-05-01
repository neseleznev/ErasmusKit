package com.cooldevs.erasmuskit.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

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
     * Calculate number of columns for the RecyclerView arrangement (Grid Layout)
     * @param resources Resourses object obtained from Activity
     * @return int
     */
    public static int getNumberOfColumns(Resources resources) {
        // Get screen dimensions (width) for the RecyclerView arrangement
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        int numColumns = 1;
        if (resources.getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            numColumns = (int) Math.ceil(dpWidth / 520f);
        }
        return numColumns;
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
        DateFormat dateFormat;
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException | IllegalArgumentException e) {
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
