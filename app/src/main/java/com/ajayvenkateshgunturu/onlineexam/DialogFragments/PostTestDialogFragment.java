package com.ajayvenkateshgunturu.onlineexam.DialogFragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.Models.TestHeaderModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class PostTestDialogFragment extends DialogFragment {

    private TextView postButton, cancelButton, title;
    private TextView dateTextView, timeTextView;
    private TestHeaderModel headerModel;
    private TextInputEditText testDurationEditText, classIdsEditText;
    private DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public PostTestDialogFragment() {
        // Required empty public constructor
    }

    public PostTestDialogFragment(TestHeaderModel headerModel) {
        this.headerModel = headerModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_test_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        setListeners();
    }

    private void initViews(View v) {
        title = v.findViewById(R.id.text_view_post_test_title);
        postButton = v.findViewById(R.id.text_view_post_test_post_button);
        cancelButton = v.findViewById(R.id.text_view_post_test_cancel_button);
        dateTextView = v.findViewById(R.id.text_view_post_test_date_button);
        timeTextView = v.findViewById(R.id.text_view_post_test_time_button);

        testDurationEditText = v.findViewById(R.id.text_input_edit_text_test_duration);
        classIdsEditText = v.findViewById(R.id.text_input_edit_text_post_test_classes);
        title.setText(headerModel.getTitle());
    }

    private void setListeners(){
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment(new DatePickerFragment.OnDateSetListener() {
                    @Override
                    public void setDate(int year, int month, int day) {
                        Calendar c = Calendar.getInstance();
                        if((year >= c.get(Calendar.YEAR)) ||
                                (year == c.get(Calendar.YEAR) && month > c.get(Calendar.MONTH)) ||
                                (year == c.get(Calendar.YEAR) && month == c.get(Calendar.MONTH) && day >= c.get(Calendar.DATE))){

                            String date = day + "-" + month + "-" + year;
                            headerModel.setDate(date);
                            dateTextView.setText(date);
                        }
                        else {
                            Toast.makeText(getActivity(), "Invalid Date", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                datePickerFragment.show(getChildFragmentManager(), "DatePicker");
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment(new TimePickerFragment.TimeSetListener() {
                    @Override
                    public void setTime(int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;
                        timeTextView.setText(time);
                        headerModel.setTime(time);
                        Log.e("time", time);
                    }
                });
                timePickerFragment.show(getChildFragmentManager(), "TimePicker");
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] array = classIdsEditText.getText().toString().split(",");

                for(int i = 0; i < array.length; i++){
                    array[i] =  array[i].trim();
                }

                if(array.length == 0){
                    Log.e("Error", "Empty fields");
                    Toast.makeText(getActivity(), "Empty Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(testDurationEditText.getText().toString().isEmpty() || headerModel.getDate() == null || headerModel.getTime() == null){
                    Log.e("Error", "Empty fields");
                    Toast.makeText(getActivity(), "Empty Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                reference.child("Teachers").child(auth.getUid()).child("Classes").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> arrayList = new ArrayList<>();

                        for(DataSnapshot s: snapshot.getChildren()){
                            arrayList.add(s.getKey());
                        }

                        for(String s: array){
                            if(!arrayList.contains(s)){
                                Log.e("Class Id", "not valid");
                                Toast.makeText(getActivity(), "Class Id is either invalid or you have not joined the class", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        updateHeader(array);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Database Error", error.getMessage());
                    }
                });
            }
        });
    }

    private void updateHeader(String[] array){

        headerModel.setDuration(testDurationEditText.getText().toString());

        reference.child("Tests").child(headerModel.getTestId()).child("Header").setValue(headerModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.e("Test Information: ", "Updated");
                updatePostClasses(array);
            }
        });
    }

    private void updatePostClasses(String[] array){

        for(int i = 0; i < array.length; i++){
            int finalI = i;
            reference.child("Tests").child(headerModel.getTestId()).child("Header").child("Classes")
                    .child(array[i].toString()).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("Posted on classes", array[finalI]);
                    if(finalI == array.length-1){
                        getDialog().dismiss();
                        updateTestDetailsInClasses(array);
                    }
                }
            });
        }
    }

    private void updateTestDetailsInClasses(String[] arr){
        for(String classId: arr){
            reference.child("Classes").child(classId).child("Tests").child(headerModel.getTestId()).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    getStudentsForClass(classId);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Update Test Failed", classId);
                    //updateTestDetailsInClasses(arr);
                }
            });
        }
    }

    private void getStudentsForClass(String classId) {
        reference.child("Classes").child(classId).child("Students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> studentsArrayList = new ArrayList<>();
                for(DataSnapshot s: snapshot.getChildren()){
                    studentsArrayList.add(s.getKey());
                }
                updateTestDetailsForStudents(studentsArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Error", error.getMessage());
                //getStudentsForClass(classId);
            }
        });
    }

    private void updateTestDetailsForStudents(ArrayList<String> studentsArray) {
        for(String student: studentsArray){
            reference.child(Constants.TYPE_STUDENTS).child(student).child("Tests").child(headerModel.getTestId()).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("Updated Test for", student);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Update Test Failed", student);
                    //updateTestDetailsForStudents(studentsArray);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }


    //Below are the fragments for date picker and time pickers
    //We are implementing the function callback using custom interface listeners

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private OnDateSetListener listener;

        public interface OnDateSetListener {
            void setDate(int year, int month, int day);
        }

        public DatePickerFragment() {
        }

        public DatePickerFragment(int contentLayoutId) {
            super(contentLayoutId);
        }

        public DatePickerFragment(OnDateSetListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            listener.setDate(year, month, day);
        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        //custom interface
        public interface TimeSetListener{
            void setTime(int hourOfDay, int minute);
        }

        private TimeSetListener listener;

        //constructors
        public TimePickerFragment(TimeSetListener listener) {
            this.listener = listener;
        }

        public TimePickerFragment() {
        }


        //create new timepicker dialog
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, min, true);
        }

        //implement on date changed listener
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            listener.setTime(hourOfDay, minute);
        }
    }

}