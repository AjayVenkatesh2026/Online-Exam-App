package com.ajayvenkateshgunturu.onlineexam.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ajayvenkateshgunturu.onlineexam.Adapters.ClassItemAdapter;
import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowClassActivity extends AppCompatActivity {

    ArrayList<String> teachersArray = new ArrayList<>();
    ArrayList<String> studentsArray = new ArrayList<>();
    RecyclerView classRecyclerView;
    DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ClassItemAdapter adapter;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_class);

        TextView textView = findViewById(R.id.text_view_class_id);
        classRecyclerView = findViewById(R.id.recycler_view_class_items);

        String classId = getIntent().getExtras().getString("classId");
        textView.setText(classId);

        teachersArray.add("Teachers");
        studentsArray.add("Students");

        adapter = new ClassItemAdapter(teachersArray, studentsArray);
        classRecyclerView.setAdapter(adapter);

        getTeachers(classId);
    }

    private void getTeachers(String classId){
        reference.child("Classes").child(classId).child(Constants.TYPE_TEACHERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    teachersArray.add(s.getValue(String.class));
                }
                Log.e("teachersArray", teachersArray.toString());
                adapter.setTeachersArrayList(teachersArray);
                adapter.notifyDataSetChanged();
                getStudents(classId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getStudents(String classId){
        reference.child("Classes").child(classId).child(Constants.TYPE_STUDENTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    studentsArray.add(s.getValue(String.class));
                }
                Log.e("studentsArray", studentsArray.toString());
                adapter.setStudentsArrayList(studentsArray);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAdapter() {
        adapter = new ClassItemAdapter(teachersArray, studentsArray);
        classRecyclerView.setAdapter(adapter);
    }
}