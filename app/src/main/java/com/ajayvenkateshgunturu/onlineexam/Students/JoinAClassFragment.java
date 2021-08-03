package com.ajayvenkateshgunturu.onlineexam.Students;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class JoinAClassFragment extends DialogFragment {

    private TextInputEditText classId;
    private TextView addButton, cancelButton;
    private DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String type;

    public JoinAClassFragment() {
        // Required empty public constructor
    }

    public JoinAClassFragment(String type) {
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getDialog().setTitle("Join a class");
        return inflater.inflate(R.layout.fragment_join_a_class, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        setListeners();
    }

    private void setListeners() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strClassId = classId.getText().toString().trim();

                if(strClassId.isEmpty()){
                    Log.e("Message", "Empty Class id");
                }
                else{
                    if (strClassId.length() == 6) {
                        if(type == Constants.TYPE_STUDENTS){
                            Log.e("strClassId", strClassId);
                            reference.child(Constants.TYPE_STUDENTS).child(auth.getUid()).child("Classes").child(strClassId).setValue("true");
                            reference.child("Classes").child(strClassId).child(Constants.TYPE_STUDENTS).child(auth.getUid()).setValue(auth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.e("Message", "Joined the class");
                                    }
                                }
                            });
                        }else if(type == Constants.TYPE_TEACHERS){
                            Log.e("strClassId", strClassId);
                            reference.child(Constants.TYPE_TEACHERS).child(auth.getUid()).child("Classes").child(strClassId).setValue("true");
                            reference.child("Classes").child(strClassId).child(Constants.TYPE_TEACHERS).child(auth.getUid()).setValue(auth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.e("Message", "Joined the class");
                                    }
                                }
                            });
                        }

                    }else{
                        Log.e("Message", "Invalid Class Id");
                    }

                    getDialog().dismiss();
                }



//                String title = testTitle.getText().toString().trim();
//                String description = testDescription.getText().toString().trim();
//
//                if(title.isEmpty() || description.isEmpty()){
//                    Toast.makeText(getActivity().getBaseContext(), "Please provide title and description of the test", Toast.LENGTH_SHORT).show();
//                }else {
//                    TestQuestionModel testQuestionModel = new TestQuestionModel(title, description);
//
//                    addNewTestQuestionListener newTestQuestionListener = (addNewTestQuestionListener) getActivity();
//                    newTestQuestionListener.addNewQuestion(testQuestionModel);
//
//                    Toast.makeText(getActivity().getBaseContext(), "Add button clicked", Toast.LENGTH_SHORT).show();
//                }

            }
        });
    }

    private void initView(View view) {
        classId = view.findViewById(R.id.text_input_edit_text_join_class);

        addButton = view.findViewById(R.id.text_view_join_class_add_button);
        cancelButton = view.findViewById(R.id.text_view_join_class_cancel_button);
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }
}