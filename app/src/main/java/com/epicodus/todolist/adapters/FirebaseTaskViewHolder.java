package com.epicodus.todolist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.epicodus.todolist.R;
import com.epicodus.todolist.models.Task;

public class FirebaseTaskViewHolder extends RecyclerView.ViewHolder {
    public TextView mTaskDescription;
    public CheckBox mCheckBox;
    View mView;
    Context mContext;

    public FirebaseTaskViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    public void bindTask(Task task) {
        mCheckBox = (CheckBox) mView.findViewById(R.id.checkBox);
        mTaskDescription = (TextView) mView.findViewById(R.id.taskDescription);
        mTaskDescription.setText(task.getDescription());
        mCheckBox.setChecked(task.isComplete());
        if (task.isComplete()) {
            mTaskDescription.setTextColor(Color.parseColor("#A9A9A9"));
        }
    }
}
