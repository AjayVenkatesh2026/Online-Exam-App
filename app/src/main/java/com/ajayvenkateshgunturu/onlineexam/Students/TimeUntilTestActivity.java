package com.ajayvenkateshgunturu.onlineexam.Students;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import com.ajayvenkateshgunturu.onlineexam.R;

import java.util.concurrent.TimeUnit;

public class TimeUntilTestActivity extends AppCompatActivity {

    private long differenceInTime;
    private boolean timeExceeded;
    private CountDownTimer timer;
    private long days, hours, minutes, seconds;
    private Bundle header;
    private TextView daysCounter, hoursCounter, minutesCounter, secondsCounter;

    private static final String TAG = "TimeUntilTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_until_test);


        Bundle extras = getIntent().getExtras();
        differenceInTime = extras.getLong("differenceInTime");
        timeExceeded = extras.getBoolean("timeExceeded");
        header = extras.getBundle("header");
        Log.e("differenceInTime", String.valueOf(differenceInTime));

        initViews();

        if(!timeExceeded)
            startCountDown();
    }

    private void initViews() {
        daysCounter = findViewById(R.id.text_view_wait_until_test_days);
        hoursCounter = findViewById(R.id.text_view_wait_until_test_hours);
        minutesCounter = findViewById(R.id.text_view_wait_until_test_minutes);
        secondsCounter = findViewById(R.id.text_view_wait_until_test_seconds);

        days = TimeUnit.MILLISECONDS.toDays(differenceInTime);
        hours = TimeUnit.MILLISECONDS.toHours(differenceInTime)%24;
        minutes = TimeUnit.MILLISECONDS.toMinutes(differenceInTime)%60;
        seconds = TimeUnit.MILLISECONDS.toSeconds(differenceInTime)%60;

        daysCounter.setText(days + "\nDays");
        hoursCounter.setText(hours + "\nHours");
        minutesCounter.setText(minutes + "\nMins");
        secondsCounter.setText(seconds + "\nSecs");
        Log.e(TAG, "Seconds: " + seconds);
    }

    private void startCountDown(){
        timer = new CountDownTimer(differenceInTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)%24;
                minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)%60;
                seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)%60;

                daysCounter.setText(days + "\nDays");
                hoursCounter.setText(hours + "\nHours");
                minutesCounter.setText(minutes + "\nMins");
                secondsCounter.setText(seconds + "\nSecs");
                Log.e(TAG, "Seconds: " + seconds);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(getBaseContext(), ConductTestActivity.class);
                intent.putExtra("header", header);
                startActivity(intent);
                finish();
            }
        };
        timer.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}