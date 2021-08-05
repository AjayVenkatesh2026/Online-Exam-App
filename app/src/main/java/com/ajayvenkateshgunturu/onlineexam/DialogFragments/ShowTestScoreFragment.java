package com.ajayvenkateshgunturu.onlineexam.DialogFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.ajayvenkateshgunturu.onlineexam.Students.StudentsMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowTestScoreFragment extends DialogFragment {

    private DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextView cancelButton, okButton, scoreTextView;
    private String testId;
    private static final String TAG = "ShowTestScoreFragment";
    private int score, noOfQuestions;
    private boolean isActivityDestroyed;

    public ShowTestScoreFragment() {
        // Required empty public constructor
    }

    //change the constructor by adding another boolean variable to indicate from where the fragment is called.

    public ShowTestScoreFragment(String testId) {
        this.testId = testId;
    }

    public ShowTestScoreFragment(int score, int noOfQuestions, boolean isActivityDestroyed) {
        this.isActivityDestroyed = isActivityDestroyed;
        this.score = score;
        this.noOfQuestions = noOfQuestions;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_test_score, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scoreTextView = view.findViewById(R.id.text_view_student_test_score);
        cancelButton = view.findViewById(R.id.text_view_score_cancel_button);
        okButton = view.findViewById(R.id.text_view_score_accept_button);

        setListeners();

        if(testId != null)
            getStudentTestScore();
        else{
            String s = score + " / " + noOfQuestions;
            scoreTextView.setText("Score: " + s);
        }

    }

    private void getStudentTestScore() {
        reference.child(Constants.TYPE_STUDENTS).child(auth.getUid()).child("Tests").child(testId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    int score = snapshot.child("score").getValue(Integer.class);
                    int noOfQuestions = snapshot.child("noOfQuestions").getValue(Integer.class);

                    String s = score + " / " + noOfQuestions;
                    scoreTextView.setText("Score: " + s);
                    //Bundle b = snapshot.getValue(Bundle.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "DatabaseError " + error.getMessage());
            }
        });
    }

    private void setListeners() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();

                if(testId == null)
                    startActivity(new Intent(getActivity(), StudentsMainActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}