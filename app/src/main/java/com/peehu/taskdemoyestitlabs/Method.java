package com.peehu.taskdemoyestitlabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;
import android.util.Patterns;

import java.util.regex.Pattern;

public class Method {
    private static final String TAG = Method.class.getSimpleName();
    String DataBaseName;
    String Emp_ID;

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        }
        return Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$").matcher(target).matches();
    }

    public static final boolean isValidPhone(CharSequence target) {
        boolean z = false;
        if (target == null) {
            return false;
        }
        if (Patterns.PHONE.matcher(target).matches() && target.length() >= 10 && target.length() <= 10) {
            z = true;
        }
        return z;
    }

    public static boolean isNetworkAvailable(Context context) {
        @SuppressLint("WrongConstant") ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity == null) {
            return false;
        }
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info != null) {
            for (NetworkInfo state : info) {
                if (state.getState() == State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getPreferences(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "");
    }

    public static void savePreferences(Context context, String key, String value) {
        Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String DataBaseName(Context context) {
        String Emp_ID, DatabaseName;
        Emp_ID = Method.getPreferences(context, "EmpCode");
        if (Emp_ID.equalsIgnoreCase("5")) {
            DatabaseName = "TestingBTCGRDDatabs";
        } else if (Emp_ID.equalsIgnoreCase("6")) {
            DatabaseName = "TestingBTCGRDDatabs";
        } else if (Emp_ID.equalsIgnoreCase("9")) {
            DatabaseName = "TestingBTCGRDDatabs";
        } else if (Emp_ID.equalsIgnoreCase("10")) {
            DatabaseName = "TestingBTCGRDDatabs";
        } else {
            DatabaseName = "BTCGRData";
        }
        return DatabaseName;
    }
}
