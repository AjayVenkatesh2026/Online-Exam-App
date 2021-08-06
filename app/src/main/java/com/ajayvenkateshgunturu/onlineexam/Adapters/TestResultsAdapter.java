package com.ajayvenkateshgunturu.onlineexam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajayvenkateshgunturu.onlineexam.Models.TestResultModel;
import com.ajayvenkateshgunturu.onlineexam.R;

import java.util.ArrayList;

public class TestResultsAdapter extends RecyclerView.Adapter<TestResultsAdapter.ResultViewHolder> {

    private Context context;
    private ArrayList<TestResultModel> results;

    public TestResultsAdapter(Context context, ArrayList<TestResultModel> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_test_result, parent, false);
        return new ResultViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder{

        TextView mailTextView, scoreTextView;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            mailTextView = itemView.findViewById(R.id.text_view_list_item_student_email);
            scoreTextView = itemView.findViewById(R.id.text_view_list_item_student_score);
        }

        public void bind(int position){
            TestResultModel result = results.get(position);
            mailTextView.setText(result.getMail());
            scoreTextView.setText("Score: " + result.getScore() + " / " + result.getNoOfQuestions());
        }
    }
}
