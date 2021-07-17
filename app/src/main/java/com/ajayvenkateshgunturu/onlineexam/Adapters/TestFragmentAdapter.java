package com.ajayvenkateshgunturu.onlineexam.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajayvenkateshgunturu.onlineexam.Models.TestQuestionModel;
import com.ajayvenkateshgunturu.onlineexam.R;

import java.util.ArrayList;

public class TestFragmentAdapter extends RecyclerView.Adapter<TestFragmentAdapter.TestViewHolder> {

    private ArrayList<TestQuestionModel> testsArrayList;

    public TestFragmentAdapter(ArrayList<TestQuestionModel> testsArrayList) {
        this.testsArrayList = testsArrayList;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_classes_layout, parent, false);
        return new TestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TestFragmentAdapter.TestViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return testsArrayList.size();
    }

    class TestViewHolder extends RecyclerView.ViewHolder{

        TextView testTitle, testDescription, testIcon;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);

            testTitle = itemView.findViewById(R.id.text_view_list_item_class_title);
            testDescription = itemView.findViewById(R.id.text_view_list_item_class_description);
            testIcon = itemView.findViewById(R.id.text_view_list_item_class_icon);
        }

        public void bind(int position){
            TestQuestionModel testQuestionModel = testsArrayList.get(position);
            testTitle.setText(testQuestionModel.getTitle());
            testDescription.setText(testQuestionModel.getDescription());
            testIcon.setText(testQuestionModel.getTitle().substring(0, 1).toUpperCase());
        }
    }
}
