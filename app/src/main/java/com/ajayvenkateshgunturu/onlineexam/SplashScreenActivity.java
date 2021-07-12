package com.ajayvenkateshgunturu.onlineexam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ajayvenkateshgunturu.onlineexam.Students.StudentsMainActivity;
import com.ajayvenkateshgunturu.onlineexam.Teachers.TeachersMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreenActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(SplashScreenActivity.this, LoginUserActivity.class);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
        if(auth.getCurrentUser()!=null){
            String uid = auth.getUid();
            String userType = ref.child("Users").child(uid).get().toString();
            if(userType.equals("Teacher")){
                intent = new Intent(this, TeachersMainActivity.class);
            }else if(userType.equals("Student")){
                intent = new Intent(this, StudentsMainActivity.class);
            }
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}