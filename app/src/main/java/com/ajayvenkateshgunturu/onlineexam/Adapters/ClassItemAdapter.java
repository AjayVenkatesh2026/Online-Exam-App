package com.ajayvenkateshgunturu.onlineexam.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajayvenkateshgunturu.onlineexam.R;

import java.util.ArrayList;

public class ClassItemAdapter extends RecyclerView.Adapter<ClassItemAdapter.classItemViewHolder> {

    ArrayList<String> teachersArrayList;
    ArrayList<String> studentsArrayList;
    int teachersArraySize;

    public ClassItemAdapter(ArrayList<String> teachersArrayList, ArrayList<String> studentsArrayList) {
        this.teachersArrayList = teachersArrayList;
        this.studentsArrayList = studentsArrayList;
        this.teachersArraySize = teachersArrayList.size();
    }

    public ClassItemAdapter(ArrayList<String> arrayList) {
        this.teachersArrayList = arrayList;
    }

    public void setTeachersArrayList(ArrayList<String> teachersArrayList) {
        this.teachersArrayList = teachersArrayList;
        this.teachersArraySize = teachersArrayList.size();
    }

    public void setStudentsArrayList(ArrayList<String> studentsArrayList) {
        this.studentsArrayList = studentsArrayList;
    }

    @NonNull
    @Override
    public classItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == 0){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_class_item_title, parent,false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_class_item, parent,false);
        }
        return new classItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull classItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return teachersArraySize+ studentsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 || position == teachersArraySize){
            return 0;
        }
        return 1;
    }

    class classItemViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public classItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view_list_item_class_id);
        }

        public void bind(int position){
            String text;
            if(position < teachersArraySize){
                text = teachersArrayList.get(position);
            }else{
                text = studentsArrayList.get(position-teachersArraySize);
            }
            textView.setText(text);
        }
    }
}
