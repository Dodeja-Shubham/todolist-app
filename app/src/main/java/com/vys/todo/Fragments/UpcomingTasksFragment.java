package com.vys.todo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.MainActivity;
import com.vys.todo.R;

import java.util.List;


public class UpcomingTasksFragment extends Fragment {

    private String TAG = "UpcomingTasksFrragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Database db = new Database(getContext());

        List<TaskDataModel> tasks = db.getAllTasks();

        for(int i = 0;i < tasks.size();i++){
            Log.e(TAG,String.valueOf(tasks.get(i).getId()));
            Log.e(TAG,tasks.get(i).getTitle());
            Log.e(TAG,tasks.get(i).getCategory());
            Log.e(TAG,tasks.get(i).getColour());
            Log.e(TAG,tasks.get(i).getCreated_at());
            Log.e(TAG,tasks.get(i).getDue_date());
        }
        return inflater.inflate(R.layout.fragment_upcoming_tasks, container, false);
    }
}