package com.epicodus.todolist.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.epicodus.todolist.R;
import com.epicodus.todolist.adapters.FirebaseTaskListAdapter;
import com.epicodus.todolist.adapters.FirebaseTaskViewHolder;
import com.epicodus.todolist.models.Task;
import com.epicodus.todolist.util.OnStartDragListener;
import com.epicodus.todolist.util.SimpleItemTouchHelperCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnStartDragListener {
    @Bind(R.id.newTaskEditText) EditText mNewTask;
    @Bind(R.id.addButton) Button mAddButton;
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    private FirebaseTaskListAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAddButton.setOnClickListener(this);


        setUpFirebaseAdapter();
    }

    private void setUpFirebaseAdapter() {
        Query query = FirebaseDatabase.getInstance().getReference("tasks").orderByChild("index");

        mFirebaseAdapter = new FirebaseTaskListAdapter(Task.class,
                R.layout.task_list_item, FirebaseTaskViewHolder.class, query, this, this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
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
            task.setPushId(pushId);
            pushRef.setValue(task);

            mNewTask.setText("");
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
