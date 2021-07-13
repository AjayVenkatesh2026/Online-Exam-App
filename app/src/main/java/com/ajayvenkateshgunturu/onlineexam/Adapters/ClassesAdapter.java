package com.ajayvenkateshgunturu.onlineexam.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajayvenkateshgunturu.onlineexam.Models.ClassModel;
import com.ajayvenkateshgunturu.onlineexam.R;

import java.util.ArrayList;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.classViewHolder> {

    ArrayList<ClassModel> arrayList;

    public ClassesAdapter(ArrayList<ClassModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public classViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_classes_layout, parent, false);
        return new classViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull classViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class classViewHolder extends RecyclerView.ViewHolder{

        TextView classTitle, classDescription, classIcon;

        public classViewHolder(@NonNull  View itemView) {
            super(itemView);

            classTitle = itemView.findViewById(R.id.text_view_list_item_class_title);
            classDescription = itemView.findViewById(R.id.text_view_list_item_class_description);
            classIcon = itemView.findViewById(R.id.text_view_list_item_class_icon);
        }

        public void bind(int position){
            ClassModel currentClass = arrayList.get(position);
            classIcon.setText(currentClass.getClassName().substring(0, 1).toUpperCase());
            classTitle.setText(currentClass.getClassName());
            classDescription.setText(currentClass.getClassDescription());
        }
    }
}
