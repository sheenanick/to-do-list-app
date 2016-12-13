package com.epicodus.todolist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import com.epicodus.todolist.models.Task;
import com.epicodus.todolist.util.ItemTouchHelperAdapter;
import com.epicodus.todolist.util.OnStartDragListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseTaskListAdapter extends FirebaseRecyclerAdapter<Task, FirebaseTaskViewHolder> implements ItemTouchHelperAdapter {
    private OnStartDragListener mOnStartDragListener;
    private DatabaseReference mRef;
    private Context mContext;
    private ChildEventListener mChildEventListener;
    private ArrayList<Task> mTasks = new ArrayList<>();
    private Task mTask;

    public FirebaseTaskListAdapter(Class<Task> modelClass, int modelLayout, Class<FirebaseTaskViewHolder> viewHolderClass, Query ref, OnStartDragListener onStartDragListener, Context context) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;

        mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mTasks.add(dataSnapshot.getValue(Task.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void populateViewHolder(final FirebaseTaskViewHolder viewHolder, Task model, int position) {
        mTask = model;
        viewHolder.bindTask(model);
        viewHolder.mTaskDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mOnStartDragListener.onStartDrag(viewHolder);
                }
                return false;
            }
        });
        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.mCheckBox.isChecked()) {
                    mTask.setComplete(true);
                    viewHolder.mTaskDescription.setTextColor(Color.parseColor("#A9A9A9"));
                } else {
                    mTask.setComplete(false);
                    viewHolder.mTaskDescription.setTextColor(Color.parseColor("#3F51B5"));
                }
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mTasks, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mTasks.remove(position);
        getRef(position).removeValue();
    }

    private void setIndexInFirebase() {
        for (Task task : mTasks) {
            int index = mTasks.indexOf(task);
            DatabaseReference ref = getRef(index);
            task.setIndex(Integer.toString(index));
            ref.setValue(task);
        }
    }

    @Override
    public void cleanup() {
        super.cleanup();
        setIndexInFirebase();
        mRef.removeEventListener(mChildEventListener);
    }
}
