package erquar.com.countdown.task;

import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import erquar.com.countdown.activities.MainActivity;
import erquar.com.countdown.configuration.GlobalVars;

/**
 * Created by Administrator on 7/24/2016.
 */
public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private MainActivity mActivity;

    /**
     * Constructor.
     * @param activity MainActivity that spawned this task.
     */
    public ApiAsyncTask(MainActivity activity) {
        this.mActivity = activity;
    }

    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {

//            mActivity.updateResultsText(getDataFromApi());
            List<String> temp = getDataFromApi();
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private List<String> getDataFromApi() throws IOException {
        com.google.api.services.calendar.Calendar client = null;
        List<String> scope = new ArrayList<>();
        scope.add(CalendarScopes.CALENDAR);
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(mActivity, scope);

        credential.setSelectedAccountName(GlobalVars.getGoogleAccountName(mActivity));
        client = GlobalVars.getCalendarService(credential);
        String pageToken = null;

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<String>();
        Events events = client.events().list("en.usa#holiday@group.v.calendar.google.com")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));
        }
        return eventStrings;
    }

}