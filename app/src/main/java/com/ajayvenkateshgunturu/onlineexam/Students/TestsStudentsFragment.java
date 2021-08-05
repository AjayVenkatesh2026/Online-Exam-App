package com.ajayvenkateshgunturu.onlineexam.Students;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.Adapters.TestsStudentFragmentAdapter;
import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.DialogFragments.ShowInstructionsDialogFragment;
import com.ajayvenkateshgunturu.onlineexam.DialogFragments.ShowTestScoreFragment;
import com.ajayvenkateshgunturu.onlineexam.Models.TestCounterModel;
import com.ajayvenkateshgunturu.onlineexam.Models.TestHeaderModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TestsStudentsFragment extends Fragment implements TestsStudentFragmentAdapter.OnTestItemClickListener {

    private ArrayList<TestHeaderModel> headerModelArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TestsStudentFragmentAdapter adapter;
    private DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private static final String TAG = "TestsStudentsFragment";

    public TestsStudentsFragment() {
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
        return inflater.inflate(R.layout.fragment_tests_students, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_students_tests);

        setAdapter();
        downloadData();
    }

    private void setAdapter() {
        adapter = new TestsStudentFragmentAdapter(getActivity(), headerModelArrayList, this);
        recyclerView.setAdapter(adapter);
    }

    private void downloadData(){
        //downloads the tests that are either participated or to be participated by the student currently logged in.
        reference.child(Constants.TYPE_STUDENTS).child(auth.getUid()).child("Tests").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> arrayList = new ArrayList<>();
                for(DataSnapshot s: snapshot.getChildren()){
                    arrayList.add(s.getKey());
                }
                //test ids are downloaded and the below function is called to download the data corresponding to the testId
                updateTestData(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());
            }
        });
    }

    private void updateTestData(ArrayList<String> testIdsArray){
        //for every testId the corresponding data is downloaded and the recycler view is updated.
        for(String testId: testIdsArray){
            reference.child("Tests").child(testId).child("Header").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        TestHeaderModel header= task.getResult().getValue(TestHeaderModel.class);
                        headerModelArrayList.add(header);
                        Log.e("Test Id", header.getTestId());
                        adapter.notifyItemInserted(headerModelArrayList.size()-1);
                    }
                }
            });
        }
    }

    @Override
    public void onTestItemClick(int position) {

        String testId = headerModelArrayList.get(position).getTestId();

        reference.child(Constants.TYPE_STUDENTS).child(auth.getUid()).child("Tests").child(testId).child("submitted").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    Log.e(TAG, "Test data of TestId: "+ testId +" is downloaded");
                    boolean isSubmitted = task.getResult().getValue(Boolean.class);
                    if(!isSubmitted){
                        fireAppropriateIntent(position);
                    }else{
                        ShowTestScoreFragment dialogFragment = new ShowTestScoreFragment(testId);
                        dialogFragment.show(getChildFragmentManager(), "Show Score");
                        Log.e(TAG, "Test is already Submitted");
                        Toast.makeText(getActivity(), "Test is already Submitted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }


    private void fireAppropriateIntent(int position) {
        TestHeaderModel header = headerModelArrayList.get(position);
        Intent i;

        int duration = Integer.parseInt(header.getDuration());
        String[] date = header.getDate().split("-");
        String[] time = header.getTime().split(":");

        TestCounterModel counter = new TestCounterModel(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]),
                Integer.parseInt(time[0]), Integer.parseInt(time[1]), duration);

        if(counter.isThereTimeUntilTest()){
            i = new Intent(getActivity(), TimeUntilTestActivity.class);
            i.putExtra("timeExceeded", false);
            i.putExtra("differenceInTime", counter.getDifferenceInTime());
            i.putExtra("header", header.toBundle());
            startActivity(i);
        }
        else if(counter.isItTimeForTest()){
            ShowInstructionsDialogFragment dialogFragment = new ShowInstructionsDialogFragment(header);
            dialogFragment.show(getChildFragmentManager(), "Show Instructions");
        }
        else if(counter.isTimeUp()){
            i = new Intent(getActivity(), TimeUntilTestActivity.class);
            i.putExtra("timeExceeded", true);
            i.putExtra("differenceInTime", counter.getDifferenceInTime());
            i.putExtra("header", header.toBundle());
            startActivity(i);
        }
        else{
            Toast.makeText(getActivity(), "You messed up the if conditions!", Toast.LENGTH_SHORT).show();
        }
    }
}
