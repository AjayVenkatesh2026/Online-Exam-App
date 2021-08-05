package com.ajayvenkateshgunturu.onlineexam.Students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajayvenkateshgunturu.onlineexam.Adapters.ConductTestAdapter;
import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.Models.TestHeaderModel;
import com.ajayvenkateshgunturu.onlineexam.Models.TestQuestionModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ConductTestActivity extends AppCompatActivity {

    private TestHeaderModel header;
    private CountDownTimer timer;
    private TextView countDownTextView;
    private ImageView submitIcon;
    private RecyclerView recyclerView;
    private ConductTestAdapter adapter;
    private ArrayList<TestQuestionModel> testQuestions = new ArrayList<>();
    private static final String TAG = "ConductTestActivity";

    private DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conduct_test);

        Bundle b = getIntent().getBundleExtra("header");
        header = TestHeaderModel.fromBundle(b);

        initViews();
        setAdapter();

        getTestQuestions();

    }

    private void initViews() {
        countDownTextView = findViewById(R.id.text_view_conduct_test_timer);
        submitIcon = findViewById(R.id.image_view_submit_icon);
        recyclerView = findViewById(R.id.recycler_view_conduct_test);

        submitIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        countDownTextView.setText(timeToString(Long.parseLong(header.getDuration()), 0, 0));
    }

    private void setAdapter(){
        adapter = new ConductTestAdapter(this, testQuestions);
        recyclerView.setAdapter(adapter);
    }

    private void getTestQuestions(){
        reference.child("Tests").child(header.getTestId()).child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    TestQuestionModel questions = s.getValue(TestQuestionModel.class);
                    testQuestions.add(questions);
                    adapter.notifyItemAdded();
                    adapter.notifyItemInserted(testQuestions.size());
                }

                startTimer(Long.parseLong(header.getDuration()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DatabaseError: " + error.getMessage());
            }
        });
    }

    private void startTimer(long duration){
        timer = new CountDownTimer(TimeUnit.MINUTES.toMillis(duration), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)%24;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)%60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)%60;
                countDownTextView.setText(timeToString(hours, minutes, seconds));
            }

            @Override
            public void onFinish() {
                submit();
            }
        };
        timer.start();
    }

    private void submit(){
        ArrayList<Integer> answers = adapter.getAnswers();
        int score = 0;
        for(int i = 0; i < answers.size(); i++){
            if(Integer.parseInt(testQuestions.get(i).getAns()) == answers.get(i)){
                score++;
            }
        }
        Log.e(TAG, "Score: " + score);
        Log.e(TAG, "submitted the test");
    }

    private String timeToString(long hours, long minutes, long seconds){
        return n(hours) + " : " + n(minutes) + " : " + n(seconds);
    }

    private String n(long n){
        return n > 9 ? "" + n: "0" + n;
    }

    @Override
    protected void onDestroy() {
        if(timer != null)
            timer.cancel();
        super.onDestroy();
    }
}