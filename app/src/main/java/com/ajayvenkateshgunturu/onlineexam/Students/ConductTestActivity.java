package com.ajayvenkateshgunturu.onlineexam.Students;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.ajayvenkateshgunturu.onlineexam.R;

public class ConductTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conduct_test);

        Bundle b = getIntent().getBundleExtra("header");
        Log.e("Bundle", b.toString());
    }
}