package com.ajayvenkateshgunturu.onlineexam.Teachers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.TextView;

import com.ajayvenkateshgunturu.onlineexam.Adapters.ClassItemAdapter;
import com.ajayvenkateshgunturu.onlineexam.R;

import java.util.ArrayList;

public class ShowClassActivity extends AppCompatActivity {

    ArrayList<String> array = new ArrayList<>();
    ArrayList<String> studentsArray = new ArrayList<>();
    RecyclerView classRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_class);

        TextView textView = findViewById(R.id.text_view_class_id);
        classRecyclerView = findViewById(R.id.recycler_view_class_items);

        String classId = getIntent().getExtras().getString("classId");
        textView.setText(classId);

        for(int i = 0; i < 20; i++){
            array.add(String.valueOf(i));
            studentsArray.add(String.valueOf(i));
        }

        ClassItemAdapter adapter = new ClassItemAdapter(array, studentsArray);
        classRecyclerView.setAdapter(adapter);
    }
}