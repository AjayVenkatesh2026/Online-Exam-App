package com.ajayvenkateshgunturu.onlineexam.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import static com.ajayvenkateshgunturu.onlineexam.Constants.FIREBASE_URL;

public class CreateNewClassroomActivity extends AppCompatActivity implements checkClassExistsListener{

    private TextInputEditText classNameEditText, classDescriptionEditText;
    private Button createClassButton;
    public boolean result;
    DatabaseReference reference = FirebaseDatabase.getInstance(FIREBASE_URL).getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_classroom);
        
        initViews();

        setListeners();

    }

    private void setListeners() {
        createClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int classId = 123456;
                checkClassExistsListener l = CreateNewClassroomActivity.this;
                l.doesClassExists(classId);
                if(result){
                    Toast.makeText(CreateNewClassroomActivity.this, "Result is true", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("In OnClick result", "false");
                }
                //FirebaseDatabase.getInstance(FIREBASE_URL).getReference("Classes").child(String.valueOf(classId)).child("admin").setValue(FirebaseAuth.getInstance().getUid());
            }
        });
    }

    private void initViews() {
        classNameEditText = findViewById(R.id.text_input_edit_text_class_name);
        classDescriptionEditText = findViewById(R.id.text_input_edit_text_class_description);
        createClassButton = findViewById(R.id.button_create_class);
    }

    public void doesClassExists(int classId){
//        AtomicBoolean result = new AtomicBoolean(false);
        result = false;
        reference.child("Classes").child(String.valueOf(classId)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
              @Override
              public void onComplete(@NonNull Task<DataSnapshot> task) {
                  if (task.isSuccessful()) {
                      DataSnapshot snapshot =  task.getResult();
                      String res = "None";
                      if (snapshot != null) {
                          res = snapshot.getValue().toString();
                          result = true;
                      } else {
                          Toast.makeText(CreateNewClassroomActivity.this.getBaseContext(), "Class doesn't Exist", Toast.LENGTH_LONG).show();
                      }
                      Log.e("Class Object", res);
                      Log.e("In OnComplete result", String.valueOf(result));
                  }
              }
          }
        );
    }
}

interface checkClassExistsListener{
    void doesClassExists(int classId);
}