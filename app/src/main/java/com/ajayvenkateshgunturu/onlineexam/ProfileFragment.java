package com.ajayvenkateshgunturu.onlineexam;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.DialogFragments.CustomBottomSheetDialogFragment;
import com.ajayvenkateshgunturu.onlineexam.DialogFragments.DataChangedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileFragment extends Fragment {

    private String UserType;
    private ImageView profilePic, changeProfileButton;
    private TextInputLayout userNameLayout, userEmailLayout;
    private EditText userNameEditText, userEmailEditText;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();

    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(String userType) {
        UserType = userType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setListeners();

        int width = getActivity().getWindow().getDecorView().getWidth();
    }

    private void initViews(View view) {
        profilePic = view.findViewById(R.id.image_view_profile_pic);
        changeProfileButton = view.findViewById(R.id.image_view_change_profile);

        userNameLayout = view.findViewById(R.id.text_input_layout_user_name);
        userEmailLayout = view.findViewById(R.id.text_input_layout_user_mail);

        userNameEditText = view.findViewById(R.id.edit_text_user_name);
        userEmailEditText = view.findViewById(R.id.edit_text_user_email);

        getUserNameAndEmail();
    }

    public void getUserNameAndEmail() {
        reference.child(UserType).child(auth.getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String name = task.getResult().getValue(String.class);
                    userNameEditText.setText(name);
                }
            }
        });
        reference.child(UserType).child(auth.getUid()).child("email").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String mail = task.getResult().getValue(String.class);
                    userEmailEditText.setText(mail);
                }
            }
        });
    }

    private void setListeners() {

        View.OnClickListener nameClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomSheetDialogFragment dialogFragment= new CustomBottomSheetDialogFragment(UserType, "name", new DataChangedListener() {
                    @Override
                    public void onDataChanged() {
                        getUserNameAndEmail();
                    }
                });
                dialogFragment.show(getChildFragmentManager(), "change name dialog");
            }
        };

        View.OnClickListener mailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBottomSheetDialogFragment dialogFragment= new CustomBottomSheetDialogFragment(UserType, "mail", new DataChangedListener() {
                    @Override
                    public void onDataChanged() {
                        getUserNameAndEmail();
                    }
                });
                dialogFragment.show(getChildFragmentManager(), "change name dialog");
            }
        };

        userNameLayout.setOnClickListener(nameClickListener);
        userNameEditText.setOnClickListener(nameClickListener);

        userEmailLayout.setOnClickListener(mailClickListener);
        userEmailEditText.setOnClickListener(mailClickListener);
    }
}