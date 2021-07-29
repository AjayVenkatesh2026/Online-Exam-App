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

    ArrayList<String> arrayList;

    public ClassItemAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public classItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_class_item, parent,false);
        return new classItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull classItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class classItemViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public classItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_view_list_item_class_id);
        }

        public void bind(int position){
            textView.setText(arrayList.get(position));
        }
    }
}
