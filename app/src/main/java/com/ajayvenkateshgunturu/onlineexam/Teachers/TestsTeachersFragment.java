package com.ajayvenkateshgunturu.onlineexam.Teachers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajayvenkateshgunturu.onlineexam.Adapters.TestFragmentAdapter;
import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.Models.TestQuestionModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TestsTeachersFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ArrayList<TestQuestionModel> testsArrayList = new ArrayList<>();
    private TestFragmentAdapter adapter;

    public TestsTeachersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tests_teachers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_teachers_tests);

        setAdapter();
        downloadData();
    }

    private void setAdapter() {
        adapter = new TestFragmentAdapter(testsArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void downloadData() {

        //readTestIds function call is to
        readTestIds(new TestIdsListener() {
            @Override
            public void setTestIds(ArrayList<Integer> testIds) {

                //function call to retrieve data of corresponding test from the testIds array
                readTestsData(new addTestToArrayListener() {
                    @Override
                    public void addTestToArray(TestQuestionModel testQuestionModel) {

                        testsArrayList.add(testQuestionModel);
                        adapter.notifyDataSetChanged();
                    }
                }, testIds);

            }
        });
    }

    private void readTestIds(TestIdsListener listener){
        reference.child(Constants.TYPE_TEACHERS).child(mAuth.getUid()).child("Tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Integer> arr = new ArrayList<>();
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        if (dataSnapshot.exists()){
                            String key = dataSnapshot.getKey();
                            assert key != null;
                            arr.add(Integer.parseInt(key));
                        }else{
                            Log.e("Snapshot", "Doesn't exist");
                        }
                    }
                }
                listener.setTestIds(arr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());
            }
        });
    }

    private void readTestsData(addTestToArrayListener listener, ArrayList<Integer> arrayList){

        for(int testId: arrayList){
            reference.child("Tests").child(String.valueOf(testId)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            TestQuestionModel testQuestionModel = dataSnapshot.getValue(TestQuestionModel.class);
                            if(testQuestionModel.getType().equals("Header")){
                                listener.addTestToArray(testQuestionModel);
                                testQuestionModel.log();
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Database Error",error.getMessage());
                }
            });
        }

    }
}

interface TestIdsListener{
    void setTestIds(ArrayList<Integer> testIds);
}

interface addTestToArrayListener{
    void addTestToArray(TestQuestionModel testQuestionModel);
}