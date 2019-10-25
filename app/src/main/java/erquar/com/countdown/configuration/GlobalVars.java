package erquar.com.countdown.configuration;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 7/22/2016.
 */
public class GlobalVars {


    private static String PRODUCE_DATE_KEY = "END_DATE_KEY";
    private static String START_DATE_KEY = "START_DATE_KEY";
    private static String FEST_KEY = "FEST_KEY";
    private static String DAYS_KEY = "INC_DAYS";
    private static String REPEAT_KEY = "REPEAT_KEY";
    private static String PASS_KEY = "PASS_KEY";
    private static String INPUT_TYPE = "INPUT_TYPE";

    private static final SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static final boolean putDateValue(Context context,
                                             String value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(PRODUCE_DATE_KEY, value);
        return editor.commit();
    }


    public static final String getDateValue(Context context) {
        return getSharedPreference(context).getString(PRODUCE_DATE_KEY, "");
    }

    public static final boolean putStartDateValue(Context context,
                                             String value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(START_DATE_KEY, value);
        return editor.commit();
    }

    public static final String getStartDateValue(Context context) {
        return getSharedPreference(context).getString(START_DATE_KEY, "");
    }

    public static final boolean getFestValue(Context context) {
        return getSharedPreference(context).getBoolean(FEST_KEY, false);
    }

    public static final boolean putFesteValue(Context context,
                                              boolean value) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(FEST_KEY, value);
        return editor.commit();
    }

    public static boolean putDaysValue (Context context, int days) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(DAYS_KEY, days);
        return editor.commit();
    }
    public static Integer getDaysValue (Context context) {
        return getSharedPreference(context).getInt(DAYS_KEY, 0);
    }

    public static Integer getRepeatValue (Context context) {
        return getSharedPreference(context).getInt(REPEAT_KEY, 0);
    }
    public static boolean putRepeatValue (Context context, int days) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(REPEAT_KEY, days);
        return editor.commit();
    }

    public static Integer getPassDaysValue (Context context) {
        return getSharedPreference(context).getInt(PASS_KEY, 0);
    }
    public static boolean putPassDaysValue (Context context, int days) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(PASS_KEY, days);
        return editor.commit();
    }

    public static Integer getInputType (Context context) {
        return getSharedPreference(context).getInt(INPUT_TYPE, 2);
    }
    public static boolean putInputType (Context context, int val) {
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(INPUT_TYPE, val);
        return editor.commit();
    }

    public static void resetPreferenceData (Context context) {
        GlobalVars.putStartDateValue(context, NationCalender.getCurrentDate());
        GlobalVars.putFesteValue(context, false);
        GlobalVars.putDaysValue(context, 0);
        GlobalVars.putRepeatValue(context, 0);
        GlobalVars.putPassDaysValue(context, 0);
    }
    public static void showAlert (Context context, String title, String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {

                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static void sendeMail (Context context, String strSubject, String strBody, String strSender, String strRecipients, String strPW) {
//        try {
//            GMailSender sender = new GMailSender(strSender, strPW);
//            sender.sendMail(strSubject,
//                    strBody,
//                    strSender,
//                    strRecipients);
//        } catch (Exception e) {
//            Log.e("SendMail", e.getMessage(), e);
//        }
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ strRecipients});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, strSubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, strBody);

        //need this to prompts email client only
        emailIntent.setType("message/rfc822");

        context.startActivity(Intent.createChooser(emailIntent, "Select an Email Client:"));
    }

    public static List<Event> getFestivalDays (Context mContext, String nation, String nation_url) {
        List<Event> items = new ArrayList<>();
        com.google.api.services.calendar.Calendar client = null;
        List<String> scope = new ArrayList<>();
        scope.add(CalendarScopes.CALENDAR);
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(mContext, scope);

        credential.setSelectedAccountName(getGoogleAccountName(mContext));
        client = getCalendarService(credential);
        String pageToken = null;
        do {
            com.google.api.services.calendar.model.Events events = null;
            try {
//                events = client.events().list("en.usa#holiday@group.v.calendar.google.com").setPageToken(pageToken).execute();
                events = client.events().list("en.usa#holiday@group.v.calendar.google.com").setMaxResults(10)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(events != null) {
                items = events.getItems();
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        return items;
    }

    public static com.google.api.services.calendar.Calendar getCalendarService(GoogleAccountCredential credential) {
        return new com.google.api.services.calendar.Calendar.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential).build();
    }

    public static String getGoogleAccountName (Context mContext) {
        AccountManager manager = (AccountManager) mContext.getSystemService(mContext.ACCOUNT_SERVICE);
        Account[] list = manager.getAccounts();
        String gmail = null;

        for(Account account: list)
        {
            if(account.type.equalsIgnoreCase("com.google"))
            {
                gmail = account.name;
                break;
            }
        }
        return gmail;
    }
}
