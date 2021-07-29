package com.ajayvenkateshgunturu.onlineexam.Teachers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
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
import java.util.Objects;

public class ClassesTeachersFragment extends Fragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance(Constants.FIREBASE_URL).getReference();
    private RecyclerView classesRecyclerView;
    private ArrayList<ClassModel> array = new ArrayList<>();
    private ClassesAdapter adapter;

    public ClassesTeachersFragment() {
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
        return inflater.inflate(R.layout.fragment_classes_teachers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        classesRecyclerView = view.findViewById(R.id.recycler_view_teachers_classes);
        setRecyclerAdapter();
        downloadData();
    }

    private void downloadData() {

        //Function to download the data of classes created by the current user(teacher)
        //function call to retrieveClassIds to get the classIds created by the current user from firebase

        retrieveClassIds(new getClassIdsListener() {
            @Override
            public void setClassIds(ArrayList<Integer> arrayList) {

                //arrayList contains the list of classIds that the user created
                //Following retrieveData call is to get the data of the respective classes of each classId in the arraylist

                retrieveData(new retrieveClasses() {
                    @Override
                    public void retrieveClass(ClassModel classModel) {

                        //after a class' data is retrieved, add it to the array and call notifyDataSetChanged() to update the elements in recyclerview.
                        array.add(classModel);
                        adapter.notifyDataSetChanged();
                    }
                }, arrayList);
            }
        });
    }

    private void setRecyclerAdapter() {
        adapter = new ClassesAdapter(getContext(), array);
        classesRecyclerView.setAdapter(adapter);
    }

    private void retrieveClassIds(getClassIdsListener listener) {
        String id = auth.getUid();
        reference.child("Teachers").child(id).child("Classes").addValueEventListener(new ValueEventListener() {
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
                listener.setClassIds(arr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", error.getMessage());
            }
        });
    }

    private void retrieveData(retrieveClasses listener, ArrayList<Integer> classIds) {

        //ArrayList<ClassModel> arrayList = new ArrayList<>();
        for(int classId: classIds){
            reference.child("Classes").child(String.valueOf(classId)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ClassModel classModel = snapshot.getValue(ClassModel.class);
                        listener.retrieveClass(classModel);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Log.e("Error Occurred", error.getMessage());
                }
            });
        }
    }
}

interface retrieveClasses {
    void retrieveClass(ClassModel classModel);
}

interface getClassIdsListener {
    void setClassIds(ArrayList<Integer> arrayList);
}