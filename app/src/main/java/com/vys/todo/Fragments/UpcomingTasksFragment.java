package com.vys.todo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vys.todo.Adapters.UpcomingTasksAdapter;
import com.vys.todo.Data.Database;
import com.vys.todo.Data.TaskDataModel;
import com.vys.todo.MainActivity;
import com.vys.todo.R;

import java.util.List;


public class UpcomingTasksFragment extends Fragment {

    private String TAG = "UpcomingTasksFragment";
    private RecyclerView upcomingRV;
    private List<TaskDataModel> allTasks;
    private UpcomingTasksAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            reloadDataDB();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upcoming_tasks, container, false);
        Database db = new Database(getContext());
        allTasks = db.getAllTasks();
        adapter = new UpcomingTasksAdapter(getContext(),allTasks);
        upcomingRV = v.findViewById(R.id.upcoming_rv);
        upcomingRV.setLayoutManager(new LinearLayoutManager(getContext()));
        upcomingRV.setAdapter(adapter);
        return v;
    }

    private void reloadDataDB(){
        Database db = new Database(getContext());
        allTasks = db.getAllTasks();
        if(adapter != null){
            adapter.setNewData(allTasks);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadDataDB();
    }
}