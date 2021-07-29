package com.ajayvenkateshgunturu.onlineexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.Students.StudentsMainActivity;
import com.ajayvenkateshgunturu.onlineexam.Teachers.TeachersMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginUserActivity extends AppCompatActivity implements redirectListener{

    private TextInputEditText emailEditText, passwordEditText;
    private Button registerButton, loginButton;
    private FirebaseAuth authInstance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        intViews();

        loginButton.callOnClick();
    }

    private void intViews() {
        emailEditText =findViewById(R.id.text_input_edit_text_login_email);
        passwordEditText = findViewById(R.id.text_input_edit_text_login_password);
        registerButton = findViewById(R.id.button_login_register);
        loginButton = findViewById(R.id.button_login_login);
        authInstance = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegisterUserActivity.class));
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(LoginUserActivity.this);
            }
        });
    }

    private void loginUser(redirectListener listener) {
        String email = Objects.requireNonNull(emailEditText.getText()).toString().toLowerCase().trim();
        String password = Objects.requireNonNull(passwordEditText.getText()).toString();

        email = "mail2@gmail.com";
        password = "password";

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Empty fields", Toast.LENGTH_SHORT).show();
            return;
        }

        authInstance.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Log.e("Login", "Successful");
                    listener.redirectUser();
                }else{
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    Log.e("Login", "Failure");
                }
            }
        });
    }

    @Override
    public void redirectUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Log.e("UId", auth.getUid());
        FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference().child("Users").child(Objects.requireNonNull(auth.getUid())).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Intent intent;
                    String userType = Objects.requireNonNull(task.getResult()).getValue().toString();
                    Log.e("userType", userType);
                    if(userType.equals("Teacher")){
                        intent = new Intent(LoginUserActivity.this, TeachersMainActivity.class);
                    }else if(userType.equals("Student")){
                        intent = new Intent(LoginUserActivity.this, StudentsMainActivity.class);
                    }else{
                        intent = new Intent(LoginUserActivity.this, RegisterUserActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }else{
                    Log.e("Task", "Failure");
                }
            }
        });

    }
}

interface redirectListener{
    void redirectUser();
}