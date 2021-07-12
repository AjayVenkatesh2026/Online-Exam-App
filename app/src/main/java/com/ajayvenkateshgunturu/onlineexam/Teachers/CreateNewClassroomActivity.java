package com.ajayvenkateshgunturu.onlineexam.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

import static com.ajayvenkateshgunturu.onlineexam.Constants.FIREBASE_URL;

public class CreateNewClassroomActivity extends AppCompatActivity{

    private TextInputEditText classNameEditText, classDescriptionEditText;
    private Button createClassButton;
    DatabaseReference reference = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_classroom);
        
        initViews();

        setListeners();

    }

    private void initViews() {
        classNameEditText = findViewById(R.id.text_input_edit_text_class_name);
        classDescriptionEditText = findViewById(R.id.text_input_edit_text_class_description);
        createClassButton = findViewById(R.id.button_create_class);
    }


    private void setListeners() {
        createClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int classId = generateRandomId(6);
                String className = classNameEditText.getText().toString().trim();
                String classDescription = classDescriptionEditText.getText().toString().trim();
                if(className.isEmpty() || classDescription.isEmpty()){
                    Toast.makeText(CreateNewClassroomActivity.this, "empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                doesClassExist(new checkClassExistsListener() {
                    @Override
                    public void setResult(boolean result) {
                        if(result){
                            Toast.makeText(CreateNewClassroomActivity.this, "Result is true", Toast.LENGTH_SHORT).show();
                            Log.e("In OnClick result", "true");
                        }else{
                            Log.e("In OnClick result", "false");
                            createNewClass(classId, className, classDescription);
                        }
                    }
                }, classId);
            }
        });
    }

    private void createNewClass(int classId, String className, String classDescription) {
        HashMap<String, Object > map = new HashMap<>();
        map.put("classId", String.valueOf(classId));
        map.put("className", className);
        map.put("classDescription", classDescription);
        map.put("admin", auth.getUid());
        reference.child("Classes").child(String.valueOf(classId)).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.e("New Class", "Created");
                    Toast.makeText(CreateNewClassroomActivity.this, "New Class Created", Toast.LENGTH_SHORT).show();
                    uploadClassData(classId);
                }else{
                    Log.e("New Class", "Not Created");
                    Toast.makeText(CreateNewClassroomActivity.this, "New Class Not Created", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadClassData(int classId){
        reference.child("Teachers").child(auth.getUid()).child("Classes").child(String.valueOf(classId)).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.e("Upload of Class Data", "Successful");
                    startActivity(new Intent(getBaseContext(), TeachersMainActivity.class));
                    finish();
                }else{
                    Log.e("Uploading of data", "Failure\nTrying Again..........");
                    uploadClassData(classId);
                }
            }
        });
    }


    private void doesClassExist(checkClassExistsListener listener, int classId){
        reference.child("Classes").child(String.valueOf(classId)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
              @Override
              public void onComplete(@NonNull Task<DataSnapshot> task) {
                  if (task.isSuccessful()) {
                      DataSnapshot snapshot =  task.getResult();
                      if (snapshot.getValue() != null) {
                          listener.setResult(true);
                      } else {
                          listener.setResult(false);
                          Toast.makeText(CreateNewClassroomActivity.this.getBaseContext(), "Class doesn't Exist", Toast.LENGTH_LONG).show();
                      }
                  }
              }
          }
        );
    }

    public int generateRandomId(int num){
        int n = 0;
        Random rand = new Random();
        for(int i = 0; i < num; i++) {
            int t = rand.nextInt(10);
            if(i == 0){
                while(t == 0){
                    t = rand.nextInt(10);
                }
            }
            n = n*10 + t;
        }
        return n;
    }
}

interface checkClassExistsListener{
    void setResult(boolean result);
}