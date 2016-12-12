package com.epicodus.todolist.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.todolist.R;
import com.epicodus.todolist.adapters.FirebaseTaskViewHolder;
import com.epicodus.todolist.models.Task;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.newTaskEditText) EditText mNewTask;
    @Bind(R.id.addButton) Button mAddButton;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private DatabaseReference mTaskReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAddButton.setOnClickListener(this);

        mTaskReference = FirebaseDatabase.getInstance().getReference("tasks");
        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Task, FirebaseTaskViewHolder>(Task.class, R.layout.task_list_item, FirebaseTaskViewHolder.class, mTaskReference) {
            @Override
            protected void populateViewHolder(FirebaseTaskViewHolder viewHolder, Task model, int position) {
                viewHolder.bindTask(model);
            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

    @Override
    public void onClick(View v) {
        if (v == mAddButton) {
            String newTask = mNewTask.getText().toString().trim();
            Task task = new Task(newTask);

            DatabaseReference pushRef = FirebaseDatabase.getInstance().getReference("tasks").push();
            String pushId = pushRef.getKey();
            pushRef.setValue(task);

            mNewTask.setText("");
        }
    }
}
