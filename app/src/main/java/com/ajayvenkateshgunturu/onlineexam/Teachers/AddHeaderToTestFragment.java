package com.ajayvenkateshgunturu.onlineexam.Teachers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.Models.TestQuestionModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddHeaderToTestFragment extends DialogFragment {

    private TextInputEditText testTitle, testDescription;
    private TextView addButton, cancelButton;

    public AddHeaderToTestFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_add_header_to_test, container, false);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        setListeners();
    }

    private void setListeners() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = testTitle.getText().toString().trim();
                String description = testDescription.getText().toString().trim();

                if(title.isEmpty() || description.isEmpty()){
                    Toast.makeText(getActivity().getBaseContext(), "Please provide title and description of the test", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    getDialog().dismiss();
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = testTitle.getText().toString().trim();
                String description = testDescription.getText().toString().trim();

                if(title.isEmpty() || description.isEmpty()){
                    Toast.makeText(getActivity().getBaseContext(), "Please provide title and description of the test", Toast.LENGTH_SHORT).show();
                }else {
                    TestQuestionModel testQuestionModel = new TestQuestionModel(title, description);

                    addNewTestQuestionListener newTestQuestionListener = (addNewTestQuestionListener) getActivity();
                    newTestQuestionListener.addNewQuestion(testQuestionModel);

                    Toast.makeText(getActivity().getBaseContext(), "Add button clicked", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
            }
        });
    }

    private void initViews(View view) {
        testTitle = view.findViewById(R.id.text_input_edit_text_test_title);
        testDescription = view.findViewById(R.id.text_input_edit_text_test_description);

        addButton = view.findViewById(R.id.text_view_test_add_button);
        cancelButton = view.findViewById(R.id.text_view_test_cancel_button);
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }
}