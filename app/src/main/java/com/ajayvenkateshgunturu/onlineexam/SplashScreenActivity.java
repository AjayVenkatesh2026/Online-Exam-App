package com.ajayvenkateshgunturu.onlineexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.Students.StudentsMainActivity;
import com.ajayvenkateshgunturu.onlineexam.Teachers.TeachersMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashScreenActivity extends AppCompatActivity {

    Intent intent;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference ref = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(SplashScreenActivity.this, LoginUserActivity.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(auth.getCurrentUser()!=null){
                    Log.e("Current User", "Present");
                    getCurrentUserType(new getUserTypeListener() {
                        @Override
                        public void setUserType(String userType) {
                            if(userType.equals("Teacher")){
                                intent = new Intent(SplashScreenActivity.this, TeachersMainActivity.class);
                            }else if(userType.equals("Student")){
                                intent = new Intent(SplashScreenActivity.this, StudentsMainActivity.class);
                            }
                            Log.e("user type", userType);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else{
                    startActivity(intent);
                }

            }
        }, 1000);
    }

    public void getCurrentUserType(getUserTypeListener listener){

        String uid = auth.getUid();
        ref.child("Users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String userType = task.getResult().getValue().toString();
                    listener.setUserType(userType);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Getting Data", "Failed");
                Toast.makeText(getBaseContext(), "Client is offline\nTrying again.........", Toast.LENGTH_SHORT).show();
                getCurrentUserType(listener);
            }
        });
    }
}

interface getUserTypeListener{
    void setUserType(String userType);
}