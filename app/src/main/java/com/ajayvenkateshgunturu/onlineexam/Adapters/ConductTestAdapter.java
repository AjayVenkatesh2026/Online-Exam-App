package com.ajayvenkateshgunturu.onlineexam.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajayvenkateshgunturu.onlineexam.Models.TestHeaderModel;
import com.ajayvenkateshgunturu.onlineexam.Models.TestQuestionModel;
import com.ajayvenkateshgunturu.onlineexam.R;

import java.util.ArrayList;

public class ConductTestAdapter extends RecyclerView.Adapter<ConductTestAdapter.TestQuestionViewHolder> {

    private Context mContext;
    private ArrayList<TestQuestionModel> questions;

    public ConductTestAdapter(Context mContext, ArrayList<TestQuestionModel> questions) {
        this.mContext = mContext;
        this.questions = questions;
    }

    @NonNull
    @Override
    public TestQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_test_question_four_options, parent, false);
        return new TestQuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TestQuestionViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class TestQuestionViewHolder extends RecyclerView.ViewHolder{

        TextView question;
        RadioButton optionOne, optionTwo, optionThree, optionFour;
        RadioGroup radioGroup;

        public TestQuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.text_view_list_item_test_question);
            optionOne = itemView.findViewById(R.id.radio_button_option_one);
            optionTwo = itemView.findViewById(R.id.radio_button_option_two);
            optionThree = itemView.findViewById(R.id.radio_button_option_three);
            optionFour = itemView.findViewById(R.id.radio_button_option_four);
            radioGroup = itemView.findViewById(R.id.radio_group_test_options);
        }

        public void bind(int position){
            TestQuestionModel questionModel = questions.get(position);

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


            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int ans = 0;
                    switch (checkedId){
                        case R.id.radio_button_option_one:
                            ans = 1;
                            break;
                        case R.id.radio_button_option_two:
                            ans = 2;
                            break;
                        case R.id.radio_button_option_three:
                            ans = 3;
                            break;
                        case R.id.radio_button_option_four:
                            ans = 4;
                            break;
                        default:
                            ans = 0;
                    }
                    Log.e("Position " + position, "Ans: " + ans);
                }
            });
        }
    }
}
