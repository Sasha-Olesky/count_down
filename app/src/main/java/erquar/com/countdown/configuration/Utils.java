package erquar.com.countdown.configuration;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import erquar.com.countdown.R;

/**
 * Created by Administrator on 7/22/2016.
 */
public class Utils {

    public static String APP_PREFERENCE = "app_preference";
    public static String IS_ICON_CREATED = "IS_ICON_CREATED";

    public static void showLongToast(Context context, String pMsg) {
        Toast.makeText(context, pMsg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, String pMsg) {
        Toast.makeText(context, pMsg, Toast.LENGTH_SHORT).show();
    }

    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }

    public static void start_Activity(Activity activity, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);

        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity == null) {
                Log.w("Utility", "couldn't get connectivity manager");
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].isAvailable()) {
                            Log.d("Utility", "network is available");
                            return true;
                        }
                    }
                }
            }
        }
        Log.d("Utility", "network is not available");
        return false;
    }

    public static Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }
    private static float sDensity = 0;

    /**
     *
     * @param context
     * @param nDip
     * @return
     */
    public static int dipToPixel(Context context, int nDip) {
        if (sDensity == 0) {
            final WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            sDensity = dm.density;
        }
        return (int) (sDensity * nDip);
    }

}
