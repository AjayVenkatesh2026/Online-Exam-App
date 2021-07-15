package com.ajayvenkateshgunturu.onlineexam.Teachers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ajayvenkateshgunturu.onlineexam.Adapters.TestsAdapter;
import com.ajayvenkateshgunturu.onlineexam.Models.TestQuestionModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CreateNewTestActivity extends AppCompatActivity implements addNewTestQuestionListener{

    FloatingActionButton addNewQuestion;
    private RecyclerView recyclerView;
    private TestsAdapter adapter;
    ArrayList<TestQuestionModel> testQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_test);

        initViews();

        setListeners();

        setAdapter();
    }

    private void setAdapter() {
        adapter = new TestsAdapter(testQuestions);
        recyclerView.setAdapter(adapter);
    }

    private void setListeners() {
        addNewQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddQuestionToTestFragment addQuestionToTestFragment = new AddQuestionToTestFragment();
                addQuestionToTestFragment.show(getSupportFragmentManager(), "Some random Tag");
            }
        });
    }

    private void initViews() {
        addNewQuestion = findViewById(R.id.floating_action_button_add_new_question);
        recyclerView = findViewById(R.id.recycler_view_create_new_test);
    }

    @Override
    protected void onResume() {
        super.onResume();

        showTestHeaderDialog();
    }

    private void showTestHeaderDialog() {
        AddHeaderToTestFragment addHeaderToTestFragment = new AddHeaderToTestFragment();
        addHeaderToTestFragment.show(getSupportFragmentManager(),"Test Header Dialog");
    }

    @Override
    public void addNewQuestion(TestQuestionModel testQuestion) {
        testQuestions.add(testQuestion);
    }
}

interface addNewTestQuestionListener{
    void addNewQuestion(TestQuestionModel testQuestion);
}