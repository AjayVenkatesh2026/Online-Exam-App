package com.ajayvenkateshgunturu.onlineexam.Teachers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.ajayvenkateshgunturu.onlineexam.Adapters.TestResultsAdapter;
import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.Models.TestHeaderModel;
import com.ajayvenkateshgunturu.onlineexam.Models.TestResultModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowTestResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<TestResultModel> resultModels = new ArrayList<>();
    private TestResultsAdapter adapter;
    private final DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final String TAG = "ShowTestResultsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_test_results);


        String testId = getIntent().getExtras().getString("testId");

        initViews();

        getTestResults(testId);
    }

    private void getTestResults(String testId) {
        reference.child("Tests").child(testId).child("Results").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot s: snapshot.getChildren()){
                        TestResultModel result = s.getValue(TestResultModel.class);
                        resultModels.add(result);
                        adapter.notifyItemInserted(resultModels.size());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DatabaseError: " + error.getMessage());
            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view_test_results);

        adapter = new TestResultsAdapter(this, resultModels);
        recyclerView.setAdapter(adapter);
    }
}