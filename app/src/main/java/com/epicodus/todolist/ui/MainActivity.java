package com.epicodus.todolist.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.epicodus.todolist.R;
import com.epicodus.todolist.adapters.FirebaseTaskListAdapter;
import com.epicodus.todolist.adapters.FirebaseTaskViewHolder;
import com.epicodus.todolist.models.Task;
import com.epicodus.todolist.util.OnStartDragListener;
import com.epicodus.todolist.util.SimpleItemTouchHelperCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnStartDragListener {
    @BindView(R.id.newTaskEditText) EditText mNewTask;
    @BindView(R.id.addButton) Button mAddButton;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.completedRecyclerView) RecyclerView mCompletedRecyclerView;
    @BindView(R.id.showCompleted) TextView mShowCompletedTextView;
    @BindView(R.id.hideCompleted) TextView mHideCompletedTextView;

    private FirebaseTaskListAdapter mTaskFirebaseAdapter;
    private FirebaseTaskListAdapter mCompletedFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAddButton.setOnClickListener(this);
        mShowCompletedTextView.setOnClickListener(this);
        mHideCompletedTextView.setOnClickListener(this);

        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        Query tasks = FirebaseDatabase.getInstance().getReference("tasks").orderByChild("index");

        mTaskFirebaseAdapter = new FirebaseTaskListAdapter(Task.class,
                R.layout.task_list_item, FirebaseTaskViewHolder.class, tasks, this, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mTaskFirebaseAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mTaskFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        Query completed = FirebaseDatabase.getInstance().getReference("complete").orderByChild("index");
        mCompletedFirebaseAdapter = new FirebaseTaskListAdapter(Task.class,
                R.layout.task_list_item, FirebaseTaskViewHolder.class, completed, this, this);

        mCompletedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCompletedRecyclerView.setAdapter(mCompletedFirebaseAdapter);

        ItemTouchHelper.Callback completedCallback = new SimpleItemTouchHelperCallback(mCompletedFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(completedCallback);
        mItemTouchHelper.attachToRecyclerView(mCompletedRecyclerView);
    }

    @Override
    public void onClick(View v) {
        if (v == mAddButton) {
            String newTask = mNewTask.getText().toString().trim();

            if (newTask.equals("")) {
                mNewTask.setError("Please enter a task");
                return;
            }

            Task task = new Task(newTask);

            DatabaseReference pushRef = FirebaseDatabase.getInstance().getReference("tasks").push();
            String pushId = pushRef.getKey();
            task.setPushId(pushId);
            pushRef.setValue(task);

            mNewTask.setText("");
        }
        if (v == mShowCompletedTextView) {
            mTaskFirebaseAdapter.moveComplete();
            mCompletedFirebaseAdapter.moveToTasks();
            mCompletedRecyclerView.setVisibility(View.VISIBLE);
            mShowCompletedTextView.setVisibility(View.GONE);
            mHideCompletedTextView.setVisibility(View.VISIBLE);
        }
        if (v == mHideCompletedTextView) {
            mTaskFirebaseAdapter.moveComplete();
            mCompletedFirebaseAdapter.moveToTasks();
            mCompletedRecyclerView.setVisibility(View.GONE);
            mShowCompletedTextView.setVisibility(View.VISIBLE);
            mHideCompletedTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTaskFirebaseAdapter.moveComplete();
        mCompletedFirebaseAdapter.moveToTasks();
        mTaskFirebaseAdapter.cleanup();
        mCompletedFirebaseAdapter.cleanup();
    }
}
