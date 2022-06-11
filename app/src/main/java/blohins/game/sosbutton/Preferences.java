package blohins.game.sosbutton;

import android.content.Context;
import android.preference.PreferenceManager;

import javax.crypto.Cipher;

public class Preferences {

    private static final String PREF_LAST_NUM = "lastNum";
    private static final String PREF_LAST_TIME = "lastTime";
    private static final String PREF_LAST_MESSAGE = "lastMessage";
    private static final String PREF_STATE_INDEX = "stateIndex";
    private static final String PREF_SECONDS_LEFT = "secondsLeft";
    private static final String PREF_ACTION = "action";

    public static String getLastNum(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_NUM, null);
    }
    public static void setLastNum(Context context, String lastResult) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_NUM, lastResult)
                .apply();
    }
    public static String getLastTime(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_TIME, null);
    }
    public static void setLastTime(Context context, String lastResult) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_TIME, lastResult)
                .apply();
    }
    public static String getLastMessage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_MESSAGE, "0");
    }
    public static void setLastMessage(Context context, String lastResult) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_MESSAGE, lastResult)
                .apply();
    }

    public static void setStateIndex(Context context, String index) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_STATE_INDEX, index)
                .apply();
    }

    public static String getStateIndex(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_STATE_INDEX, "0");
    }

    public static void setSecondsLeft(Context context, String seconds) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SECONDS_LEFT, seconds)
                .apply();
    }

    public static String getSecondsLeft (Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SECONDS_LEFT, "0");
    }

    public static void setAction(Context context, String action) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_ACTION, action)
                .apply();
    }

    public static String getAction(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_ACTION, null);
    }

    public static void clearPreferences(Context context, String index) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .clear()
                .apply();
        Preferences.setStateIndex(context, index);
    }
}
