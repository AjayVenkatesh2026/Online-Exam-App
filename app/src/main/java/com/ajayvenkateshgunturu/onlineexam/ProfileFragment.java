package com.ajayvenkateshgunturu.onlineexam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class ProfileFragment extends Fragment {

    private String UserType;
    private ImageView profilePic, changeProfileButton;
    private TextInputLayout userNameLayout, userEmailLayout;
    private EditText userNameEditText, userEmailEditText;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static final String TAG = "ProfileFragment";

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

        getUserData();
    }

    public void getUserData() {
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
        reference.child(UserType).child(auth.getUid()).child("ProfilePic").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    String profileUrl = task.getResult().getValue(String.class);
                    Picasso.get().load(profileUrl).into(profilePic);
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
                        getUserData();
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
                        getUserData();
                    }
                });
                dialogFragment.show(getChildFragmentManager(), "change name dialog");
            }
        };

        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });

        userNameLayout.setOnClickListener(nameClickListener);
        userNameEditText.setOnClickListener(nameClickListener);

        userEmailLayout.setOnClickListener(mailClickListener);
        userEmailEditText.setOnClickListener(mailClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            Uri uri = data.getData();
            Log.e(TAG, uri.toString());
            changeProfilePicture(uri);
        }

    }

    private void changeProfilePicture(Uri uri) {
        profilePic.setImageURI(uri);
        String randomId = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child("images").child(randomId + ".jpg");
        ref.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(), "File uploaded Successfully", Toast.LENGTH_SHORT).show();

                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Uri downloadUri = task.getResult();
                                changeProfileUri(downloadUri);
                            }
                        }
                    });

                }else if(task.isCanceled()){
                    Log.e(TAG, task.getResult().getError().getMessage());
                }
            }
        });
    }

    private void changeProfileUri(Uri downloadUri) {

        reference.child(UserType).child(auth.getUid()).child("ProfilePic").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Profile pic changed successfully.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}