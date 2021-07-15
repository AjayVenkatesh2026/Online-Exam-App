package com.ajayvenkateshgunturu.onlineexam.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajayvenkateshgunturu.onlineexam.Models.TestQuestionModel;
import com.ajayvenkateshgunturu.onlineexam.R;

import java.util.ArrayList;

public class TestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TestQuestionModel> arrayList;

    public TestsAdapter(ArrayList<TestQuestionModel> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder v;
        if(viewType == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_test_header,parent, false);
            v = new TestHeaderViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_test_question_four_options,parent, false);
            v = new TestQuestionViewHolder(view);
        }
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 0:
                TestHeaderViewHolder v0 = (TestHeaderViewHolder) holder;
                v0.bind(position);
                break;
            case 1:
                TestQuestionViewHolder v = (TestQuestionViewHolder) holder;
                v.bind(position);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }else{
            return 1;
        }
    }

    class TestHeaderViewHolder extends RecyclerView.ViewHolder{

        private TextView title, description;

        public TestHeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.text_view_list_item_test_title);
            description = itemView.findViewById(R.id.text_view_list_item_test_description);
        }

        void bind(int position){
            TestQuestionModel questionModel = arrayList.get(position);

            title.setText(questionModel.getTitle());
            description.setText(questionModel.getDescription());
        }
    }

    class TestQuestionViewHolder extends RecyclerView.ViewHolder{

        private TextView question;
        private RadioButton optionOne, optionTwo, optionThree, optionFour;

        public TestQuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.text_view_list_item_test_question);
            optionOne = itemView.findViewById(R.id.radio_button_option_one);
            optionTwo = itemView.findViewById(R.id.radio_button_option_two);
            optionThree = itemView.findViewById(R.id.radio_button_option_three);
            optionFour = itemView.findViewById(R.id.radio_button_option_four);
        }

        void bind(int position){

            TestQuestionModel questionModel = arrayList.get(position);

            question.setText(questionModel.getQuestion());
            optionOne.setText(questionModel.getOptionOne());
            optionTwo.setText(questionModel.getOptionTwo());

            if(!questionModel.getOptionThree().isEmpty()){
                optionThree.setText(questionModel.getOptionThree());
            }else{
                optionThree.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }

            if(!questionModel.getOptionFour().isEmpty()){
                optionFour.setText(questionModel.getOptionFour());
            }else{
                optionFour.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
        }

    }
}
