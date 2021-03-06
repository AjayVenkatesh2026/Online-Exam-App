package com.ajayvenkateshgunturu.onlineexam.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.Adapters.TestsAdapter;
import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.Models.TestHeaderModel;
import com.ajayvenkateshgunturu.onlineexam.Models.TestQuestionModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateNewTestActivity extends AppCompatActivity implements addNewTestQuestionListener, setHeaderListener{

    private FloatingActionButton addNewQuestion;
    private Button exitButton, createNewTestButton;
    private RecyclerView recyclerView;
    private TestsAdapter adapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    ArrayList<TestQuestionModel> testQuestions = new ArrayList<>();
    TestHeaderModel headerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_test);

        initViews();

        setListeners();

        setAdapter();

    }

    private void setAdapter() {
        adapter = new TestsAdapter();
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

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateNewTestActivity.this, TeachersMainActivity.class));
                finish();
            }
        });

        createNewTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int TestUniqueId = CreateNewClassroomActivity.generateRandomId(7);
                Log.e("Create new test button", "clicked");

                uploadData(new checkIfTestIdExists(){
                    @Override
                    public void setResult(boolean result) {
                        if(result){
                            Toast.makeText(CreateNewTestActivity.this, "Same test Id\n trying again...", Toast.LENGTH_SHORT).show();
                            createNewTestButton.callOnClick();
                        }else{
                            headerModel.setTestId(String.valueOf(TestUniqueId));
                            reference.child("Teachers").child(mAuth.getUid()).child("Tests").child(String.valueOf(TestUniqueId)).setValue("true");
                            DatabaseReference testRef = reference.child("Tests").child(String.valueOf(TestUniqueId));
                            testRef.child("Header").setValue(headerModel);
                            for(TestQuestionModel t: testQuestions){
                                testRef.child("Questions").push().setValue(t);
                            }
                        }

                        startActivity(new Intent(CreateNewTestActivity.this, TeachersMainActivity.class));
                        finish();
                    }
                }, TestUniqueId);
            }
        });
    }

    private void initViews() {
        addNewQuestion = findViewById(R.id.floating_action_button_add_new_question);
        recyclerView = findViewById(R.id.recycler_view_create_new_test);

        createNewTestButton = findViewById(R.id.button_create_new_test);
        exitButton = findViewById(R.id.button_exit);
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
        adapter.notifyItemInserted(testQuestions.size());
    }

    @Override
    public void setHeaderModel(TestHeaderModel headerModel) {
        this.headerModel = headerModel;

        adapter.setHeader(headerModel);
        adapter.setArrayList(testQuestions);
        adapter.notifyItemInserted(0);
    }

    private void uploadData(checkIfTestIdExists listener, int testId){
        reference.child("Tests").child(String.valueOf(testId)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.getResult().getValue() == null){
                    listener.setResult(false);
                }else{
                    listener.setResult(true);
                }
            }
        });
    }
}

interface setHeaderListener{
    void setHeaderModel(TestHeaderModel testHeaderModel);
}

interface addNewTestQuestionListener{
    void addNewQuestion(TestQuestionModel testQuestion);
}

interface checkIfTestIdExists{
    void setResult(boolean result);
}