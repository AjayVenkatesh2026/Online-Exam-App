package com.ajayvenkateshgunturu.onlineexam.DialogFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private String userType, changeType;
    private DataChangedListener listener;
    private TextView cancelButton, saveButton, title;
    private EditText input;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    private static final String TAG = "BottomSheetDialog";

    public CustomBottomSheetDialogFragment() {
        // Required empty public constructor
    }

    public CustomBottomSheetDialogFragment(String userType, String changeType) {
        this.userType = userType;
        this.changeType = changeType;
    }

    public CustomBottomSheetDialogFragment(String userType, String changeType, DataChangedListener listener) {
        this.userType = userType;
        this.changeType = changeType;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_custom_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initViews(view);
        setListeners();

        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews(View view) {
        cancelButton = view.findViewById(R.id.text_view_cancel_button);
        saveButton = view.findViewById(R.id.text_view_save_button);
        title = view.findViewById(R.id.text_view_dialog_title);

        if(changeType.equals("name")){
            title.setText("Enter new Name");
        }else if(changeType.equals("mail")){
            title.setText("Enter new Email");
        }

        input = view.findViewById(R.id.edit_text_dialog_input);
        input.requestFocus();
    }

    private void setListeners() {

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString().trim();
                if(text.isEmpty()){
                    Toast.makeText(getContext(), "Empty Fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(changeType.equals("name")){
                    reference.child(userType).child(auth.getUid()).child("name").setValue(text).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getContext(), "Name changed", Toast.LENGTH_SHORT).show();
                            }else if(task.isCanceled()){
                                Toast.makeText(getContext(), "Failed to change the name.", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, task.getResult().toString());
                            }
                            getDialog().dismiss();
                            if(listener!=null){
                                listener.onDataChanged();
                            }
                        }
                    });
                }else if(changeType.equals("mail")){
                    Toast.makeText(getContext(), "Feature not added yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

