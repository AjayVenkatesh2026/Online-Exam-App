package com.ajayvenkateshgunturu.onlineexam.DialogFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ajayvenkateshgunturu.onlineexam.Models.TestHeaderModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.ajayvenkateshgunturu.onlineexam.Students.ConductTestActivity;

public class ShowInstructionsDialogFragment extends DialogFragment {

    private TestHeaderModel header;
    private TextView cancelButton, acceptButton;

    public ShowInstructionsDialogFragment() {
        // Required empty public constructor
    }

    public ShowInstructionsDialogFragment(TestHeaderModel header) {
        this.header = header;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_instructions_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setListeners();
    }

    private void initViews(View v) {
        cancelButton = v.findViewById(R.id.text_view_instructions_cancel_button);
        acceptButton = v.findViewById(R.id.text_view_instructions_accept_button);
    }

    private void setListeners() {
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                Intent i = new Intent(getActivity(), ConductTestActivity.class);
                i.putExtra("header", header.toBundle());
                startActivity(i);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
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