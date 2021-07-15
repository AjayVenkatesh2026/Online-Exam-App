package com.ajayvenkateshgunturu.onlineexam.Teachers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.Models.TestQuestionModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddQuestionToTestFragment extends DialogFragment {

    private TextInputEditText question, optionOne, optionTwo, optionThree, optionFour;
    private TextView cancelButton, addButton;

    public AddQuestionToTestFragment() {
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
        return inflater.inflate(R.layout.fragment_add_question_to_test, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

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

                String q = question.getText().toString().trim();
                String OptionOne = optionOne.getText().toString().trim();
                String OptionTwo = optionTwo.getText().toString().trim();
                String OptionThree = optionThree.getText().toString().trim();
                String OptionFour = optionFour.getText().toString().trim();

                TestQuestionModel questionModel;

                if(q.isEmpty() || OptionOne.isEmpty() || OptionTwo.isEmpty()){
                    Toast.makeText(getActivity().getBaseContext(),"Empty Fields!", Toast.LENGTH_SHORT).show();
                }else{
                    questionModel = new TestQuestionModel(q, OptionOne, OptionTwo, OptionThree, OptionFour);

                    addNewTestQuestionListener newTestQuestionListener = (addNewTestQuestionListener) getActivity();
                    newTestQuestionListener.addNewQuestion(questionModel);

                    Toast.makeText(getActivity().getBaseContext(),"Question Added!", Toast.LENGTH_SHORT).show();

                    getDialog().dismiss();
                }
            }
        });

    }

    private void initViews(View view) {

        question = view.findViewById(R.id.text_input_edit_text_editable_test_question);
        optionOne = view.findViewById(R.id.text_input_edit_text_editable_test_option_one);
        optionTwo = view.findViewById(R.id.text_input_edit_text_editable_test_option_two);
        optionThree = view.findViewById(R.id.text_input_edit_text_editable_test_option_three);
        optionFour = view.findViewById(R.id.text_input_edit_text_editable_test_option_four);

        cancelButton = view.findViewById(R.id.text_view_test_cancel_button);
        addButton = view.findViewById(R.id.text_view_test_add_button);
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }
}