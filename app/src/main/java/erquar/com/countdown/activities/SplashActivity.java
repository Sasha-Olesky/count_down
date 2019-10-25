package erquar.com.countdown.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import erquar.com.countdown.R;

/**
 * Created by Administrator on 7/22/2016.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(){
            @Override
            public void run() {
//                NationCalender.readCalendarEvent(SplashActivity.this);
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
