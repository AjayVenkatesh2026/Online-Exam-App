package com.ajayvenkateshgunturu.onlineexam.Students;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.LoginUserActivity;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.ajayvenkateshgunturu.onlineexam.Teachers.ClassesTeachersFragment;
import com.ajayvenkateshgunturu.onlineexam.Teachers.ProfileTeachersFragment;
import com.ajayvenkateshgunturu.onlineexam.Teachers.TeachersMainActivity;
import com.ajayvenkateshgunturu.onlineexam.Teachers.TestsTeachersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class StudentsMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private NavigationBarView.OnItemSelectedListener onItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            if(id == R.id.menu_item_classes){
                Toast.makeText(getBaseContext(), "Classes Clicked", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container_view_student, ClassesStudentsFragment.class, null).commit();
            }
            else if(id == R.id.menu_item_profile){
                Toast.makeText(getBaseContext(), "Profile Clicked", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container_view_student, ProfileStudentsFragment.class, null).commit();
            }
            else if(id == R.id.menu_item_tests){
                Toast.makeText(getBaseContext(), "Tests Clicked", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container_view_student, TestsStudentsFragment.class, null).commit();
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_main);

        intiViews();

        setListeners();
    }

    private void setListeners() {
        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.menu_item_classes);
    }

    private void intiViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_students);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_students, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_join_class:
                JoinAClassFragment joinAClassFragment = new JoinAClassFragment();
                joinAClassFragment.show(getSupportFragmentManager(), "Join a new Class");
                Toast.makeText(this, "Join a class clicked!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_item_student_logout:
                mAuth.signOut();
                Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginUserActivity.class));
                finish();
                break;
            default:
                return true;
        }
        return true;
    }
}