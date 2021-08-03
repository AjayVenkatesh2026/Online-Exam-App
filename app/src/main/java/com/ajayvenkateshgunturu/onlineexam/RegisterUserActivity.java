package com.ajayvenkateshgunturu.onlineexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.Models.StudentModel;
import com.ajayvenkateshgunturu.onlineexam.Models.TeacherModel;
import com.ajayvenkateshgunturu.onlineexam.Students.StudentsMainActivity;
import com.ajayvenkateshgunturu.onlineexam.Teachers.TeachersMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class RegisterUserActivity extends AppCompatActivity implements myCallback {

    private TextInputEditText emailEditText, passwordEditText, nameEditText, confirmPasswordEditText;
    private Button registerButton, loginButton;
    private RadioGroup radioGroup;
    private FirebaseAuth authInstance;
    private DatabaseReference firebaseDatabaseReference;
    private String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        intiViews();

        setClickListeners();

    }


    private void intiViews() {
        emailEditText = findViewById(R.id.text_input_edit_text_register_email);
        nameEditText = findViewById(R.id.text_input_edit_text_register_name);
        passwordEditText = findViewById(R.id.text_input_edit_text_register_password);
        confirmPasswordEditText = findViewById(R.id.text_input_edit_text_register_confirm_password);

        radioGroup = findViewById(R.id.radio_group_container);

        registerButton = findViewById(R.id.button_register_register);
        loginButton = findViewById(R.id.button_register_login);

        authInstance = FirebaseAuth.getInstance();
        firebaseDatabaseReference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    }


    private void setClickListeners() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(RegisterUserActivity.this);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), LoginUserActivity.class);
                startActivity(i);
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button_teacher) {
                    userType = "Teacher";
                } else if (checkedId == R.id.radio_button_student) {
                    userType = "Student";
                }
            }
        });
    }

    private void registerUser(myCallback callback) {

        String email = Objects.requireNonNull(emailEditText.getText()).toString().trim().toLowerCase();
        String name = Objects.requireNonNull(nameEditText.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(confirmPasswordEditText.getText()).toString();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || confirmPassword.isEmpty() || userType.isEmpty()) {
            Toast.makeText(RegisterUserActivity.this, "Empty fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "make sure password in both fields are same", Toast.LENGTH_SHORT).show();
            return;
        }

        authInstance.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                    callback.uploadUserData(email, name, userType);
                    Log.e("Registration", "Successful");
                } else {
                    Log.e("Registration", "Failure");
                }
            }
        });
    }

    public void uploadUserData(String email, String name, String userType) {
        if (userType.equals("Teacher")) {
            TeacherModel teacher = new TeacherModel(email, name, userType, authInstance.getUid());
            teacher.log();
            firebaseDatabaseReference.child("Users").child(authInstance.getUid()).setValue("Teacher");
            firebaseDatabaseReference.child("Teachers").child(authInstance.getUid()).setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Upload Data Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            startActivity(new Intent(this, TeachersMainActivity.class));
            finish();
        } else if(userType.equals("Student")){
            StudentModel student = new StudentModel(email, name, userType, authInstance.getUid());
            student.log();
            firebaseDatabaseReference.child("Users").child(authInstance.getUid()).setValue("Student");
            firebaseDatabaseReference.child("Students").child(authInstance.getUid()).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Upload Data Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            startActivity(new Intent(this, StudentsMainActivity.class));
            finish();
        } else{
            firebaseDatabaseReference.child("User").setValue("None");
        }
    }
}

interface myCallback {
    void uploadUserData(String email, String name, String userType);
}