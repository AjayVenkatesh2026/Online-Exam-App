package com.ajayvenkateshgunturu.onlineexam.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.LoginUserActivity;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class TeachersMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_main);

        intiViews();

        setListeners();
    }

    private void setListeners() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.menu_item_classes){
                    Toast.makeText(TeachersMainActivity.this, "Classes Clicked", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container_view_teacher, ClassesTeachersFragment.class, null).commit();
                    return true;
                }else if(id == R.id.menu_item_profile){
                    Toast.makeText(TeachersMainActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container_view_teacher, ProfileTeachersFragment.class, null).commit();
                    return true;
                }else if(id == R.id.menu_item_tests){
                    Toast.makeText(TeachersMainActivity.this, "Tests Clicked", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment_container_view_teacher, TestsTeachersFragment.class, null).commit();
                    return true;
                }
                return false;
            }
        });
    }

    private void intiViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_teachers);
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.fragment_container_view_teacher, ClassesTeachersFragment.class, null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_meu_teachers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_create_class:
                Toast.makeText(this, "Create Class clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, CreateNewClassroomActivity.class));
                break;
            case R.id.menu_item_new_test:
                Toast.makeText(this, "New Test Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_item_sign_out:
                Toast.makeText(this, "Sign out Clicked", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginUserActivity.class));
                finish();
                break;
            default:
                return false;
        }
        return true;
    }
}