package com.ajayvenkateshgunturu.onlineexam.Students;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ajayvenkateshgunturu.onlineexam.Adapters.ClassesAdapter;
import com.ajayvenkateshgunturu.onlineexam.Constants;
import com.ajayvenkateshgunturu.onlineexam.Models.ClassModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ClassesStudentsFragment extends Fragment implements joinNewClassListener{

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    private RecyclerView recyclerView;
    private ClassesAdapter adapter;
    private ArrayList<ClassModel> array = new ArrayList<>();

    public ClassesStudentsFragment() {
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
        return inflater.inflate(R.layout.fragment_classes_students, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        setAdapter();

        retrieveClassIds();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_students_classes);
    }

    private void setAdapter() {
        adapter = new ClassesAdapter(getContext(), array);
        recyclerView.setAdapter(adapter);
    }

    private void retrieveClassIds() {
        String id = auth.getUid();
        reference.child("Students").child(id).child("Classes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Integer> arr = new ArrayList<>();
                for(DataSnapshot s: snapshot.getChildren()){
                    if(s.exists()){
                        String key = s.getKey();
                        arr.add(Integer.parseInt(key));
                    }else{
                        Log.e("snapshot", "doesn't exist");
                    }
                }
                array.clear();
                adapter.notifyDataSetChanged();
                retrieveData(new getStudentClassesListener() {
                    @Override
                    public void retrieveClasses(ClassModel classModel) {

                        //after a class' data is retrieved, add it to the array and call notifyDataSetChanged() to update the elements in recyclerview.
                        array.add(classModel);
                        adapter.notifyItemInserted(array.size()-1);
                    }
                }, arr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private void retrieveData(getStudentClassesListener listener, ArrayList<Integer> classIds) {

        //ArrayList<ClassModel> arrayList = new ArrayList<>();
        for(int classId: classIds){
            reference.child("Classes").child(String.valueOf(classId)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ClassModel classModel = snapshot.getValue(ClassModel.class);
                        listener.retrieveClasses(classModel);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Log.e("Error Occurred", error.getMessage());
                }
            });
        }
    }

    @Override
    public void setClassId(int classId) {

    }
}

interface joinNewClassListener{
    void setClassId(int classId);
}

interface getStudentClassIdsListener{
    void setClassIds(ArrayList<Integer> arrayList);
}

interface getStudentClassesListener{
    void retrieveClasses(ClassModel classModel);
}