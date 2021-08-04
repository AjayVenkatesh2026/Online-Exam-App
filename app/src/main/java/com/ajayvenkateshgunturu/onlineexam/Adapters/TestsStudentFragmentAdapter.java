package com.ajayvenkateshgunturu.onlineexam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ajayvenkateshgunturu.onlineexam.Models.TestHeaderModel;
import com.ajayvenkateshgunturu.onlineexam.R;

import java.util.ArrayList;

public class TestsStudentFragmentAdapter extends RecyclerView.Adapter<TestsStudentFragmentAdapter.StudentTestViewHolder>{

    private Context mContext;
    private ArrayList<TestHeaderModel> testsArrayList;
    private OnTestItemClickListener listener;

    public interface OnTestItemClickListener{
        void onTestItemClick(int position);
    }

    public TestsStudentFragmentAdapter(Context mContext, ArrayList<TestHeaderModel> testsArrayList) {
        this.mContext = mContext;
        this.testsArrayList = testsArrayList;
    }

    public TestsStudentFragmentAdapter(Context mContext, ArrayList<TestHeaderModel> testsArrayList, OnTestItemClickListener listener) {
        this.mContext = mContext;
        this.testsArrayList = testsArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentTestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_classes_layout, parent, false);
        return new StudentTestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentTestViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return testsArrayList.size();
    }

    class StudentTestViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, menuIcon, profileIcon;

        public StudentTestViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.text_view_list_item_class_title);
            description = itemView.findViewById(R.id.text_view_list_item_class_description);
            menuIcon = itemView.findViewById(R.id.text_view_list_item_class_icon);
            profileIcon = itemView.findViewById(R.id.text_view_list_item_popup_menu);

            profileIcon.setPadding(0,0,0,0);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) profileIcon.getLayoutParams();
            params.height = 0;
            params.width = 0;
            profileIcon.setLayoutParams(params);


        }

        public void bind(int position){

            TestHeaderModel headerModel = testsArrayList.get(position);
            title.setText(headerModel.getTitle());
            description.setText(headerModel.getDescription());
            menuIcon.setText(headerModel.getTitle().substring(0, 1).toUpperCase());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onTestItemClick(position);
                    }
                }
            });

        }
    }
}
