package com.ajayvenkateshgunturu.onlineexam.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajayvenkateshgunturu.onlineexam.DialogFragments.PostTestDialogFragment;
import com.ajayvenkateshgunturu.onlineexam.Models.TestHeaderModel;
import com.ajayvenkateshgunturu.onlineexam.Models.TestQuestionModel;
import com.ajayvenkateshgunturu.onlineexam.R;
import com.ajayvenkateshgunturu.onlineexam.Teachers.ShowTestResultsActivity;

import java.util.ArrayList;

public class TestFragmentAdapter extends RecyclerView.Adapter<TestFragmentAdapter.TestViewHolder> {

    private ArrayList<TestHeaderModel> testsArrayList;
    private Context context;
    private FragmentManager manager;

    public TestFragmentAdapter(ArrayList<TestHeaderModel> testsArrayList, Context context, FragmentManager fragmentManager) {
        this.testsArrayList = testsArrayList;
        this.context = context;
        this.manager = fragmentManager;
    }

    public TestFragmentAdapter(ArrayList<TestHeaderModel> testsArrayList) {
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

        TextView testTitle, testDescription, testIcon, popupTextview;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);

            testTitle = itemView.findViewById(R.id.text_view_list_item_class_title);
            testDescription = itemView.findViewById(R.id.text_view_list_item_class_description);
            testIcon = itemView.findViewById(R.id.text_view_list_item_class_icon);
            popupTextview = itemView.findViewById(R.id.text_view_list_item_popup_menu);
        }

        public void bind(int position){
            TestHeaderModel headerModel = testsArrayList.get(position);
            testTitle.setText(headerModel.getTitle());
            testDescription.setText(headerModel.getDescription());
            testIcon.setText(headerModel.getTitle().substring(0, 1).toUpperCase());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ShowTestResultsActivity.class);
                    i.putExtra("testId", headerModel.getTestId());
                    context.startActivity(i);
                }
            });

            popupTextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, popupTextview);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.popup_menu_tests_teachers);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_item_post_test:
                                    Toast.makeText(context, "Post test Clicked", Toast.LENGTH_SHORT).show();
                                    postTest(position);
                                    return true;
                                case R.id.menu_item_delete_test:
                                    Toast.makeText(context, "Delete test Clicked", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
        }
    }

    private void postTest(int position) {
        PostTestDialogFragment dialogFragment = new PostTestDialogFragment(testsArrayList.get(position));
        dialogFragment.show(manager, "TAG");
    }
}
